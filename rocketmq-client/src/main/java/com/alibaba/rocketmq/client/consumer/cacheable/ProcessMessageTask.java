package com.alibaba.rocketmq.client.consumer.cacheable;

import com.alibaba.rocketmq.client.log.ClientLogger;
import com.alibaba.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;

public class ProcessMessageTask implements Runnable {

    private static final Logger LOGGER = ClientLogger.getLog();

    private static final String NEXT_TIME_KEY = "next_time";

    private MessageExt message;

    private MessageHandler messageHandler;

    private CacheableConsumer cacheableConsumer;

    public ProcessMessageTask(MessageExt message, //Message to process.
                              MessageHandler messageHandler, //Message handler.
                              CacheableConsumer cacheableConsumer
    ) {
        this.message = message;
        this.messageHandler = messageHandler;
        this.cacheableConsumer = cacheableConsumer;
    }

    @Override
    public void run() {
        try {
            long start = System.currentTimeMillis();
            int result = messageHandler.handle(message);
            if (0 == result) {
                cacheableConsumer.getStatistics().addValue(System.currentTimeMillis() - start);
                cacheableConsumer.getSuccessCounter().incrementAndGet();
            } else if (result > 0) {
                cacheableConsumer.getFailureCounter().incrementAndGet();
                cacheableConsumer.getMessageQueue().offer(message);
            } else {
                LOGGER.error("Unable to process returning result: " + result);
            }
        } catch (Exception e) {
            LOGGER.error("ProcessMessageTask failed! Automatic retry scheduled.", e);
            cacheableConsumer.getMessageQueue().offer(message);
        } finally {
            cacheableConsumer.getInProgressMessageQueue().remove(message);
        }
    }

}
