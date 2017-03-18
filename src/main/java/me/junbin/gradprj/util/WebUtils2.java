package me.junbin.gradprj.util;

import me.junbin.commons.web.WebUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/7 21:33
 * @description : 该工具类建议只在 Web 环境下使用
 */
public abstract class WebUtils2 extends WebUtils {

    private static ServletRequestAttributes threadContext() {
        return ServletRequestAttributes.class.cast(
                RequestContextHolder.getRequestAttributes()
        );
    }

    public static HttpServletRequest currentRequest() {
        return threadContext().getRequest();
    }

    public static HttpServletResponse currentResponse() {
        return threadContext().getResponse();
    }

    public static HttpSession currentSession() {
        return currentRequest().getSession();
    }

    public static HttpSession currentSession(boolean autoCreate) {
        return currentRequest().getSession(autoCreate);
    }

    public static String currentSessionId() {
        return threadContext().getSessionId();
    }

}
