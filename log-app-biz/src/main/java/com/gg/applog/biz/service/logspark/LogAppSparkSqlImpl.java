package com.gg.applog.biz.service.logspark;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * @author guowei
 * @date 2018/11/8
 */
@Service
public class LogAppSparkSqlImpl {
    @Value ("${mysql.driver}")
    private String driver;
    @Value ("${mysql.url}")
    private String url;
    @Value ("${mysql.username}")
    private String username;
    @Value ("${mysql.password")
    private String password;
    @Value ("${mysql.table}")
    private String table;
    private Properties connProperties;

    @PostConstruct
    public void init() {
        Properties properties = new Properties();
        properties.put("user", this.username);
        properties.put("password", this.password);
        properties.put("driver", this.driver);
        this.connProperties = properties;
    }

    public String getUrl() {
        return url;
    }

    public String getTable() {
        return table;
    }

    public Properties getConnProperties() {
        return connProperties;
    }
}
