package me.junbin.gradprj.web;

import me.junbin.commons.util.Args;
import me.junbin.gradprj.domain.Account;
import me.junbin.gradprj.util.Global;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/1/31 18:36
 * @description : 这个类只能由 {@link Controller}
 * 或者 {@link ControllerAdvice} 注解标注
 * 的类可以继承并使用。这是因为内部的一些方法存在限制，只能在 Web 环境下生效。
 * 这些方法都是从 {@link me.junbin.commons.web.WebUtils} 中截取出来的，更多有用的方法可
 * 以直接调用 {@link me.junbin.commons.web.WebUtils}
 */
public abstract class BaseController {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    // 数据验证，验证继承 BaseController 的类必须被 Controller 或者 ControllerAdvice 注解标注
    {
        Class<?> clazz = this.getClass();
        Controller controller = clazz.getDeclaredAnnotation(Controller.class);
        ControllerAdvice controllerAdvice = clazz.getDeclaredAnnotation(ControllerAdvice.class);
        if (controller == null && controllerAdvice == null) {
            log.error("{} 没有标注 Controller 或者 ControllerAdvice 注解！无法确保在 Web 环境时不可以继承 BaseController。", this.getClass().getName());
            throw new RuntimeException("继承于 BaseController 的类必须被 Controller 或者 ControllerAdvice 注解标注");
        }
    }

    @ModelAttribute
    public void populateAccountToModel(HttpSession session, Model model) {
        Object object = session.getAttribute(Global.LOGIN_ACCOUNT_KEY);
/*
        if (object == null) { // 尚未登陆或者不想登陆（以游客身份登陆）
            removeAllAttributes(session);
            Account visitor = loginAsVisitor();
            // 将值传入到 Session 中
            session.setAttribute(Global.LOGIN_ACCOUNT_KEY, visitor);
            object = visitor;
        }
*/
        model.addAttribute(Global.LOGIN_ACCOUNT_KEY, object);
    }

    protected Account loginAsVisitor() {
        Account visitor = Global.getVisitorAccount();
        UsernamePasswordToken token =
                new UsernamePasswordToken(visitor.getPrincipal(), visitor.getPassword());
        Subject subject = SecurityUtils.getSubject();
        subject.login(token); // 在这里该方法不可能抛出异常
        return visitor;
    }

    protected HttpSession removeAllAttributes(HttpSession session) {
        return removeAllBut(session, null);
    }

    protected HttpSession removeAllBut(HttpSession session, String notRemoveKey) {
        Args.notNull(session);
        Enumeration<String> attrNames = session.getAttributeNames();
        String attrName;
        while (attrNames.hasMoreElements()) {
            attrName = attrNames.nextElement();
            if (StringUtils.equals(attrName, notRemoveKey)) {
                continue;
            }
            session.removeAttribute(attrName);
        }
        return session;
    }

    protected HttpSession removeAllBut(HttpSession session, String firstNotRemove, String... moreNotRemoves) {
        Args.notNull(session);
        Set<String> key = new HashSet<>(moreNotRemoves.length);
        if (moreNotRemoves.length > 0) {
            key.addAll(Arrays.asList(moreNotRemoves));
        }
        key.add(firstNotRemove);
        key.remove(null);
        Enumeration<String> attrNames = session.getAttributeNames();
        String attrName;
        while (attrNames.hasMoreElements()) {
            attrName = attrNames.nextElement();
            if (key.contains(attrName)) {
                continue;
            }
            session.removeAttribute(attrName);
        }
        return session;
    }

    /**
     * 获取当前线程所属的 {@link WebApplicationContext}，此方法需要当前线程拥有 Spring Web
     * 应用程序的 {@link ClassLoader}（即必须在 Spring Web 环境下才能获取得到）。
     *
     * @return 如果当前线程并不属于 Spring Web 应用程序，那么返回 {@code null}；否则返回
     * 当前 Web 应用程序的 {@link WebApplicationContext}
     * 。
     */
    protected WebApplicationContext currentWebAppCtx() {
        return ContextLoader.getCurrentWebApplicationContext();
    }

    /**
     * 基于 Bean 的类型来检索 Spring Bean
     *
     * @param requiredType Bean 的类型
     * @param <T>          泛型
     * @return 如果 Spring 容器中存在唯一一个指定类型的 Bean，那么返回该 Bean，否则抛出异常
     */
    protected <T> T getBean(Class<T> requiredType) {
        WebApplicationContext wac = currentWebAppCtx();
        if (wac == null) {
            throw new IllegalStateException();
        }
        return wac.getBean(requiredType);
    }

    /**
     * 基于 Bean 的名称进行检索，同时限定 Bean 的类型，如果检索不到 Bean 或者检索到的 Bean
     * 的类型不匹配则抛出异常
     *
     * @param name         Bean 的名称
     * @param requiredType Bean 的类型
     * @param <T>          泛型
     * @return 如果 Spring 容器中存在指定名称的 Bean 并且 Bean 类型与指定的类型匹配，那么返
     * 回该 Bean，否则抛出异常
     */
    protected <T> T getBean(String name, Class<T> requiredType) {
        WebApplicationContext wac = currentWebAppCtx();
        if (wac == null) {
            throw new IllegalArgumentException();
        }
        return wac.getBean(name, requiredType);
    }

    /**
     * 获取当前线程所属的 {@link ServletContext}，此方法需要当前线程在 Spring Web 环境下才能生效。
     *
     * @return 如果当前线程不在 Spring Web 环境中，那么返回 {@code null}，否则返回当前 Web 应用程序
     * 的 {@link ServletContext}
     */
    protected ServletContext currentServletContext() {
        WebApplicationContext webAppCtx = currentWebAppCtx();
        return null == webAppCtx ? null : webAppCtx.getServletContext();
    }

    /**
     * 获取当前线程所属应用的 webapp 的根目录，此方法需要当前线程在 Spring Web 环境下才能生效。
     *
     * @return 当前应用的 webapp 根目录的绝对路径
     * @throws IllegalArgumentException 当前线程不属于 Spring Web 应用程序
     */
    protected Path getWebRootPath() {
        return getWebRootPath(currentServletContext());
    }

    /**
     * 获取指定 {@link ServletContext} 的 webapp 根目录的绝对路径
     *
     * @param servletContext 指定 Web 应用的 {@link ServletContext}
     * @return 指定 {@link ServletContext} 的 webapp 根目录的绝对路径
     * @throws IllegalArgumentException {@code ServletContext} 为 {@code null}，或者无法获取 Web 应用根目录
     */
    protected Path getWebRootPath(ServletContext servletContext) {
        Args.notNull(servletContext, "ServletContext must not be null");
        String pathString = servletContext.getRealPath("/");
        Path path = Paths.get(pathString).normalize();
        Args.check(Files.exists(path), "Web app root is not available /*accessible*/");
        return path;
    }

}
