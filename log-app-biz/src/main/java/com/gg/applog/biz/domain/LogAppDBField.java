package com.gg.applog.biz.domain;

/**
 * @author daigouwei
 * @date 2018/11/7
 */
public class LogAppDBField {
    private String appId;
    private String userId;
    private String profile;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "LogAppDBField{" + "appId='" + appId + '\'' + ", userId='" + userId + '\'' + ", profile='" + profile
                + '\'' + '}';
    }
}
