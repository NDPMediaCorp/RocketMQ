package com.ndpmedia.rocketmq.cockpit.controller.api;

import com.ndpmedia.rocketmq.cockpit.model.ConsumerGroup;
import com.ndpmedia.rocketmq.cockpit.mybatis.mapper.ConsumerGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/api/consumer-group")
public class ConsumerGroupServiceController {

    @Autowired
    private ConsumerGroupMapper consumerGroupMapper;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<ConsumerGroup> list() {
        return consumerGroupMapper.list();
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ConsumerGroup get(@PathVariable("id") long id) {
        return consumerGroupMapper.get(id);
    }

    @RequestMapping(value = "/cluster-name/{clusterName}", method = RequestMethod.GET)
    @ResponseBody
    public List<ConsumerGroup> listByClusterName(@PathVariable("clusterName") String clusterName) {
        return consumerGroupMapper.listByClusterName(clusterName);
    }

    @RequestMapping(value = "/consumer-group-name/{consumerGroupName}", method = RequestMethod.GET)
    @ResponseBody
    public ConsumerGroup getByConsumerGroupName(@PathVariable("consumerGroupName") String consumerGroupName) {
        return consumerGroupMapper.getByGroupName(consumerGroupName);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ConsumerGroup add(@RequestBody ConsumerGroup consumerGroup) {
        consumerGroupMapper.insert(consumerGroup);
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
        consumerGroupMapper.delete(id);
    }

}
