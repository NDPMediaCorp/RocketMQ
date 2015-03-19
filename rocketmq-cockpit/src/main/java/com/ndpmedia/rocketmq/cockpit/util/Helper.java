package com.ndpmedia.rocketmq.cockpit.util;

import com.ndpmedia.rocketmq.cockpit.model.CockpitRole;
import com.ndpmedia.rocketmq.cockpit.model.CockpitUser;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class Helper {

    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    public static String getInstanceName() {
        String localHostAddress;
        try {
            localHostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            localHostAddress = "127.0.0.1";
        }

        return localHostAddress + "@" + Thread.currentThread().getId() + "_" + COUNTER.incrementAndGet();
    }

    public static boolean hasRole(HttpServletRequest request, CockpitRole cockpitRole) {
        CockpitUser cockpitUser = (CockpitUser) request.getSession().getAttribute(LoginConstant.COCKPIT_USER_KEY);
        if (null == cockpitUser) {
            return false;
        }

        List<CockpitRole> roles = cockpitUser.getCockpitRoles();

        return null != roles && roles.contains(cockpitRole);

    }
}
