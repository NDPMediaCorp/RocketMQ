package com.alibaba.rocketmq.option;

public final class CommandOption {

    public static final String OPTION_SSL = "ssl";

    private CommandOption() {}

    public static boolean hasOption(String options) {

        if (null == options) {
            throw new IllegalArgumentException("Option to check should not be null");
        }

        return "true".equals(System.getenv("ROCKETMQ_ENABLE_" + options.toUpperCase()))
                || "true".equals(System.getProperty("enable_" + options.toLowerCase()));

    }
}
