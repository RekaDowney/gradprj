package me.junbin.gradprj.util;

import me.junbin.commons.web.WebUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/7 21:33
 * @description :
 */
public class WebUtils2 extends WebUtils {

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

}
