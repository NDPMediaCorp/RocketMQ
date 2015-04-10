package com.ndpmedia.rocketmq.stalker.file;

/**
 * Created by robert.xu on 2015/4/9.
 */
public interface FileFormat {
    public boolean check(String log);
}
