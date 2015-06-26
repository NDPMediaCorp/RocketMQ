/*
 * Copyright (c) 2015. All Rights Reserved.
 */
package com.alibaba.rocketmq.client.impl;

public class FindConsumeOffsetResult {
    private long offset;
    private boolean fromMaster;

    public FindConsumeOffsetResult(long offset, boolean fromMaster) {
        this.offset = offset;
        this.fromMaster = fromMaster;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public boolean isFromMaster() {
        return fromMaster;
    }

    public void setFromMaster(boolean fromMaster) {
        this.fromMaster = fromMaster;
    }
}
