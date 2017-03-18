package me.junbin.gradprj.util;

import me.junbin.commons.util.Args;
import me.junbin.gradprj.listener.WebAppListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/22 21:10
 * @description :
 */
public abstract class WebAppCtxHolder {

    private static ApplicationContext appCtx;

    public static void setAppCtx(ApplicationContext appCtx) {
        // 获取调用 setAppCtx(ApplicationContext) 方法的类名
        String callerClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        // 只有当方法是由 WebAppListener 类调用的时候才执行注入
        if (StringUtils.equals(callerClassName, WebAppListener.class.getName())) {
            Args.notNull(appCtx);
            WebAppCtxHolder.appCtx = appCtx;
        }
    }

/*
    public static Environment getEnvironment() {
        return appCtx.getEnvironment();
    }
*/

    public static ApplicationContext getAppCtx() {
        return appCtx;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return appCtx.getBean(beanClass);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return appCtx.getBean(name, requiredType);
    }

    public static boolean containsBean(String name) {
        return appCtx.containsBean(name);
    }

    public static boolean isSingleton(String name) {
        return appCtx.isSingleton(name);
    }

    public static boolean isPrototype(String name) {
        return appCtx.isPrototype(name);
    }

}
