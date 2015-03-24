package com.ndpmedia.rocketmq.cockpit.scheduler;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by robert on 2015/3/23.
 */
public class MonitorServer
{
    private ClassPathXmlApplicationContext applicationContext;

    public static void main(String[] args)
    {
        MonitorServer monitorServer = new MonitorServer();
        monitorServer.start();
    }

    public void start(){
        init();

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        long nextMinutes = 60 - (System.currentTimeMillis() / 1000 % 60);
        TaskScheduler taskScheduler = (TaskScheduler) applicationContext.getBean("taskScheduler");
        scheduledExecutorService.scheduleAtFixedRate(taskScheduler, nextMinutes, 5*60, TimeUnit.SECONDS);
    }

    //初始化bean
    private void init()
    {
        String _basePath = "xml/";
        applicationContext = new ClassPathXmlApplicationContext(
                new String[]{_basePath + "*.xml"});
    }
}
