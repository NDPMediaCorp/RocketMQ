package com.ndpmedia.rocketmq.cockpit.model;

import java.util.Map;

/**
 * Created by robert.xu on 2015/4/8.
 */
public class CockpitMessage {
    /**
     * 消息ID
     */
    private String msgId;

    /**
     * 消息标签
     */
    private String tags;

    /**
     * 消息关键字
     */
    private String keys;

    /**
     * 消息主题
     */
    private String topic;

    /**
     * 消息体
     */
    private byte[] body;

    /**
     * 消息属性，都是系统属性，禁止应用设置
     */
    private Map<String, String> properties;

    /**
     * 消息存储时间戳
     */
    private long storTime;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public long getStorTime() {
        return storTime;
    }

    public void setStorTime(long storTime) {
        this.storTime = storTime;
    }
}
