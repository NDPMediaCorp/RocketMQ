package com.ndpmedia.rocketmq.stalker.file;

import java.io.*;

/**
 * Created by robert.xu on 2015/4/9.
 */
public class TailFile {

    private String path;

    private RandomAccessFile randomAccessFile;

    private long lastIndex = 0L;

    public TailFile(String path){
        this.path = path;
    }

    public String makeFile(long index) {
        File file = new File(path);

        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            randomAccessFile.seek(index);
            String temp = randomAccessFile.readLine();
            lastIndex = randomAccessFile.getFilePointer();
            return temp;

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                randomAccessFile.close();
            }
            catch (IOException e) {
                randomAccessFile = null;
            }
        }

        return null;
    }

    public long getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(long lastIndex) {
        this.lastIndex = lastIndex;
    }
}
