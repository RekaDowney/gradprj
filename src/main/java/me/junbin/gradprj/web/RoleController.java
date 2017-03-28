package me.junbin.gradprj.web;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.junbin.commons.page.PageRequest;
import me.junbin.commons.util.CollectionUtils;
import me.junbin.gradprj.domain.Account;
import me.junbin.gradprj.domain.Perm;
import me.junbin.gradprj.domain.Role;
import me.junbin.gradprj.enumeration.PermType;
import me.junbin.gradprj.service.PermService;
import me.junbin.gradprj.service.RoleService;
import me.junbin.gradprj.util.AccountMgr;
import me.junbin.gradprj.util.Global;
import me.junbin.gradprj.util.ZTreeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/25 11:34
 * @description :
 */
@Controller
@RequestMapping(value = "/role")
public class RoleController extends BaseRestController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private PermService permService;

    @RequiresPermissions(value = "manage:*:role")
    @RequestMapping(value = "/append", method = RequestMethod.GET)
    public String append() {
        return "backend/addRole";
    }

    @ResponseBody
    @RequiresPermissions(value = "manage:*:role")
    @RequestMapping(value = "/append", method = RequestMethod.POST)
    public Object append(Role role, @ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account account) {
        role.setId(Global.uuid());
        role.setCreator(account.getId());
        role.setCreatedTime(LocalDateTime.now());
        String info = role.getRoleName() + '(' + role.getRoleNameCn() + ')';
        JsonObject res;
        if (roleService.insert(role) == 1) {
            res = getSuccessResult(String.format("添加角色%s成功", info));
        } else {
            res = getFailResult(String.format("添加角色%s失败", info));
        }
        return res;
    }


    @RequiresPermissions(value = "manage:*:role")
    @RequestMapping(value = "/{roleId:\\w{32}}/modify", method = RequestMethod.GET)
    public String modify(@PathVariable(value = "roleId") String roleId, Model model) {
        model.addAttribute("role", roleService.selectById(roleId));
        return "backend/modifyRole";
    }

    @ResponseBody
    @RequiresPermissions(value = "manage:*:role")
    @RequestMapping(value = "/{roleId:\\w{32}}/modify", method = RequestMethod.POST)
    public Object modify(@PathVariable(value = "roleId") String roleId, Role role,
                         @ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account account) {
        Role db = roleService.selectById(roleId);
        String dbInfo = db.getRoleName() + '(' + db.getRoleNameCn() + ')';
        String info = role.getRoleName() + '(' + role.getRoleNameCn() + ')';
        db.setRoleName(role.getRoleName());
        db.setRoleNameCn(role.getRoleNameCn());
        db.setRemarks(role.getRemarks());
        db.setModifier(account.getId());
        db.setModifiedTime(LocalDateTime.now());
        JsonObject res;
        if (roleService.update(db) == 1) {
            if (StringUtils.equals(dbInfo, info)) {
                res = getSuccessResult(String.format("修改角色%s成功", info));
            } else {
                res = getSuccessResult(String.format("修改角色%s为%s成功", dbInfo, info));
            }
            AccountMgr.reloadAll();
        } else {
            res = getSuccessResult(String.format("删除角色%s失败", info));
        }
        return res;
    }

    @ResponseBody
    @RequiresPermissions(value = "manage:*:role")
    @RequestMapping(value = "/{roleId:\\w{32}}/delete", method = RequestMethod.POST)
    public Object delete(@PathVariable(value = "roleId") String roleId,
                         @ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account account) {
        Role role = roleService.selectById(roleId);
        role.setModifier(account.getId());
        role.setModifiedTime(LocalDateTime.now());
        role.setValid(false);
        String info = role.getRoleName() + '(' + role.getRoleNameCn() + ')';
        JsonObject res;
        if (roleService.delete(role) == 1) {
            res = getSuccessResult(String.format("删除角色%s成功", info));
            AccountMgr.reloadAll();
        } else {
            res = getSuccessResult(String.format("删除角色%s失败", info));
        }
        return res;
    }

    @RequiresPermissions(value = "manage:*:role")
    @RequestMapping(value = "/{roleId:\\w{32}}/perm/grant", method = RequestMethod.GET)
    public String grantPerm(@PathVariable(value = "roleId") String roleId, Model model) {
        Role role = roleService.selectById(roleId);
        model.addAttribute("role", role);
        List<Perm> total = permService.selectAll();
        List<Perm> functionalList = total.stream()
                                         .filter(p -> p.getPermType() != PermType.MENU)
                                         .collect(Collectors.toList());
        List<JsonObject> tree = ZTreeUtils.constructCheckedTree(functionalList, role.getPermIdList());
        model.addAttribute("tree", tree);
        return "backend/grantPerm";
    }

    @ResponseBody
    @RequiresPermissions(value = "manage:*:role")
    @RequestMapping(value = "/{roleId:\\w{32}}/perm/grant", method = RequestMethod.POST)
    public Object grantPerm(@PathVariable(value = "roleId") String roleId, @RequestBody String permIdArr,
                            @ModelAttribute(Global.LOGIN_ACCOUNT_KEY) Account account) {
        JsonArray array = new JsonParser().parse(permIdArr).getAsJsonArray();
        List<String> permIdList = new ArrayList<>(array.size());
        for (JsonElement item : array) {
            permIdList.add(item.getAsString());
        }
        JsonObject res;
        if (CollectionUtils.isEmpty(permIdList)) {
            res = getFailResult("请勾选上“文档浏览”权限");
            return res;
        }

        // 必须先将所有的“栏目”权限都获取出来，再添加到列表中，因为之后将移除所有的权限
        List<String> categoryIdList = roleService.getCategoryPermId(roleId);
        permIdList.addAll(categoryIdList);

        roleService.revokeAllPerms(roleId);
        roleService.grantPerm(roleId, permIdList);
        AccountMgr.reloadAll();
        return getSuccessResult("权限分配成功");
    }

    @RequiresPermissions(value = "manage:*:role")
    @RequestMapping(value = "/{roleId:\\w{32}}/category/grant", method = RequestMethod.GET)
    public String grantCategory(@PathVariable(value = "roleId") String roleId, Model model) {
        Role role = roleService.selectById(roleId);
        model.addAttribute("role", role);
        List<Perm> menu = Global.getCategoryMenu();
        List<JsonObject> tree = ZTreeUtils.constructCheckedTree(menu, role.getPermIdList());
        model.addAttribute("tree", tree);
        return "backend/grantCategory";
    }

    @ResponseBody
    @RequiresPermissions(value = "manage:*:role")
    @RequestMapping(value = "/{roleId:\\w{32}}/category/grant", method = RequestMethod.POST)
    public Object grantCategory(@PathVariable(value = "roleId") String roleId,
                                @RequestBody String catIdArr) {
        JsonArray array = new JsonParser().parse(catIdArr).getAsJsonArray();
        List<String> permIdList = new ArrayList<>(array.size());
        for (JsonElement item : array) {
            permIdList.add(item.getAsString());
        }
        JsonObject res;
        if (CollectionUtils.isEmpty(permIdList)) {
            res = getFailResult("请至少选择一个栏目");
            return res;
        }

        // 必须先将所有的“非栏目”权限都获取出来
        Role role = roleService.selectById(roleId);
        List<String> functionalIdList = role.getPermIdList();
        List<String> categoryIdList = roleService.getCategoryPermId(roleId);
        functionalIdList.removeAll(categoryIdList); // 此时的 functionalIdList 才是真正的非栏目权限 ID 列表
        permIdList.addAll(functionalIdList);

        roleService.revokeAllPerms(roleId);
        roleService.grantPerm(roleId, permIdList);
        AccountMgr.reloadAll();
        return getSuccessResult("栏目分配成功");
    }

    @ResponseBody
    @RequiresPermissions(value = "manage:*:role")
    @RequestMapping(value = "/page/{pageOffset:\\d+}/{pageSize:\\d+}", method = RequestMethod.POST)
    public Object pageAjax(@PathVariable("pageOffset") int pageOffset,
                           @PathVariable("pageSize") int pageSize,
                           @RequestParam(value = "roleName", required = false) String roleName) {
        return roleService.page(roleName, new PageRequest(pageOffset, pageSize));
    }

}
