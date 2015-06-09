/*
 * Copyright (c) 2015. All Rights Reserved.
 */
package com.alibaba.rocketmq.remoting.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class CloudUtil {

    private static final Logger LOG = LoggerFactory.getLogger(RemotingHelper.RemotingLogName);

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private static final String AWS_EC2_QUERY_PUBLIC_IPV4_COMMAND = "ec2metadata --public-ipv4";

    private CloudUtil() {
    }

    /**
     * This method queries IPv4 address if the current instance is an AWS EC2.
     * @return Public IPv4 address of the current EC2 instance; null if it's not AWS EC2.
     */
    public static String awsEC2QueryPublicIPv4() {
        return executeCommand(AWS_EC2_QUERY_PUBLIC_IPV4_COMMAND);
    }

    /**
     * This method would execute the command and return execution result.
     * @param command Command to execute.
     * @return execution result if successful; null otherwise.
     */
    public static String executeCommand(String command) {
        BufferedReader bufferedReader = null;
        Process process =  null;
        try {
            process = Runtime.getRuntime().exec(command);
            InputStream inputStream = null;
            if (null != process) {
                StringBuilder stringBuilder = new StringBuilder();
                if (0 == process.waitFor()) {
                    inputStream = process.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    boolean first = true;
                    while (null != (line = bufferedReader.readLine())) {
                        if (first) {
                            first = false;
                            stringBuilder.append(line);
                        } else {
                            stringBuilder.append(LINE_SEPARATOR).append(line);
                        }
                    }
                    return stringBuilder.toString();
                } else {
                    inputStream = process.getErrorStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    boolean first = true;
                    while (null != (line = bufferedReader.readLine())) {
                        if (first) {
                            first = false;
                            stringBuilder.append(line);
                        } else {
                            stringBuilder.append(LINE_SEPARATOR).append(line);
                        }
                    }
                    LOG.error("Execute command [" + command + "] failed due to non-zero return value. Error message: "
                            + stringBuilder.toString());
                    process.destroy();
                    return null;
                }
            }
            return null;
        } catch (Throwable e) {
            LOG.error("Failed to execute command [" + command + "]", e);
            e.printStackTrace();
            return null;
        } finally {
            if (null != bufferedReader) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
