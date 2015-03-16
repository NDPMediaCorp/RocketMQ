package com.ndpmedia.rocketmq.cockpit.mybatis.mapper;

import com.ndpmedia.rocketmq.cockpit.model.ConsumeProgress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConsumeProgressMapper {

    long insert(ConsumeProgress consumeProgress);

    void delete(long id);

    /**
     * Retrieve consume progress records by specified parameters. All of these parameters are optional.
     * @param consumerGroup Optional consumer group.
     * @param topic Optional topic.
     * @param brokerName Optional broker name.
     * @param queueId Optional queue ID, use -1 in case all queues are wanted.
     * @return list of consume progress records.
     */
    List<ConsumeProgress> list(@Param("consumerGroup") String consumerGroup,
                               @Param("topic")String topic,
                               @Param("brokerName")String brokerName,
                               @Param("queueId")int queueId);
}
