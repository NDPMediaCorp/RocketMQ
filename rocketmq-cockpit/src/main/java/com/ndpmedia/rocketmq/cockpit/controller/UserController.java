package com.ndpmedia.rocketmq.cockpit.controller;

import com.ndpmedia.rocketmq.cockpit.model.CockpitRole;
import com.ndpmedia.rocketmq.cockpit.model.CockpitUser;
import com.ndpmedia.rocketmq.cockpit.model.Status;
import com.ndpmedia.rocketmq.cockpit.model.Team;
import com.ndpmedia.rocketmq.cockpit.mybatis.mapper.CockpitRoleMapper;
import com.ndpmedia.rocketmq.cockpit.mybatis.mapper.CockpitUserMapper;
import com.ndpmedia.rocketmq.cockpit.mybatis.mapper.TeamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping
public class UserController {

    @Autowired
    private CockpitUserMapper cockpitUserMapper;

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private CockpitRoleMapper cockpitRoleMapper;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register() {
        ModelAndView modelAndView = new ModelAndView("user/register");
        List<Team> teamList = teamMapper.list();
        modelAndView.addObject("teamList", teamList);

        List<CockpitRole> roleList = cockpitRoleMapper.list();
        modelAndView.addObject("roleList", roleList);

        return modelAndView;
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CockpitUser register(@RequestBody CockpitUser cockpitUser) {
        cockpitUser.setStatus(Status.DRAFT);
        cockpitUserMapper.insert(cockpitUser);
        cockpitUser.setPassword(null);
        return cockpitUser;
    }


    @RequestMapping(value = "/activate/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CockpitUser activate(@PathVariable("id") long id) {
        cockpitUserMapper.activate(id);
        return cockpitUserMapper.get(id, null);
    }

}
