package com.ndpmedia.rocketmq.cockpit.scheduler;

import com.alibaba.rocketmq.common.MixAll;
import com.ndpmedia.rocketmq.cockpit.model.ConsumeProgress;
import com.ndpmedia.rocketmq.cockpit.mybatis.mapper.ConsumeProgressMapper;
import com.ndpmedia.rocketmq.cockpit.service.ConsumeProgressService;
import com.ndpmedia.rocketmq.cockpit.service.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class TaskScheduler implements Runnable{

    private Logger logger = LoggerFactory.getLogger(TaskScheduler.class);

    @Autowired
    private ConsumeProgressMapper consumeProgressMapper;

    @Autowired
    private ConsumeProgressService consumeProgressService;

    @Autowired
    private TopicService topicService;

    @Scheduled(fixedDelay = 5000)
    @Override
    public void run() {
        Date date = new Date();
        try {
            Set<String> topicList = topicService.fetchTopics();

            List<ConsumeProgress> consumeProgressList;
            for (String topic : topicList) {
                if (!topic.contains(MixAll.RETRY_GROUP_TOPIC_PREFIX)) {
                    //All operational consumer groups have a topic with pattern: %RETRY%_{ConsumerGroup}
                    continue;
                }

                consumeProgressList = consumeProgressService
                        .queryConsumerProgress(topic.replace(MixAll.RETRY_GROUP_TOPIC_PREFIX, ""), null, null);
                for (ConsumeProgress cp : consumeProgressList) {
                    if (null == cp || null == cp.getTopic() || null == cp.getBrokerName()) {
                        continue;
                    }
                    cp.setCreateTime(date);
                    consumeProgressMapper.insert(cp);
                }
            }
        } catch (Exception e) {
            if (!e.getMessage().contains("offset table is empty")) {
                logger.warn("[MONITOR][CONSUME PROCESS] main method failed." + e);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * *")
    public void deleteDeprecatedData() {
        logger.info("Start to clean deprecated data");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        int numberOfRecordsDeleted = consumeProgressMapper.bulkDelete(calendar.getTime());
        logger.info("Deleted " + numberOfRecordsDeleted + " consume progress records.");
    }
}
