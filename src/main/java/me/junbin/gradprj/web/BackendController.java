package me.junbin.gradprj.web;

import com.google.gson.JsonObject;
import me.junbin.commons.page.PageRequest;
import me.junbin.gradprj.domain.Account;
import me.junbin.gradprj.enumeration.DocumentType;
import me.junbin.gradprj.enumeration.MyGsonor;
import me.junbin.gradprj.service.AccountService;
import me.junbin.gradprj.service.DocumentService;
import me.junbin.gradprj.service.PermService;
import me.junbin.gradprj.service.RoleService;
import me.junbin.gradprj.util.Global;
import me.junbin.gradprj.util.HighChartsUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/24 20:34
 * @description :
 */
@Controller
@RequestMapping(value = "/backend")
public class BackendController extends BasePoController {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermService permService;

    @RequestMapping(value = {"/index", "/manage"}, method = RequestMethod.GET)
    @RequiresPermissions(value = "manage:*:backend")
    public String index() {
        return "backend/index";
    }

    @RequestMapping(value = {"/main"}, method = RequestMethod.GET)
    @RequiresPermissions(value = "manage:*:backend")
    public String mainBoard(@ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account account, Model model) {
        Map<DocumentType, Integer> statistics = documentService.statistics();
        model.addAttribute("pieData", HighChartsUtils.constructPieData(statistics));
        model.addAttribute("columnData", HighChartsUtils.constructColumnData(statistics));
        return "backend/main";
    }

    @RequestMapping(value = "/user/manage", method = RequestMethod.GET)
    @RequiresPermissions(value = "manage:*:user")
    public String userManage(@ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account account, Model model) {
        model.addAttribute("page", accountService.page(null, new PageRequest(0, Global.DEFAULT_PAGE_SIZE)));
        return "backend/manageAccount";
    }

    @RequestMapping(value = "/role/manage", method = RequestMethod.GET)
    @RequiresPermissions(value = "manage:*:role")
    public String roleManage(@ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account account, Model model) {
        model.addAttribute("page", roleService.page(null, new PageRequest(0, Global.DEFAULT_PAGE_SIZE)));
        return "backend/manageRole";
    }

    @RequestMapping(value = {"/category/manage", "/perm/manage"}, method = RequestMethod.GET)
    @RequiresPermissions(value = "manage:*:perm")
    public String categoryManage(@ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account account, Model model) {
        List<JsonObject> tree = Global.getCategoryTree();
        model.addAttribute("tree", tree);
        return "backend/manageCategory";
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public String info(@ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account account) {
        return MyGsonor.SN_PRETTY.toJson(account);
    }

}
