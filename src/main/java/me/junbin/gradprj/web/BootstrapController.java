package me.junbin.gradprj.web;

import me.junbin.commons.util.Args;
import me.junbin.gradprj.domain.Account;
import me.junbin.gradprj.service.CaptchaService;
import me.junbin.gradprj.util.EncryptUtils;
import me.junbin.gradprj.util.Global;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/23 20:22
 * @description :
 */
@Controller
@RequestMapping(value = "/")
public class BootstrapController extends BaseController {


    @Autowired
    private CaptchaService textKaptchaService;

    // 尚未以游客等任何身份登陆时
    @RequiresGuest
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model,
                        @ModelAttribute(Global.LOGIN_ERROR_KEY) String errorMsg,
                        @ModelAttribute(Global.PRINCIPAL) String principal) {
        model.addAttribute(Global.LOGIN_ERROR_KEY, errorMsg);
        model.addAttribute(Global.PRINCIPAL, principal);
        return "login2";
    }

    // 当且仅当由游客身份切换登陆时
    @RequiresRoles(value = {Global.VISITOR_NAME})
    @RequestMapping(value = "/auth/login", method = RequestMethod.GET)
    public String login(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @RequiresAuthentication
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @RequiresGuest
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpSession session, RedirectAttributes redirectAttributes,
                        boolean loginAsVisitor, String principal, String password,
                        String kaptcha) {
        Subject subject = SecurityUtils.getSubject();
        if (loginAsVisitor) {
            this.loginAsVisitor();
            session.setAttribute(Global.LOGIN_ACCOUNT_KEY, Global.getVisitorAccount());
            session.removeAttribute(Global.ERROR_TIME_KEY);
            return "redirect:/index";
        } else {
            Integer errorTime = (Integer) session.getAttribute(Global.ERROR_TIME_KEY);
            if (errorTime == null) {
                errorTime = 0;
            }

            // 验证码确认
            String sessionKaptcha = (String) session.getAttribute(Global.CAPTCHA_KEY);
            if (sessionKaptcha != null) {
                if (!StringUtils.equalsIgnoreCase(sessionKaptcha, kaptcha)) {
                    errorTime++;
                    log.warn("登陆失败 --> 验证码错误！错误次数：{}", errorTime);
                    session.setAttribute(Global.ERROR_TIME_KEY, errorTime);
                    redirectAttributes.addFlashAttribute(Global.PRINCIPAL, principal);
                    redirectAttributes.addFlashAttribute(Global.LOGIN_ERROR_KEY, Global.ERROR_TIP_CAPTCHA);
                    return "redirect:/login";
                } else {
                    session.removeAttribute(Global.CAPTCHA_KEY);
                }
            }
            Args.notNull(principal);
            Args.notNull(password);
            password = EncryptUtils.md5Encrypt(password, principal);
            try {
                subject.login(new UsernamePasswordToken(principal, password));
            } catch (IncorrectCredentialsException e) {
                errorTime++;
                log.warn("账户{}登陆失败 --> 密码错误！错误次数：{}", principal, errorTime);
                session.setAttribute(Global.ERROR_TIME_KEY, errorTime);
                redirectAttributes.addFlashAttribute(Global.PRINCIPAL, principal);
                redirectAttributes.addFlashAttribute(Global.LOGIN_ERROR_KEY, Global.ERROR_TIP_PRINCIPAL);
                return "redirect:/login";
            } catch (UnknownAccountException e) {
                errorTime++;
                log.warn("登陆失败 --> 账户不存在！错误次数：{}", errorTime);
                session.setAttribute(Global.ERROR_TIME_KEY, errorTime);
                redirectAttributes.addFlashAttribute(Global.PRINCIPAL, principal);
                redirectAttributes.addFlashAttribute(Global.LOGIN_ERROR_KEY, Global.ERROR_TIP_PRINCIPAL);
                return "redirect:/login";
            }
            Account account = (Account) subject.getPrincipal();
            session.removeAttribute(Global.ERROR_TIME_KEY);
            session.setAttribute(Global.LOGIN_ACCOUNT_KEY, account);
            return "redirect:/index";
        }
    }

/*
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpSession session, RedirectAttributes redirectAttributes,
                        String principal, String password, boolean loginAsVisitor) {
        Subject subject = SecurityUtils.getSubject();
        if (loginAsVisitor) {
            Account account = (Account) session.getAttribute(Global.LOGIN_ACCOUNT_KEY);
            if (account == null ||
                    !StringUtils.equals(account.getPrincipal(), Global.VISITOR_NAME)) {
                // 执行登出操作，切记不可以通过 HttpSession.invalidate() 方法登陆，否则当前 Session 失效
                session = WebUtils2.currentSession(true);
                // 以游客身份登陆
                loginAsVisitor();
                session.setAttribute(Global.LOGIN_ACCOUNT_KEY, Global.getVisitorAccount());
            }
        } else {
            Args.notNull(principal);
            Args.notNull(password);
            session.invalidate();
            session = WebUtils2.currentSession(true);
            password = EncryptUtils.md5Encrypt(principal, password);
            UsernamePasswordToken token = new UsernamePasswordToken(principal, password);
            Integer errorTime = (Integer) session.getAttribute(Global.ERROR_TIME_KEY);
            if (errorTime == null) {
                errorTime = 0;
            }
            try {
                subject.login(token);
            } catch (IncorrectCredentialsException e) {
                errorTime++;
                log.warn("账户{}登陆失败 --> 密码错误！错误次数：{}", principal, errorTime);
                session.setAttribute(Global.ERROR_TIME_KEY, errorTime);
                redirectAttributes.addFlashAttribute(Global.LOGIN_ERROR_KEY, Global.ERROR_TIP_PRINCIPAL);
                return "redirect:/login";
            } catch (UnknownAccountException e) {
                errorTime++;
                log.warn("登陆失败 --> 账户不存在！错误次数：{}", principal, errorTime);
                session.setAttribute(Global.ERROR_TIME_KEY, errorTime);
                redirectAttributes.addFlashAttribute(Global.LOGIN_ERROR_KEY, Global.ERROR_TIP_PRINCIPAL);
                return "redirect:/login";
            }
            Account account = (Account) subject.getPrincipal();
            session.setAttribute(Global.LOGIN_ACCOUNT_KEY, account);
        }
        return "redirect:/index";
    }
*/

    @RequiresAuthentication
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(@ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account account) {
        return "index";
    }

    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public ModelAndView generateCaptcha(HttpSession session, HttpServletResponse response) throws IOException {
        this.setNotCache(response);
        response.setContentType("image/jpeg");

        String captchaText = textKaptchaService.createText();

        session.setAttribute(Global.CAPTCHA_KEY, captchaText);

        BufferedImage bufferedImage = textKaptchaService.createImage(captchaText);
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            ImageIO.write(bufferedImage, "jpg", outputStream);
            outputStream.flush();
        }
        return null;
    }

    private void setNotCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
    }

}
