package com.ndpmedia.rocketmq.stalker.file;

/**
 * Created by robert.xu on 2015/4/10.
 */
public class FileFormatFactory {

    /**
     * file analysis
     *
     * @param type
     * @return
     */
    public static FileFormat getFileFormat(int type) {
        switch (type) {
        case 1:
            return new LogFormat();
        case 2:
            return null;
        case 3:
            return null;
        default:
            return new LogFormat();
        }
    }
}
