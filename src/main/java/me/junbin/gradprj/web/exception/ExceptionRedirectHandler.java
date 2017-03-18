package me.junbin.gradprj.web.exception;

import me.junbin.gradprj.exception.ServiceException;
import me.junbin.gradprj.util.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/25 13:21
 * @description :
 */
@ControllerAdvice
public class ExceptionRedirectHandler {

    private static final Logger log = LoggerFactory.getLogger(ExceptionRedirectHandler.class);

    @ExceptionHandler(ServiceException.class)
    public ModelAndView handleServiceException(ServiceException e) {
        log.warn("服务层发生异常", e);
        ModelAndView mv = new ModelAndView();
        mv.setView(new RedirectView("/error/50x", true, false, false));
        mv.addObject(Global.ERROR_50X_KEY, e);
        return mv;
    }

    /**
     * 通过 {@link RedirectView} 进行路径解析的时候主要是通过调用 {@link RedirectView#createTargetUrl(Map, HttpServletRequest)}
     * 方法解析的。
     * <p>
     * {@link RedirectView#contextRelative} 属性：当前重定向路径是否基于 servletContext 进行解析，默认为 {@code false}，
     * 如果值为 {@code true} 并且当前的重定向路径是以 {@code /} 开头的话，那么在路径前添加 ${contextPath}；
     * {@link RedirectView#http10Compatible} 属性：是否兼容 Http 1.0，默认为 {@code true}
     * {@link RedirectView#exposeModelAttributes} 属性：是否暴露 Model 中的属性到 url 的请求参数，默认为 {@link true}
     */
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(RuntimeException e) {
        log.warn("系统发生异常", e);
        ModelAndView mv = new ModelAndView();
        // 以 POST 方法发起重定向请求
        mv.setView(new RedirectView("/error/50x", true, false, false));
        mv.addObject(Global.ERROR_50X_KEY, e);
        return mv;
    }

}
