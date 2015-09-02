package com.alibaba.rocketmq.client.consumer.cacheable;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.log.ClientLogger;
import com.alibaba.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;

import java.util.List;

public class FrontController implements MessageListenerConcurrently {

    private static final Logger LOGGER = ClientLogger.getLog();

    private final CacheableConsumer cacheableConsumer;

    private JobSubmitter jobSubmitter;

    public FrontController(CacheableConsumer cacheableConsumer) {
        this.cacheableConsumer = cacheableConsumer;
        jobSubmitter = new JobSubmitter();
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages,
                                                    ConsumeConcurrentlyContext context) {
        if (null == messages) {
            LOGGER.error("Found null while preparing to consume messages in batch.");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }

        for (MessageExt message : messages) {
            if (null != message) {
                try {
                    if (cacheableConsumer.isAboutFull()) {
                        cacheableConsumer.suspend();
                        // Stash those pre-fetched message.
                        cacheableConsumer.getLocalMessageStore().stash(message);
                        LOGGER.warn("Client message queue is about to full; stop receiving message from broker.");
                    } else {
                        // Normal Processing.
                        cacheableConsumer.getMessageQueue().put(message);
                    }
                } catch (InterruptedException e) {
                    LOGGER.error("Failed to put message into message queue", e);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        }

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    public void stopSubmittingJob() {
        jobSubmitter.stop();
    }

    public void startSubmittingJob() {
        Thread jobSubmitterThread = new Thread(jobSubmitter);
        jobSubmitterThread.setName("JobSubmitter");
        jobSubmitterThread.start();
    }

    /**
     * This thread wraps messages from message queue into ProcessMessageTask items and submit them into
     */
    class JobSubmitter implements Runnable {

        private volatile boolean running = true;

        @Override
        public void run() {
            while (running) {
                try {
                    //Block if there is no message in the queue.
                    MessageExt message = cacheableConsumer.getMessageQueue().take();
                    final MessageHandler messageHandler = cacheableConsumer.getTopicHandlerMap().get(message.getTopic());
                    ProcessMessageTask task = new ProcessMessageTask(message, messageHandler, cacheableConsumer);
                    cacheableConsumer.getInProgressMessageQueue().put(message);
                    cacheableConsumer.getExecutorWorkerService().submit(task);
                } catch (Exception e) {
                    LOGGER.error("Error while submitting ProcessMessageTask", e);
                }
            }
        }

        public void stop() {
            running = false;
        }

    }
}

