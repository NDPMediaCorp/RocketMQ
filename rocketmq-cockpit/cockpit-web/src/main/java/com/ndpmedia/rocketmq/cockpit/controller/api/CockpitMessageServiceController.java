package com.ndpmedia.rocketmq.cockpit.controller.api;

import com.alibaba.rocketmq.client.QueryResult;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.alibaba.rocketmq.tools.admin.DefaultMQAdminExt;
import com.ndpmedia.rocketmq.cockpit.model.CockpitMessage;
import com.ndpmedia.rocketmq.cockpit.util.MessageTranslate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert.xu on 2015/4/7.
 */
@Controller
@RequestMapping(value = "/api/message")
public class CockpitMessageServiceController {

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CockpitMessage getMessageByID(@PathVariable("id") String id) {
        DefaultMQAdminExt defaultMQAdminExt = new DefaultMQAdminExt();
        try {
            defaultMQAdminExt.start();
            MessageExt messageExt = defaultMQAdminExt.viewMessage(id);
            return MessageTranslate.translateFrom(messageExt);
        }
        catch (MQClientException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (RemotingException e) {
            e.printStackTrace();
        }
        catch (MQBrokerException e) {
            e.printStackTrace();
        }
        finally {
            defaultMQAdminExt.shutdown();
        }
        return null;
    }

    @RequestMapping(value = "/{topic}/{key}", method = RequestMethod.GET)
    @ResponseBody
    public List<CockpitMessage> getMessageByKey(@PathVariable("topic") String topic, @PathVariable("key") String key) {
        DefaultMQAdminExt defaultMQAdminExt = new DefaultMQAdminExt();
        try {
            defaultMQAdminExt.start();
            QueryResult queryResult = defaultMQAdminExt.queryMessage(topic, key, 32, 0, Long.MAX_VALUE);
            List<CockpitMessage> result = new ArrayList<CockpitMessage>();
            for (MessageExt messageExt:queryResult.getMessageList()){
                result.add(MessageTranslate.translateFrom(messageExt));
            }
            return result;
        }
        catch (MQClientException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            defaultMQAdminExt.shutdown();
        }
        return null;
    }
}
