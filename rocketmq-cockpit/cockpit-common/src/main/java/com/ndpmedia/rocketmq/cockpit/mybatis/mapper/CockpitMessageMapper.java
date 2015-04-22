package com.ndpmedia.rocketmq.cockpit.mybatis.mapper;

import com.ndpmedia.rocketmq.cockpit.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CockpitMessageMapper {

    List<CockpitMessageFlow> list(@Param("id") String id);

    String tracerId(@Param("id") String id);
}
