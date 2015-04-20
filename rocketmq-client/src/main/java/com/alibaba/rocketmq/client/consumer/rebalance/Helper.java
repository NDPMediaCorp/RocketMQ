package com.alibaba.rocketmq.client.consumer.rebalance;

import com.alibaba.rocketmq.common.message.MessageQueue;

import java.util.List;
import java.util.regex.Pattern;

public final class Helper {

    public static final String BROKER_NAME_REGEX = "\\p{Alnum}{1,}_(\\d{1,})_\\p{Alnum}{1,}";

    public static final Pattern BROKER_NAME_PATTERN = Pattern.compile(BROKER_NAME_REGEX);

    public static void checkRebalanceParameters(String consumerGroup, String currentCID, List<MessageQueue> mqAll,
                                                List<String> cidAll) {
        if (currentCID == null || currentCID.length() < 1) {
            throw new IllegalArgumentException("currentCID is empty");
        }
        if (mqAll == null || mqAll.isEmpty()) {
            throw new IllegalArgumentException("mqAll is null or mqAll empty");
        }
        if (cidAll == null || cidAll.isEmpty()) {
            throw new IllegalArgumentException("cidAll is null or cidAll empty");
        }
    }
}
