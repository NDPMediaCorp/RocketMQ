package com.ndpmedia.rocketmq.stalker.executor;

import com.ndpmedia.rocketmq.stalker.config.DataConfig;
import com.ndpmedia.rocketmq.stalker.file.*;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.Date;


/**
 * Created by robert.xu on 2015/4/9.
 * file monitor thread
 */
public class FileMonitor extends Thread {

    private String path = "";

    private String name = "";

    private long index = 0;

    private RandomAccessFile indexRAF;

    private MappedByteBuffer mbb;

    private TailFile tailFile;

    public FileMonitor(String filePath) {
        this.path = filePath;
        tailFile = new TailFile(filePath);
        this.name = filePath.substring(filePath.lastIndexOf("/"));
        makeIndex();
    }

    @Override
    public void run() {
        int retryTime = 0;

        while (true) {
            index = mbb.getLong(0);

            String line = tailFile.makeFile(index);

            if (null == line) {
                waitFileChange(2 * 1000);
            } else if (!line.contains(DataConfig.getProperties().getProperty("log.type"))) {
                //不匹配格式暂不设定处理内容
            } else {
                FileFormat fileFormat = FileFormatFactory.getFileFormat(0);
                //符合校验标准，却校验失败，将重返执行校验流程，再次校验该内容。重试5次后放弃。
                if (!fileFormat.check(line) && retryTime++ < 5) {
                    continue;
                }
            }

            index = tailFile.getLastIndex();
            retryTime = 0;
            mbb.putLong(0, index);
        }
    }

    private void makeIndex() {

        File indexFile = new File(DataConfig.getDataPath() + name + ".index");
        try {
            indexRAF = new RandomAccessFile(indexFile, "rw");
            if (indexFile.length() == 0)
                indexRAF.writeLong(0L);

            mbb = indexRAF.getChannel().map(MapMode.READ_WRITE, 0, 16);
        } catch (FileNotFoundException e) {
            System.out.println(new Date().toString() + " read index save failed. index file not found.");
        } catch (IOException e) {
            System.out.println(new Date().toString() + " read index IP failed.");
        }
    }

    private void waitFileChange(long millisecond) {
        try {
            sleep(millisecond);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
