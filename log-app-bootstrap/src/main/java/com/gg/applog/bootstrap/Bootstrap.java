package com.gg.applog.bootstrap;

import com.gg.applog.util.SpringContainerHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * @author daigouwei
 * @date 2018/11/5
 */
public class Bootstrap {
    private static final Logger LOG = LoggerFactory.getLogger(Bootstrap.class);

    public static void main(String[] args) {
        GenericXmlApplicationContext genericXmlApplicationContext = new GenericXmlApplicationContext();
        try {
            LOG.info("log-app bootstrapping...");
            genericXmlApplicationContext.load("applicationContext.xml");
            genericXmlApplicationContext.refresh();
            SpringContainerHolder.applicationContext = genericXmlApplicationContext;
            LOG.info("log-app finish bootstrap.");
        }
        catch (BeansException | IllegalStateException e) {
            genericXmlApplicationContext.close();
            LOG.error("fail bootstrap log-app!!!", e);
            System.exit(1);
        }
    }
}
