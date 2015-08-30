package com.alibaba.rocketmq.client.producer.concurrent;

/**
 * Created by macbookpro on 15/8/30.
 */
public class LocalMessageStoreException extends Exception {
    public LocalMessageStoreException(String message) {
        super(message);
    }

    public LocalMessageStoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocalMessageStoreException(Throwable cause) {
        super(cause);
    }

    protected LocalMessageStoreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
