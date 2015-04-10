package com.ndpmedia.rocketmq.stalker.executor;

import java.util.List;

/**
 * Created by robert.xu on 2015/4/9.
 */
public class ExecutorMonitor extends Thread{

    private List<FileMonitor> fileMonitors;

    @Override
    public void run() {
        while(true){
            System.out.println(" executor monitor wake up .");

            if (null == fileMonitors || fileMonitors.isEmpty()) {
                System.out.println(" no file monitor is running .");

                continue;
            }

            for (FileMonitor fileMonitor:fileMonitors){
                if(fileMonitor.isAlive())
                    continue;
                System.out.println(" file monitor " + fileMonitor.getPath() + " is stopped. try to restart .");
                FileMonitor file2 = new FileMonitor(fileMonitor.getPath());
                fileMonitors.remove(fileMonitor);
                file2.start();
                fileMonitors.add(file2);
            }



            try {
                sleep(30 * 1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public List<FileMonitor> getFileMonitors() {
        return fileMonitors;
    }

    public void setFileMonitors(List<FileMonitor> fileMonitors) {
        this.fileMonitors = fileMonitors;
    }
}
