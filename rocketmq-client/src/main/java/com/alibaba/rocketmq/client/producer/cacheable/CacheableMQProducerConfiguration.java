package com.alibaba.rocketmq.client.producer.cacheable;

public class CacheableMQProducerConfiguration {

    private String producerGroup;

    private int defaultTopicQueueNumber = 16;

    private int retryTimesBeforeSendingFailureClaimed = 3;

    private int sendMessageTimeOutInMilliSeconds = 3000;

    private int resendFailureMessageDelay = 2000;

    public static final int MAXIMUM_NUMBER_OF_MESSAGE_PERMITS = 20000;

    public static final int MINIMUM_NUMBER_OF_MESSAGE_PERMITS = 3000;

    private int initialNumberOfMessagePermits = MINIMUM_NUMBER_OF_MESSAGE_PERMITS;

    public CacheableMQProducerConfiguration configureProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
        return this;
    }

    public CacheableMQProducerConfiguration configureDefaultTopicQueueNumber(int defaultTopicQueueNumber) {
        this.defaultTopicQueueNumber = defaultTopicQueueNumber;
        return this;
    }

    public CacheableMQProducerConfiguration configureRetryTimesBeforeSendingFailureClaimed(int retryTimesBeforeSendingFailureClaimed) {
        this.retryTimesBeforeSendingFailureClaimed = retryTimesBeforeSendingFailureClaimed;
        return this;
    }

    public CacheableMQProducerConfiguration configureSendMessageTimeOutInMilliSeconds(int sendMessageTimeOutInMilliSeconds) {
        this.sendMessageTimeOutInMilliSeconds = sendMessageTimeOutInMilliSeconds;
        return this;
    }

    public CacheableMQProducerConfiguration configureResendFailureMessageDelay(int resendFailureMessageDelay) {
        this.resendFailureMessageDelay = resendFailureMessageDelay;
        return this;
    }

    public CacheableMQProducerConfiguration configureInitialNumberOfMessagePermits(int initialNumberOfMessagePermits) {
        this.initialNumberOfMessagePermits = initialNumberOfMessagePermits;
        return this;
    }

    public CacheableMQProducer build() {
        if (!isReadyToBuild()) {
            throw new RuntimeException(reportMissingConfiguration());
        }

        return new CacheableMQProducer(this);
    }

    private boolean isReadyToBuild() {
        return null != producerGroup;
    }

    private String reportMissingConfiguration() {
        StringBuilder stringBuilder = null;

        if (null == producerGroup) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Producer Group required");
        }

        return stringBuilder != null ? stringBuilder.toString() : null;
    }


    public String getProducerGroup() {
        return producerGroup;
    }

    public int getDefaultTopicQueueNumber() {
        return defaultTopicQueueNumber;
    }

    public int getRetryTimesBeforeSendingFailureClaimed() {
        return retryTimesBeforeSendingFailureClaimed;
    }

    public int getSendMessageTimeOutInMilliSeconds() {
        return sendMessageTimeOutInMilliSeconds;
    }

    public int getResendFailureMessageDelay() {
        return resendFailureMessageDelay;
    }

    public int getInitialNumberOfMessagePermits() {
        return initialNumberOfMessagePermits;
    }
}
