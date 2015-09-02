package com.alibaba.rocketmq.common.message;

import java.nio.ByteBuffer;

public class MessageEncoder {

    public static final int MAGIC_CODE = 0xAABBCCDD ^ 1880681586 + 8;

    /**
     * 序列化消息
     */
    public static ByteBuffer encode(MessageExt msg) {
        final byte[] propertiesData = msg.getProperties() == null ? null : MessageDecoder.messageProperties2String(msg.getProperties()).getBytes();
        final int propertiesLength = propertiesData == null ? 0 : propertiesData.length;
        final byte[] topicData = msg.getTopic().getBytes();
        final int topicLength = topicData.length;
        final int bodyLength = msg.getBody() == null ? 0 : msg.getBody().length;

        final int msgLen = 4 // 1 TOTAL SIZE
                + 4 // 2 MAGIC CODE
                + 4 // 3 BODY CRC
                + 4 // 4 QUEUE ID
                + 4 // 5 FLAG
                + 8 // 6 QUEUE OFFSET
                + 8 // 7 commit log offset
                + 4 // 8 SYS FLAG
                + 8 // 9 BORN TIMESTAMP
                + 8 // 10 BORN HOST
                + 8 // 11 STORE TIMESTAMP
                + 8 // 12 STORE HOST ADDRESS
                + 4 // 13 RE-CONSUME TIMES
                + 8 // 14 Prepared Transaction Offset
                + 4 + bodyLength // 14 BODY
                + 1 + topicLength // 15 TOPIC
                + 2 + propertiesLength; // 16 propertiesLength

        ByteBuffer byteBuffer = ByteBuffer.allocate(msgLen);

        // 1 TOTALSIZE
        byteBuffer.putInt(msgLen);

        // 2 MAGICCODE
        byteBuffer.putInt(MAGIC_CODE);

        // 3 BODY CRC
        byteBuffer.putInt(msg.getBodyCRC());

        // 4 QUEUE ID
        byteBuffer.putInt(msg.getQueueId());

        // 5 FLAG
        byteBuffer.putInt(msg.getFlag());

        // 6 QUEUE OFFSET
        byteBuffer.putLong(msg.getQueueOffset());

        // 7 commit log offset
        byteBuffer.putLong(msg.getCommitLogOffset());

        // 8 System FLAG
        byteBuffer.putInt(msg.getSysFlag());

        // 9 BORN TIMESTAMP
        byteBuffer.putLong(msg.getBornTimestamp());

        // 10 BORN HOST
        byteBuffer.put(msg.getBornHostBytes());

        // 11 STORE TIMESTAMP
        byteBuffer.putLong(msg.getStoreTimestamp());

        // 12 STORE HOST ADDRESS
        byteBuffer.put(msg.getStoreHostBytes());

        // 13 RE-CONSUME TIMES
        byteBuffer.putInt(msg.getReconsumeTimes());

        // 14 Prepared Transaction Offset
        byteBuffer.putLong(msg.getPreparedTransactionOffset());

        // 15 BODY
        byteBuffer.putInt(bodyLength);
        if (bodyLength > 0) {
            byteBuffer.put(msg.getBody());
        }

        // 16 TOPIC
        byteBuffer.put((byte) topicLength);
        byteBuffer.put(topicData);

        // 17 PROPERTIES
        byteBuffer.putShort((short) propertiesLength);
        if (propertiesLength > 0) {
            byteBuffer.put(propertiesData);
        }

        byteBuffer.flip();
        return byteBuffer;
    }

}
