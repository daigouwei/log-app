package com.gg.applog.util;

import org.springframework.context.ApplicationContext;

/**
 * @author daigouwei
 * @date 2018/11/7
 */
public abstract class SpringContainerHolder {
    public static ApplicationContext applicationContext;

    public static <T> T lookup(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static Object lookup(String name) {
        return applicationContext.getBean(name);
    }
}
