package com.ndpmedia.rocketmq.cockpit.model;

import java.util.Date;

public class Login {
    private long id;

    private String sessionId;

    private Date loginTime;

    private CockpitUser cockpitUser;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public CockpitUser getCockpitUser() {
        return cockpitUser;
    }

    public void setCockpitUser(CockpitUser cockpitUser) {
        this.cockpitUser = cockpitUser;
    }
}
