package com.ndpmedia.rocketmq.stalker;

import com.ndpmedia.rocketmq.stalker.executor.ExecutorMonitor;
import com.ndpmedia.rocketmq.stalker.executor.FileMonitor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert.xu on 2015/4/9.
 */
public class StalkerServer {

    public static void main(String[] args){
        init(args);
    }

    public static void init(String[] paths){
        //初始化配置，拉起整体服务，整体服务内部包含：1 日志扫描线程组 2 日志扫描线程组监控

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:applicationContext.xml");

        List<FileMonitor> fileMonitors = new ArrayList<FileMonitor>();

        for (String path:paths){
            FileMonitor fileMonitor = new FileMonitor(path);
            fileMonitor.start();

            fileMonitors.add(fileMonitor);
        }

        ExecutorMonitor executorMonitor = new ExecutorMonitor();
        executorMonitor.setFileMonitors(fileMonitors);
        executorMonitor.start();
    }
}
