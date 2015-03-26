package com.ndpmedia.rocketmq.cockpit.controller.api;

import com.ndpmedia.rocketmq.cockpit.model.CockpitRole;
import com.ndpmedia.rocketmq.cockpit.model.CockpitUser;
import com.ndpmedia.rocketmq.cockpit.model.ConsumerGroup;
import com.ndpmedia.rocketmq.cockpit.mybatis.mapper.ConsumerGroupMapper;
import com.ndpmedia.rocketmq.cockpit.service.CockpitConsumerGroupService;
import com.ndpmedia.rocketmq.cockpit.util.LoginConstant;
import com.ndpmedia.rocketmq.cockpit.util.WebHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/api/consumer-group")
public class ConsumerGroupServiceController {

    @Autowired
    private ConsumerGroupMapper consumerGroupMapper;

    @Autowired
    private CockpitConsumerGroupService cockpitConsumerGroupService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<ConsumerGroup> list(HttpServletRequest request) {
        return consumerGroupMapper.list(getTeamId(request), null, null);
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ConsumerGroup get(@PathVariable("id") long id) {
        return consumerGroupMapper.get(id);
    }

    @RequestMapping(value = "/cluster-name/{clusterName}", method = RequestMethod.GET)
    @ResponseBody
    public List<ConsumerGroup> listByClusterName(@PathVariable("clusterName") String clusterName,
                                                 HttpServletRequest request) {
        return consumerGroupMapper.list(getTeamId(request), clusterName, null);
    }

    @RequestMapping(value = "/consumer-group-name/{consumerGroupName}", method = RequestMethod.GET)
    @ResponseBody
    public ConsumerGroup getByConsumerGroupName(@PathVariable("consumerGroupName") String consumerGroupName,
                                                HttpServletRequest request) {
        return consumerGroupMapper.list(getTeamId(request), null, consumerGroupName).get(0);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ConsumerGroup add(@RequestBody ConsumerGroup consumerGroup, HttpServletRequest request) {
        CockpitUser cockpitUser = (CockpitUser)request.getSession().getAttribute(LoginConstant.COCKPIT_USER_KEY);
        long teamId = cockpitUser.getTeam().getId();
        cockpitConsumerGroupService.insert(consumerGroup, teamId);
        return consumerGroup;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void update(@RequestBody ConsumerGroup consumerGroup) {
        consumerGroup.setUpdateTime(new Date());
        consumerGroupMapper.update(consumerGroup);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @ResponseBody
    public void register(@PathVariable("id") long id){
        consumerGroupMapper.register(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable("id") long id) {
        cockpitConsumerGroupService.delete(id);
    }

    private long getTeamId(HttpServletRequest request) {
        long teamId = 0;
        if (!WebHelper.hasRole(request, CockpitRole.ROLE_ADMIN)) {
            CockpitUser cockpitUser = (CockpitUser)request.getSession().getAttribute(LoginConstant.COCKPIT_USER_KEY);
            teamId = cockpitUser.getTeam().getId();
        }
        return teamId;
    }

}
