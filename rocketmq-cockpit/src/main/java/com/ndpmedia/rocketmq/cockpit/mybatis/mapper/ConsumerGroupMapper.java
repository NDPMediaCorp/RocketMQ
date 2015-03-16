package com.ndpmedia.rocketmq.cockpit.mybatis.mapper;

import com.ndpmedia.rocketmq.cockpit.model.ConsumerGroup;

import java.util.List;

public interface ConsumerGroupMapper {

    List<ConsumerGroup> list();

    ConsumerGroup get(long id);

    ConsumerGroup getByGroupName(String groupName);

    List<ConsumerGroup> listByClusterName(String clusterName);

    List<ConsumerGroup> listByTeamId(String clusterName);

    long insert(ConsumerGroup consumerGroup);

    void update(ConsumerGroup consumerGroup);

    void register(long id);

    void delete(long id);


}
