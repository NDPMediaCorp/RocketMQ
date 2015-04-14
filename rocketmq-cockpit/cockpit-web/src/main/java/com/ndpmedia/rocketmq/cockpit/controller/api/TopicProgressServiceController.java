package com.ndpmedia.rocketmq.cockpit.controller.api;

import com.ndpmedia.rocketmq.cockpit.model.*;
import com.ndpmedia.rocketmq.cockpit.mybatis.mapper.ConsumeProgressMapper;
import com.ndpmedia.rocketmq.cockpit.util.LoginConstant;
import com.ndpmedia.rocketmq.cockpit.util.WebHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/api/topic-progress")
public class TopicProgressServiceController {

    @Autowired
    private ConsumeProgressMapper consumeProgressMapper;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<String> list(HttpServletRequest request) {
        return consumeProgressMapper.topicList(null);
    }

    @RequestMapping(value = "/{topic}", method = RequestMethod.GET)
    @ResponseBody
    public List<ConsumeProgress> list(@PathVariable("topic") String topic){
        return consumeProgressMapper.brokerTPSList(null, topic, null, -1);
    }
}
