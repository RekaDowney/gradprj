package me.junbin.gradprj.web;

import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;
import com.zhuozhengsoft.pageoffice.PageOfficeLink;
import me.junbin.gradprj.domain.Account;
import me.junbin.gradprj.enumeration.DocumentType;
import me.junbin.gradprj.enumeration.MyGsonor;
import me.junbin.gradprj.service.AccountService;
import me.junbin.gradprj.service.RoleService;
import me.junbin.gradprj.util.DocumentUtils;
import me.junbin.gradprj.util.Global;
import me.junbin.gradprj.util.WebAppCtxHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/7 22:06
 * @description :
 */
@Controller
@RequestMapping("/test")
public class TestController extends BasePoController {

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

    @RequestMapping(value = "/write", method = RequestMethod.GET)
    public String write(Model model, HttpServletRequest request) {
        model.addAttribute("doc_u1", PageOfficeLink.openWindow(request, "/test/open/u1", "width:100%;height:100%;"));
        model.addAttribute("doc_u2", PageOfficeLink.openWindow(request, "/test/open/u2", "width:100%;height:100%;"));
        return "test/write";
    }

    @RequestMapping(value = "/open/{user:\\w+}", method = RequestMethod.GET)
    public String open(@PathVariable("user") String user, HttpServletRequest request) {
        String url = DocumentUtils.getActualPathUri("5e046c80de714d5aa1e9c2ab6d76dcad.docx");
        PageOfficeCtrl ctrl = this.create(request);
        ctrl.setCaption("文档X"); // 设置控件标题栏
        ctrl.addCustomToolButton("切换全屏", "fullScreenSwitch", 4);

        ctrl.setMenubar(false);
        ctrl.setTimeSlice(2);

        ctrl.webOpen(url, DocumentType.WORD.normalEdit(), user);
        ctrl.setTagId("docCtrl");

        return "test/popupEdit";
    }


}
