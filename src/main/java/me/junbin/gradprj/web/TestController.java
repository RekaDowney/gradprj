package me.junbin.gradprj.web;

import me.junbin.gradprj.domain.Account;
import me.junbin.gradprj.service.AccountService;
import me.junbin.gradprj.util.EncryptUtils;
import me.junbin.gradprj.util.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDateTime;

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

    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public String insert() {
        Account account = new Account();
        account.setId(Global.uuid());
        account.setPrincipal("Reka");
        account.setPassword(EncryptUtils.md5Encrypt("123456", "Reka"));
        account.setCreatedTime(LocalDateTime.now());
        account.setLocked(false);
        account.setValid(true);
        accountService.insert(account);
        return "insertOk";
    }

    @RequestMapping(value = "/query/{principal:\\w+}", method = RequestMethod.GET)
    public String query(@PathVariable("principal") String principal, Model model) {
        Account account = accountService.selectByPrincipal(principal);
        model.addAttribute("account", account);
        return "name";
    }


}
