package com.gg.applog.biz.domain;

import scala.Serializable;

/**
 * @author daigouwei
 */
public class LogAppParserDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userPkId;
    private String cpid;
    private String time;
    private String Ip;
    private String prodId;
    private String catCode;
    private String keyword;

    public LogAppParserDetail() {
    }

    public String getUserPkId() {
        return userPkId;
    }

    public void setUserPkId(String userPkId) {
        this.userPkId = userPkId;
    }

    public String getCpid() {
        return cpid;
    }

    public void setCpid(String cpid) {
        this.cpid = cpid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getCatCode() {
        return catCode;
    }

    public void setCatCode(String catCode) {
        this.catCode = catCode;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "LogAppParserDetail{" + "userPkId='" + userPkId + '\'' + ", cpid='" + cpid + '\'' + ", time='" + time
            + '\'' + ", Ip='" + Ip + '\'' + ", prodId='" + prodId + '\'' + ", catCode='" + catCode + '\''
            + ", keyword='" + keyword + '\'' + '}';
    }
}
