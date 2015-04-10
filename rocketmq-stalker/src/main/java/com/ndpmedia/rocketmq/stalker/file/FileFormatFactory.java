package com.ndpmedia.rocketmq.stalker.file;

/**
 * Created by robert.xu on 2015/4/10.
 */
public class FileFormatFactory {
    public static FileFormat getFileFormat(int type) {
        switch (type) {
        case 1:
            return new LogFormat();
        default:
            return new LogFormat();
        }
    }
}
