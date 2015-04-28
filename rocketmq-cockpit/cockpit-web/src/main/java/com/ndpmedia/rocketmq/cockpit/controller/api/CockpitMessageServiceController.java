package com.ndpmedia.rocketmq.cockpit.controller.api;

import com.alibaba.rocketmq.client.QueryResult;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.MixAll;
import com.alibaba.rocketmq.common.admin.ConsumeStats;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.alibaba.rocketmq.tools.admin.DefaultMQAdminExt;
import com.ndpmedia.rocketmq.cockpit.model.CockpitMessage;
import com.ndpmedia.rocketmq.cockpit.model.CockpitMessageFlow;
import com.ndpmedia.rocketmq.cockpit.mybatis.mapper.CockpitMessageMapper;
import com.ndpmedia.rocketmq.cockpit.util.MessageTranslate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by robert.xu on 2015/4/7.
 */
@Controller
@RequestMapping(value = "/api/message")
public class CockpitMessageServiceController {

    @Autowired
    private CockpitMessageMapper cockpitMessageMapper;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CockpitMessage getMessageByID(@PathVariable("id") String id) {
        DefaultMQAdminExt defaultMQAdminExt = new DefaultMQAdminExt();
        try {
            defaultMQAdminExt.start();
            MessageExt messageExt = defaultMQAdminExt.viewMessage(id);
            return MessageTranslate.translateFrom(messageExt);
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } finally {
            defaultMQAdminExt.shutdown();
        }
        return null;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, String>> getConsumerTypeByID(@PathVariable("id") String id) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        DefaultMQAdminExt defaultMQAdminExt = new DefaultMQAdminExt();
        MessageExt messageExt = null;
        try {
            defaultMQAdminExt.start();
            messageExt = defaultMQAdminExt.viewMessage(id);

        } catch (Exception e) {
            System.out.println("[QUERY MESSAGE] can not get message by id" + id);
        }


        Set<String> topics = null;
        try {
            topics = defaultMQAdminExt.fetchAllTopicList().getTopicList();
        } catch (Exception e) {
            System.out.println("[QUERY MESSAGE] can not get topics");
        }

        for (String topic : topics){
            Map<String, String> consumer = new HashMap<String, String>();
            if (topic.startsWith(MixAll.RETRY_GROUP_TOPIC_PREFIX)){
                ConsumeStats consumeStats = null;
                try {
                    consumeStats = defaultMQAdminExt.examineConsumeStats(topic.replace(MixAll.RETRY_GROUP_TOPIC_PREFIX, ""));

                    for (MessageQueue messageQueue : consumeStats.getOffsetTable().keySet()){
                        if (!messageExt.getTopic().equals(messageQueue.getTopic()))
                            continue;
                        consumer.put("Consumer", topic.replace(MixAll.RETRY_GROUP_TOPIC_PREFIX, ""));
                        if (consumeStats.getOffsetTable().get(messageQueue).getConsumerOffset() > messageExt.getQueueOffset())
                            consumer.put("type", "已投递");
                        else
                            consumer.put("type", "未投递");
                    }

                    if (!consumer.isEmpty())
                        result.add(consumer);
                } catch (Exception e) {
                    System.out.println("[QUERY MESSAGE] can not get consumer offset" + topic);
                }
            }
        }

        defaultMQAdminExt.shutdown();
        return result;
    }

    @RequestMapping(value = "/{topic}/{key}", method = RequestMethod.GET)
    @ResponseBody
    public List<CockpitMessage> getMessageByKey(@PathVariable("topic") String topic, @PathVariable("key") String key) {
        DefaultMQAdminExt defaultMQAdminExt = new DefaultMQAdminExt();
        try {
            defaultMQAdminExt.start();
            QueryResult queryResult = defaultMQAdminExt.queryMessage(topic, key, 32, 0, Long.MAX_VALUE);
            List<CockpitMessage> result = new ArrayList<CockpitMessage>();
            for (MessageExt messageExt : queryResult.getMessageList()) {
                result.add(MessageTranslate.translateFrom(messageExt));
            }
            return result;
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            defaultMQAdminExt.shutdown();
        }
        return null;
    }

    @RequestMapping(value = "/download/{msgId}", method = RequestMethod.GET)
    public void downloadMsg(@PathVariable("msgId") String msgId, HttpServletRequest request, HttpServletResponse response) {
        CockpitMessage cockpitMessage = this.getMessageByID(msgId);

        response.setContentType("application/x-download");
        String tempFileName = null;
        try {
            tempFileName = URLEncoder.encode(msgId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.addHeader("Content-Disposition", "attachment;filename=" + tempFileName);
        byte[] buffer = cockpitMessage.getBody();

        try {
            response.getOutputStream().write(buffer, 0, buffer.length);
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/flow/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<CockpitMessageFlow> getFlowByID(@PathVariable("id") String id){
        String tracerId = cockpitMessageMapper.tracerId(id);
        return cockpitMessageMapper.list(id, tracerId);
    }
}
