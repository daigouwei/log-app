package com.gg.applog.biz.service;

import com.gg.applog.biz.service.logspark.LogSparkDriverImpl;
import com.gg.applog.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Service;

/**
 * @author daigouwei
 * @date 2018/11/7
 */
@Service
public class LogAppInit extends BaseService implements SmartLifecycle {
    @Autowired
    private LogSparkDriverImpl logSparkDriverImpl;

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable runnable) {

    }

    @Override
    public void start() {
        LOG.info("Spring Bean全部加载完毕，开始运行主任务...");
        logSparkDriverImpl.handleAppLog();
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public int getPhase() {
        return 0;
    }
}
