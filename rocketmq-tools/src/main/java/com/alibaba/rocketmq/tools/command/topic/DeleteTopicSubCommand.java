/**
 * Copyright (C) 2010-2013 Alibaba Group Holding Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.rocketmq.tools.command.topic;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.remoting.RPCHook;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.alibaba.rocketmq.srvutil.ServerUtil;
import com.alibaba.rocketmq.tools.admin.DefaultMQAdminExt;
import com.alibaba.rocketmq.tools.command.CommandUtil;
import com.alibaba.rocketmq.tools.command.SubCommand;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * 删除Topic配置命令
 * 
 * @author manhong.yqd<manhong.yqd@alibaba-inc.com>
 * @since 2013-8-21
 */
public class DeleteTopicSubCommand implements SubCommand {
    @Override
    public String commandName() {
        return "deleteTopic";
    }


    @Override
    public String commandDesc() {
        return "Delete topic from broker and NameServer.";
    }


    /**
     * <p>
     *     This command requires presence of topic and (cluster or broker) name.
     *     If topic and clusterName are provided, all topic information is erased from both name server and brokers.
     *     If topic and brokerName are provided, topic information pertaining to given broker will be removed.
     *     If topic, clusterName and brokerName are all provided, clusterName will ONLY be used to verify that given
     *     broker has a master role.
     * </p>
     * @param options Command line options.
     * @return built command line options.
     */
    @Override
    public Options buildCommandlineOptions(Options options) {
        Option opt = new Option("t", "topic", true, "topic name");
        opt.setRequired(true);
        options.addOption(opt);

        opt = new Option("c", "clusterName", true, "delete topic from which cluster");
        opt.setRequired(false);
        options.addOption(opt);

        opt = new Option("b", "brokerAddress", true, "delete topic from specified broker");
        opt.setRequired(false);
        options.addOption(opt);

        return options;
    }


    public static void deleteTopic(final DefaultMQAdminExt adminExt,//
            final String clusterName,//
            final String brokerAddress,
            final String topic//
    ) throws InterruptedException, MQBrokerException, RemotingException, MQClientException {

        Set<String> masterSet = null;
        boolean deleteByCluster = false;
        if (null == brokerAddress && null != clusterName) {
            masterSet = CommandUtil.fetchMasterAddrByClusterName(adminExt, clusterName);
            deleteByCluster = true;
        } else if (null != brokerAddress) {
            masterSet = new HashSet<String>();
            masterSet.add(brokerAddress);
        }

        // 删除 broker 上的 topic 信息
        adminExt.deleteTopicInBroker(masterSet, topic);
        if (deleteByCluster) {
            System.out.printf("delete topic [%s] from cluster [%s] success.\n", topic, clusterName);
        } else {
            System.out.printf("delete topic [%s] from broker [%s] success.\n", topic, brokerAddress);
        }

        // 删除 NameServer 上的 topic 信息
        Set<String> nameServerSet = null;
        if (adminExt.getNamesrvAddr() != null) {
            String[] ns = adminExt.getNamesrvAddr().trim().split(";");
            nameServerSet = new HashSet(Arrays.asList(ns));
        }

        // 删除 NameServer 上的 topic 信息
        if (!deleteByCluster) {
            adminExt.deleteTopicInNameServer(nameServerSet, topic, masterSet);
        } else {
            adminExt.deleteTopicInNameServer(nameServerSet, topic, null);
        }
        System.out.printf("delete topic [%s] from NameServer success.\n", topic);
    }


    @Override
    public void execute(CommandLine commandLine, Options options, RPCHook rpcHook) {
        DefaultMQAdminExt adminExt = new DefaultMQAdminExt(rpcHook);
        adminExt.setInstanceName(Long.toString(System.currentTimeMillis()));
        try {
            String topic = commandLine.getOptionValue('t').trim();

            String clusterName = null;

            if (commandLine.hasOption('c')) {
                clusterName = commandLine.getOptionValue('c').trim();
            }

            String brokerName = null;
            if (commandLine.hasOption('b')) {
                brokerName = commandLine.getOptionValue('b').trim();
            }

            if (null != clusterName || null != brokerName) {
                adminExt.start();
                deleteTopic(adminExt, clusterName, brokerName, topic);
                return;

            }

            ServerUtil.printCommandLineHelp("mqadmin " + this.commandName(), options);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            adminExt.shutdown();
        }
    }
}
