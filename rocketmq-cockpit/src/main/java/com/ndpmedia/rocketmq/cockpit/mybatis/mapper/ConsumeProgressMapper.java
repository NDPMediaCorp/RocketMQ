package com.ndpmedia.rocketmq.cockpit.mybatis.mapper;

import com.ndpmedia.rocketmq.cockpit.model.ConsumeProgress;

import java.util.List;
import java.util.Map;

public interface ConsumeProgressMapper {

    long insert(ConsumeProgress consumeProgress);

    void delete(long id);

    List<ConsumeProgress> list();

    List<ConsumeProgress> listByConsumerGroup(String consumerGroup);

    List<ConsumeProgress> listByConsumerGroupThenTopic(Map<String, String> map);
}
