package com.ndpmedia.rocketmq.stalker.file;

/**
 * Created by robert.xu on 2015/4/9.
 * base interface for format
 */
public interface FileFormat {
    public boolean check(String log);
}
