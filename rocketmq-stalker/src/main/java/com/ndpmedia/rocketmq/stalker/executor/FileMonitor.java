package com.ndpmedia.rocketmq.stalker.executor;

import com.ndpmedia.rocketmq.stalker.config.DataConfig;
import com.ndpmedia.rocketmq.stalker.file.*;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;


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
        while (true) {
            index = mbb.getLong(0);

            String line = tailFile.makeFile(index);

            if (null == line || !line.contains(DataConfig.getProperties().getProperty("log.type"))) {
                waitFileChange(2*1000);

            }else {
                System.out.println(line);
                FileFormat fileFormat = FileFormatFactory.getFileFormat(0);
                fileFormat.check(line);
            }

            index = tailFile.getLastIndex();

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
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitFileChange(long millisecond){
        try{
            sleep(millisecond);
        }catch (Exception e){
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
