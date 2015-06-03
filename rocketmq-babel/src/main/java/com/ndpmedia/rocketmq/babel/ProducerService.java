package com.ndpmedia.rocketmq.babel;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.log.ClientLogger;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import org.apache.thrift.TException;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ProducerService implements Producer.Iface {

    private static final Logger LOG = ClientLogger.getLog();

    private DefaultMQProducer producer;

    public ProducerService() {
        producer = new DefaultMQProducer(Helper.getConfig().getProperty("producer_group"));
        try {
            producer.start();
        } catch (MQClientException e) {
            LOG.error("Failed to start DefaultMQProducer", e);
        }
    }



    @Override
    public String send(Message message) throws TException {
        try {
            SendResult sendResult = producer.send(Helper.wrap(message));
            return sendResult.getMsgId();
        } catch (Exception e) {
            LOG.error("Send message error", e);
            throw new TException("Send message failed", e);
        }
    }

    @Override
    public List<String> batchSend(List<Message> messageList) throws TException {
        List<String> result = new ArrayList<String>(messageList.size());
        for (Message msg : messageList) {
            result.add(send(msg));
        }
        return result;
    }

    @Override
    public void stop() throws TException {
        producer.shutdown();
    }
}
