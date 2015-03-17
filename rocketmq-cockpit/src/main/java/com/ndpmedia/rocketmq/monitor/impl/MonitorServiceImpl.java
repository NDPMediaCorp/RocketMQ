package com.ndpmedia.rocketmq.monitor.impl;

import com.ndpmedia.rocketmq.cockpit.model.ConsumeProgress;
import com.ndpmedia.rocketmq.cockpit.mybatis.mapper.ConsumeProgressMapper;
import com.ndpmedia.rocketmq.monitor.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MonitorServiceImpl implements MonitorService {

    @Autowired
    private ConsumeProgressMapper consumeProgressMapper;

    @Override
    public List<ConsumeProgress> monitor(String consumerGroup) {
        return consumeProgressMapper.list(consumerGroup, null, null, -1);
    }

    @Override
    public List<ConsumeProgress> monitor(String consumerGroup, String topic) {
        return consumeProgressMapper.list(consumerGroup, topic, null, -1);
    }
}
