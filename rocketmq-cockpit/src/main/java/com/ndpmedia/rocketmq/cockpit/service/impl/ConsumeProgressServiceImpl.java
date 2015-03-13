package com.ndpmedia.rocketmq.cockpit.service.impl;

import com.alibaba.rocketmq.common.admin.ConsumeStats;
import com.alibaba.rocketmq.common.admin.OffsetWrapper;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.tools.admin.DefaultMQAdminExt;
import com.ndpmedia.rocketmq.cockpit.model.ConsumeProgress;
import com.ndpmedia.rocketmq.cockpit.model.ConsumerGroup;
import com.ndpmedia.rocketmq.cockpit.mybatis.mapper.ConsumerGroupMapper;
import com.ndpmedia.rocketmq.cockpit.service.ConsumeProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service("consumeProgressService")
public class ConsumeProgressServiceImpl implements ConsumeProgressService {

    @Autowired
    private ConsumerGroupMapper consumerGroupMapper;

    @Override
    public List<ConsumeProgress> queryConsumerProgress(String groupName, String topic, String broker) {
        List<ConsumeProgress> consumeProgressList = new ArrayList<ConsumeProgress>();
        DefaultMQAdminExt defaultMQAdminExt = new DefaultMQAdminExt();
        defaultMQAdminExt.setInstanceName(Long.toString(System.currentTimeMillis()));
        ConsumerGroup consumerGroup = consumerGroupMapper.getByGroupName(groupName);
        try {
            defaultMQAdminExt.start();
            // 查询特定consumer
            ConsumeStats consumeStats = defaultMQAdminExt.examineConsumeStats(groupName);

            List<MessageQueue> messageQueueList = new LinkedList<MessageQueue>();
            messageQueueList.addAll(consumeStats.getOffsetTable().keySet());
            Collections.sort(messageQueueList);

            long diffTotal = 0L;

            for (MessageQueue messageQueue : messageQueueList) {
                OffsetWrapper offsetWrapper = consumeStats.getOffsetTable().get(messageQueue);
                if (null != topic && !topic.equals(messageQueue.getTopic())) {
                    continue;
                }

                if (null != broker && !broker.equals(messageQueue.getBrokerName())) {
                    continue;
                }

                long diff = offsetWrapper.getBrokerOffset() - offsetWrapper.getConsumerOffset();
                diffTotal += diff;

                consumeProgressList.add(new ConsumeProgress(consumerGroup, messageQueue, offsetWrapper, diff));
            }

            consumeProgressList.add(new ConsumeProgress(consumerGroup, null, null, diffTotal));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            defaultMQAdminExt.shutdown();
        }
        return consumeProgressList;
    }
}