package com.alibaba.rocketmq.client.producer.concurrent;

/**
 * Created by macbookpro on 15/8/30.
 */
public class IllegalMagicCodeException extends Exception {
    public IllegalMagicCodeException(String message) {
        super(message);
    }

    public IllegalMagicCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalMagicCodeException(Throwable cause) {
        super(cause);
    }

    protected IllegalMagicCodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
