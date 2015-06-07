/*
 * Copyright (c) 2015. All Rights Reserved.
 */
package com.alibaba.rocketmq.remoting.common;

import org.junit.Assert;
import org.junit.Test;

public class CloudUtilTest {

    @Test
    public void testExecuteCommand() throws Exception {
        Assert.assertNotNull(CloudUtil.executeCommand("date"));
    }

    @Test
    public void testExecuteCommand_CommandNotFound() throws Exception {
        Assert.assertNull(CloudUtil.executeCommand("abcdefg"));
    }

}