package me.junbin.gradprj.web;

import com.google.gson.JsonObject;
import me.junbin.gradprj.domain.Account;
import me.junbin.gradprj.domain.Perm;
import me.junbin.gradprj.enumeration.PermType;
import me.junbin.gradprj.service.PermService;
import me.junbin.gradprj.util.AccountMgr;
import me.junbin.gradprj.util.Global;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/24 21:14
 * @description :
 */
@Controller
@RequestMapping(value = {"/perm", "/category"})
public class CategoryController extends BaseRestController {

    @Autowired
    private PermService permService;

    @RequestMapping(value = "/append", method = RequestMethod.GET)
    @RequiresPermissions(value = "manage:*:perm")
    public String append(@RequestParam(value = "parentId", required = false) String parentId, Model model) {
        model.addAttribute("parentId", parentId);
        return "backend/addCategory";
    }

    @RequestMapping(value = "/append", method = RequestMethod.POST)
    @RequiresPermissions(value = "manage:*:perm")
    @ResponseBody
    public Object append(@RequestParam(value = "parentId", required = false) String parentId,
                         @RequestParam String permName,
                         @RequestParam Integer weight,
                         @RequestParam Boolean attachable,
                         @ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account account) {
        Perm perm = new Perm();
        if (StringUtils.length(parentId) != 32) {
            perm.setParentId(null);
        } else {
            perm.setParentId(parentId);
        }
        perm.setId(Global.uuid());
        perm.setPermName(permName);
        perm.setWeight(weight);
        perm.setAttachable(attachable);
        // 必须添加上权限匹配字符串，否则授权验证时将会抛出 NullPointerException
        perm.setPermPattern(perm.getId() + ":*");
        perm.setCreatedTime(LocalDateTime.now());
        perm.setCreator(account.getId());
        perm.setPermType(PermType.MENU);
        perm.setValid(true);

        JsonObject res;
        if (permService.insert(perm) == 1) {
            Global.addCategory(perm);
            res = getSuccessResult(String.format("添加栏目%s成功", permName));
            res.add("node", Global.permMenuAsTreeNode(perm));
        } else {
            res = getFailResult("添加失败");
        }
        return res;
    }

    @RequestMapping(value = "/{categoryId:\\w{32}}/modify", method = RequestMethod.GET)
    @RequiresPermissions(value = "manage:*:perm")
    public String modify(@PathVariable(value = "categoryId") String categoryId, Model model) {
        Perm perm = permService.selectById(categoryId);
        model.addAttribute("perm", perm);
        model.addAttribute("categoryId", categoryId);
        return "backend/modifyCategory";
    }

    @RequestMapping(value = "/{categoryId:\\w{32}}/modify", method = RequestMethod.POST)
    @RequiresPermissions(value = "manage:*:perm")
    @ResponseBody
    public Object modify(@PathVariable(value = "categoryId") String categoryId,
                         @RequestParam String permName,
                         @RequestParam Integer weight,
                         @RequestParam Boolean attachable,
                         @ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account account) {
        Perm perm = permService.selectById(categoryId);
        String prev = perm.getPermName();
        perm.setPermName(permName);
        perm.setWeight(weight);
        perm.setAttachable(attachable);
        perm.setModifier(account.getId());
        perm.setModifiedTime(LocalDateTime.now());
        JsonObject res;
        if (permService.update(perm) == 1) {
            Global.updateCategory(perm);
            res = getSuccessResult(String.format("將栏目%s修改为%s成功", prev, permName));
            res.add("node", Global.permMenuAsTreeNode(perm));
            // 重载所有的账户信息
            AccountMgr.reloadAll();
        } else {
            res = getFailResult("修改失败");
        }
        return res;
    }


    @RequestMapping(value = "/{categoryId:\\w{32}}/delete", method = RequestMethod.POST)
    @RequiresPermissions(value = "manage:*:perm")
    @ResponseBody
    public Object delete(@PathVariable(value = "categoryId") String categoryId,
                         @ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account account) {
        Perm perm = permService.selectById(categoryId);
        perm.setModifier(account.getId());
        perm.setModifiedTime(LocalDateTime.now());
        perm.setActive(false);
        JsonObject res;
        if (permService.delete(perm) == 1) {
            res = getSuccessResult(String.format("删除栏目%s成功", perm.getPermName()));
            // 撤销所有相关联的角色
            //permService.detachAssociatedRole(perm.getId());
            Global.removeCategory(perm);
            // 重载所有的账户信息
            AccountMgr.reloadAll();
        } else {
            res = getSuccessResult("删除失败");
        }
        return res;
    }

}
