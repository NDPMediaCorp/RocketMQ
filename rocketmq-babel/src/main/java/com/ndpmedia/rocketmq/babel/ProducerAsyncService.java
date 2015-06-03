/*
 * Copyright (c) 2015. All Rights Reserved.
 */
package com.ndpmedia.rocketmq.babel;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.log.ClientLogger;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.ndpmedia.rocketmq.babel.Producer.AsyncIface;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ProducerAsyncService implements AsyncIface {

    private static final Logger LOG = ClientLogger.getLog();

    private DefaultMQProducer producer;

    public ProducerAsyncService() {
        producer = new DefaultMQProducer(Helper.getConfig().getProperty("producer_group"));
        try {
            producer.start();
        } catch (MQClientException e) {
            LOG.error("Start DefaultMQProducer failed", e);
        }
    }

    @Override
    public void send(Message message, AsyncMethodCallback resultHandler) throws TException {
        try {
            producer.send(Helper.wrap(message), new BabelSendCallback(resultHandler));
        } catch (Exception e) {
            LOG.error("Send message failed", e);
            throw new TException("Send message failed", e);
        }
    }

    @Override
    public void batchSend(List<Message> messageList, AsyncMethodCallback resultHandler) throws TException {

        if (null == messageList || messageList.isEmpty()) {
            resultHandler.onError(new Exception("Message list being null or empty"));
            return;
        }

        final List<String> resultList = new ArrayList<String>(messageList.size());
        final CountDownLatch latch = new CountDownLatch(resultList.size());

        boolean hasError = false;
        for (Message message : messageList) {
            try {
                producer.send(Helper.wrap(message), new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        resultList.add(sendResult.getMsgId());
                        latch.countDown();
                    }

                    @Override
                    public void onException(Throwable e) {
                        resultList.add(null);
                        latch.countDown();
                    }
                });
            } catch (Exception e) {
                LOG.error("Batch send failed", e);
                hasError = true;
            }
        }

        try {
            latch.await();
            hasError = hasError && resultList.contains(null);
            if (hasError) {
                resultHandler.onError(new Exception("Batch send failed a few messages"));
            } else {
                resultHandler.onComplete(resultList);
            }
        } catch (InterruptedException e) {
            throw new TException("Batch send failed.", e);
        }
    }

    @Override
    public void stop(AsyncMethodCallback resultHandler) throws TException {
        producer.shutdown();
    }


    class BabelSendCallback implements SendCallback {

        private AsyncMethodCallback resultHandler;

        public BabelSendCallback(AsyncMethodCallback resultHandler) {
            this.resultHandler = resultHandler;
        }

        @Override
        public void onSuccess(SendResult sendResult) {
            resultHandler.onComplete(sendResult.getMsgId());
        }

        @Override
        public void onException(Throwable e) {
            resultHandler.onError(new Exception(e));
        }
    }
}
