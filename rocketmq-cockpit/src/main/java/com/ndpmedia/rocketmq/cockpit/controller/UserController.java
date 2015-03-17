package com.ndpmedia.rocketmq.cockpit.controller;

import com.ndpmedia.rocketmq.cockpit.model.CockpitRole;
import com.ndpmedia.rocketmq.cockpit.model.CockpitUser;
import com.ndpmedia.rocketmq.cockpit.model.Status;
import com.ndpmedia.rocketmq.cockpit.model.Team;
import com.ndpmedia.rocketmq.cockpit.mybatis.mapper.CockpitRoleMapper;
import com.ndpmedia.rocketmq.cockpit.mybatis.mapper.CockpitUserMapper;
import com.ndpmedia.rocketmq.cockpit.mybatis.mapper.TeamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register() {
        ModelAndView modelAndView = new ModelAndView("user/register");
        List<Team> teamList = teamMapper.list();
        modelAndView.addObject("teamList", teamList);

        List<CockpitRole> roleList = cockpitRoleMapper.list();
        modelAndView.addObject("roleList", roleList);

        return modelAndView;
    }


    /**
     * TODO make this method transactional.
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CockpitUser register(HttpServletRequest request) {
        CockpitUser cockpitUser = new CockpitUser();
        cockpitUser.setStatus(Status.DRAFT);
        cockpitUser.setEmail(request.getParameter("email"));
        cockpitUser.setUsername(request.getParameter("userName"));
        cockpitUser.setPassword(passwordEncoder.encode(request.getParameter("password")));
        Team team = new Team();
        team.setId(Long.parseLong(request.getParameter("teamId")));
        cockpitUser.setTeam(team);
        cockpitUserMapper.insert(cockpitUser);

        teamMapper.addMember(team.getId(), cockpitUser.getId());

        return cockpitUser;
    }


    @RequestMapping(value = "/activate/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CockpitUser activate(@PathVariable("id") long id) {
        cockpitUserMapper.activate(id);
        return cockpitUserMapper.get(id, null);
    }

}
