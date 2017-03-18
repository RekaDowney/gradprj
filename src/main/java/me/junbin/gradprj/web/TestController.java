package me.junbin.gradprj.web;

import me.junbin.gradprj.domain.Account;
import me.junbin.gradprj.enumeration.MyGsonor;
import me.junbin.gradprj.service.AccountService;
import me.junbin.gradprj.service.RoleService;
import me.junbin.gradprj.util.Global;
import me.junbin.gradprj.util.WebAppCtxHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/7 22:06
 * @description :
 */
@Controller
@RequestMapping("/test")
public class TestController extends BaseController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/query/{principal:\\w+}", method = RequestMethod.GET)
    public String query(@PathVariable("principal") String principal, Model model) {
        Account account = accountService.selectByPrincipal(principal);
        model.addAttribute("account", account.toString());
        return "name";
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        RoleService roleService = WebAppCtxHolder.getBean(RoleService.class);
        return roleService == null ? "null" : roleService.toString();
    }

    @RequestMapping(value = "/default", method = RequestMethod.GET)
    @ResponseBody
    public String defaultAccount(@ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account loginAccount) {
        return MyGsonor.SN_PRETTY.toJson(loginAccount);
    }

    @RequestMapping(value = "/default/obj", method = RequestMethod.GET)
    @ResponseBody
    public Object defaultAccountObj(@ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account loginAccount) {
        return loginAccount;
    }

    @RequestMapping(value = "/function", method = RequestMethod.GET)
    public String testFunction(HttpSession session) {
        RuntimeException r = new RuntimeException("Test");
        session.setAttribute(Global.ERROR_50X_KEY, r);
        return "test";
    }


}
