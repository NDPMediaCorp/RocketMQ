package com.ndpmedia.rocketmq.cockpit.controller.api;

import com.ndpmedia.rocketmq.cockpit.model.CockpitUser;
import com.ndpmedia.rocketmq.cockpit.mybatis.mapper.CockpitUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/api/user")
public class UserServiceController {

    @Autowired
    private CockpitUserMapper cockpitUserMapper;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public CockpitUser register(@RequestBody CockpitUser cockpitUser) {


        cockpitUserMapper.insert(cockpitUser);

        return cockpitUser;
    }

}
