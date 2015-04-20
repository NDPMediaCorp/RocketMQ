/**
 * Copyright (C) 2010-2013 Alibaba Group Holding Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.rocketmq.client;

import com.alibaba.rocketmq.client.consumer.DefaultMQPullConsumer;
import com.alibaba.rocketmq.client.log.ClientLogger;
import com.alibaba.rocketmq.common.Pair;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author shijia.wxr<vintage.wang@gmail.com>
 * @since 2013-11-13
 */
public class MQHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQHelper.class);

    public static final String IP_REGEX = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public static final Pattern IP_PATTERN = Pattern.compile(IP_REGEX);

    /**
     * 根据时间戳来重置一个订阅组的消费进度
     * 
     * @param messageModel
     *            广播消费还是集群消费
     * @param instanceName
     *            实例名称，保持与工作Consumer一致。
     * @param consumerGroup
     *            订阅组
     * @param topic
     *            topic
     * @param timestamp
     *            时间戳
     * @throws Exception
     */
    public static void resetOffsetByTimestamp(//
            final MessageModel messageModel,//
            final String instanceName,//
            final String consumerGroup, //
            final String topic, //
            final long timestamp) throws Exception {
        final Logger log = ClientLogger.getLog();

        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer(consumerGroup);
        consumer.setInstanceName(instanceName);
        consumer.setMessageModel(messageModel);
        consumer.start();

        Set<MessageQueue> mqs = null;
        try {
            mqs = consumer.fetchSubscribeMessageQueues(topic);
            if (mqs != null && !mqs.isEmpty()) {
                TreeSet<MessageQueue> mqsNew = new TreeSet<MessageQueue>(mqs);
                for (MessageQueue mq : mqsNew) {
                    long offset = consumer.searchOffset(mq, timestamp);
                    if (offset >= 0) {
                        consumer.updateConsumeOffset(mq, offset);
                        log.info("resetOffsetByTimestamp updateConsumeOffset success, {} {} {}",
                            consumerGroup, offset, mq);
                    }
                }
            }
        }
        catch (Exception e) {
            log.warn("resetOffsetByTimestamp Exception", e);
            throw e;
        }
        finally {
            if (mqs != null) {
                consumer.getDefaultMQPullConsumerImpl().getOffsetStore().persistAll(mqs);
            }
            consumer.shutdown();
        }
    }


    public static void resetOffsetByTimestamp(//
            final MessageModel messageModel,//
            final String consumerGroup, //
            final String topic, //
            final long timestamp) throws Exception {
        resetOffsetByTimestamp(messageModel, "DEFAULT", consumerGroup, topic, timestamp);
    }

    /**
     * Only IPv4 is supported.
     * @param range
     * @param ip
     * @return
     */
    public static boolean isIPinRange(String range, String ip) {
        List<Pair<Long, Long>> numericalIPRanges = buildIPRanges(range);
        long value = ipAddressToNumber(ip);
        for (Pair<Long, Long> pair : numericalIPRanges) {
            if (pair.getObject1() <= value && pair.getObject2() >= value) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * Valid ranges may be a single IPv4 address or two IPv4 addresses joined by "-". If the latter form is used, we
     * assume the starting IP is smaller or equal to the ending IP. Multiple ranges are concatenated by semi-colon ";".
     * </p>
     *
     * <p>
     *     For example, the following ranges are all valid:
     *     <ul>
     *         <li>10.2.2.1</li>
     *         <li>10.1.36.10-10.1.36.14;10.2.2.1</li>
     *         <li>10.1.36.10-10.1.36.14;10.2.2.1-10.3.255.255</li>
     *     </ul>
     * </p>
     *
     * @param range ranges of IP address, conforming to the rules described above.
     * @return Numerical representation of the ranges list.
     */
    public static List<Pair<Long, Long>> buildIPRanges(String range) {
        List<Pair<Long, Long>> numericalIPRanges = new ArrayList<Pair<Long, Long>>();

        if (null == range || range.isEmpty()) {
            return numericalIPRanges;
        }

        String[] ipRanges = range.split(";");
        for (String ipRange : ipRanges) {
            if (ipRange.contains("-")) {
                String[] ipAddresses = ipRange.split("-");
                if (ipAddresses.length == 2) {
                    long ipStart = ipAddressToNumber(ipAddresses[0]);
                    long ipEnd = ipAddressToNumber(ipAddresses[1]);

                    if (ipStart > 0 && ipStart <= ipEnd) {
                        Pair<Long, Long> rangeItem = new Pair<Long, Long>(ipStart, ipEnd);
                        numericalIPRanges.add(rangeItem);
                    } else {
                        LOGGER.error("Starting range IP: {} is less than ending IP: {}", ipAddresses[0], ipAddresses[1]);
                    }
                }
            } else {
                long numericalIP = ipAddressToNumber(ipRange);
                if (numericalIP > 0) {
                    Pair<Long, Long> rangeItem = new Pair<Long, Long>(numericalIP, numericalIP);
                    numericalIPRanges.add(rangeItem);
                } else {
                    LOGGER.error("Ignoring mal-formed IP address: {}", ipRange);
                }
            }
        }

        return numericalIPRanges;

    }


    public static long ipAddressToNumber(String ipAddress) {
        Matcher matcher = IP_PATTERN.matcher(ipAddress);
        if (matcher.matches()) {
            String[] segments = ipAddress.split("\\.");
            long[] numericalSegments = new long[segments.length];
            for (int i = 0; i < segments.length; i++) {
                numericalSegments[i] = Long.parseLong(segments[i]);
                numericalSegments[i] = numericalSegments[i] << ((segments.length - 1 - i) * 8);
            }
            long result = 0;
            for (long numericalSegment : numericalSegments) {
                result += numericalSegment;
            }
            return result;
        }
        return -1;
    }
}
