package me.junbin.gradprj.web;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.junbin.commons.page.PageRequest;
import me.junbin.gradprj.domain.Account;
import me.junbin.gradprj.domain.Document;
import me.junbin.gradprj.domain.Role;
import me.junbin.gradprj.enumeration.DocSuffix;
import me.junbin.gradprj.enumeration.DocumentType;
import me.junbin.gradprj.service.AccountService;
import me.junbin.gradprj.service.DocumentService;
import me.junbin.gradprj.service.RoleService;
import me.junbin.gradprj.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static me.junbin.gradprj.util.Global.LOGIN_ACCOUNT_KEY;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/25 11:36
 * @description :
 */
@Controller
@RequestMapping(value = "/account")
public class AccountController extends BaseRestController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DocumentService documentService;

    @ResponseBody
    @RequestMapping(value = "/{principal:\\w+}/exists", method = RequestMethod.POST)
    public Object exists(@PathVariable("principal") String principal) {
        Account account = accountService.selectByPrincipal(principal);
        JsonObject res = getSuccessResult();
        res.addProperty("exists", account != null);
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/excel/upload", method = RequestMethod.POST)
    @RequiresPermissions(value = {"manage:*:user"})
    public Object upload(HttpServletRequest request) throws IOException, InvalidFormatException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        int size = fileMap.size();
        if (size != 1) {
            return getFailResult("请只选择一个Excel文件");
        }
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        MultipartFile file = fileMap.values().stream().findFirst().get();
        DocSuffix docSuffix = DocSuffix.of(file.getOriginalFilename());
        if (docSuffix.getType() != DocumentType.EXCEL) {
            return getFailResult("请只选择一个Excel文件");
        }
        List<Account> accountList = ExcelUtils.parse(file.getInputStream());
        accountService.replaceInsert(accountList);
        return getSuccessResult(String.format("成功插入或者覆盖%d个账户", accountList.size()));
    }

    @RequiresPermissions(value = "manage:*:user")
    @RequestMapping(value = "/append", method = RequestMethod.GET)
    public String append() {
        return "backend/addAccount";
    }

    @ResponseBody
    @RequiresPermissions(value = "manage:*:user")
    @RequestMapping(value = "/append", method = RequestMethod.POST)
    public Object append(@RequestParam String aPrincipal,
                         @RequestParam String aPassword) {
        Account newAccount = new Account();
        newAccount.setPrincipal(aPrincipal);
        newAccount.setPassword(EncryptUtils.md5Encrypt(aPassword, aPrincipal));
        newAccount.setId(Global.uuid());
        newAccount.setCreatedTime(LocalDateTime.now());
        JsonObject res;
        if (accountService.insert(newAccount) >= 1) {
            res = getSuccessResult(String.format("添加账户%s成功", aPrincipal));
        } else {
            res = getFailResult(String.format("添加账户%s失败", aPrincipal));
        }
        return res;
    }

    @ResponseBody
    @RequiresPermissions(value = "manage:*:user")
    @RequestMapping(value = "/{accountId:\\w{32}}/pwd/reset", method = RequestMethod.POST)
    public Object resetPwd(@PathVariable(value = "accountId") String accountId,
                           @ModelAttribute(LOGIN_ACCOUNT_KEY) Account account) {
        Account db = accountService.selectById(accountId);
        String aPrincipal = db.getPrincipal();
        db.setPassword(EncryptUtils.md5Encrypt(Global.DEFAULT_PASSWORD, aPrincipal));
        db.setModifier(account.getId());
        db.setModifiedTime(LocalDateTime.now());
        JsonObject res;
        if (accountService.update(db) >= 1) {
            res = getSuccessResult(String.format("重置账户%s的密码为“123456”成功", aPrincipal));
        } else {
            res = getFailResult(String.format("重置账户%s的密码为“123456”失败", aPrincipal));
        }
        return res;
    }

    @ResponseBody
    @RequiresPermissions(value = "manage:*:user")
    @RequestMapping(value = "/{accountId:\\w{32}}/delete", method = RequestMethod.POST)
    public Object delete(@PathVariable(value = "accountId") String accountId,
                         @ModelAttribute(LOGIN_ACCOUNT_KEY) Account account) {
        Account delAccount = accountService.selectById(accountId);
        delAccount.setValid(false);
        delAccount.setModifier(account.getId());
        delAccount.setModifiedTime(LocalDateTime.now());
        JsonObject res;
        if (accountService.delete(delAccount) >= 1) {
            res = getSuccessResult(String.format("删除账户%s成功", delAccount.getPrincipal()));
            AccountMgr.removeByAccountId(delAccount.getId());
        } else {
            res = getFailResult(String.format("删除账户%s失败", delAccount.getPrincipal()));
        }
        return res;
    }

    @RequiresPermissions(value = "manage:*:user")
    @RequestMapping(value = "/{accountId:\\w{32}}/role/grant", method = RequestMethod.GET)
    public String grantRole(@PathVariable(value = "accountId") String accountId, Model model) {
        Account account = accountService.selectById(accountId);
        model.addAttribute("account", account);
        List<Role> total = roleService.selectAll();
        List<Role> roleList = accountService.acquireRoles(accountId);
        model.addAttribute("tree", ZTreeUtils.constructCheckedRoleTree(total, roleList));
        return "backend/grantRole";
    }

    @ResponseBody
    @RequiresPermissions(value = "manage:*:user")
    @RequestMapping(value = "/{accountId:\\w{32}}/role/grant", method = RequestMethod.POST)
    public Object grantRole(@RequestBody String roleIdArr,
                            @PathVariable(value = "accountId") String accountId) {
        JsonArray array = new JsonParser().parse(roleIdArr).getAsJsonArray();
        Account account = accountService.selectById(accountId);
        if (array.size() == 0) {
            accountService.revokeAllRoles(accountId);
            AccountMgr.reloadByAccountId(accountId);
            return getSuccessResult(String.format("已清空账户%s拥有的所有角色", account.getPrincipal()));
        }
        List<String> roleIdList = new ArrayList<>(array.size());
        for (JsonElement item : array) {
            roleIdList.add(item.getAsString());
        }
        accountService.revokeAllRoles(accountId);
        accountService.grantRole(accountId, roleIdList);
        if (StringUtils.equals(account.getPrincipal(), Global.VISITOR_NAME)) {
            AccountMgr.reloadVisitor();
        } else {
            AccountMgr.reloadByAccountId(accountId);
        }
        return getSuccessResult("角色分配成功");
    }

    @ResponseBody
    @RequiresPermissions(value = "manage:*:user")
    @RequestMapping(value = "/page/{pageOffset:\\d+}/{pageSize:\\d+}", method = RequestMethod.POST)
    public Object pageAjax(@PathVariable("pageOffset") int pageOffset,
                           @PathVariable("pageSize") int pageSize,
                           @RequestParam(value = "aPrincipal", required = false) String aPrincipal) {
        return accountService.page(aPrincipal, new PageRequest(pageOffset, pageSize));
    }

    @RequiresRoles(value = {Global.REGISTERED_USER_ROLE_NAME})
    @RequestMapping(value = "/personal/center", method = RequestMethod.GET)
    public String personalCenter(@ModelAttribute(LOGIN_ACCOUNT_KEY) Account account, Model model) {
        model.addAttribute("account", account);
        return "common/index";
    }

    @RequiresRoles(value = {Global.REGISTERED_USER_ROLE_NAME})
    @RequestMapping(value = "/doc/page", method = RequestMethod.GET)
    public String docPage(@ModelAttribute(LOGIN_ACCOUNT_KEY) Account account, Model model) {
        model.addAttribute("page", documentService.pagePersonal(account.getId(),
                new PageRequest(0, Global.DEFAULT_PAGE_SIZE)));
        return "common/manageDoc";
    }

    @ResponseBody
    @RequiresRoles(value = {Global.REGISTERED_USER_ROLE_NAME})
    @RequestMapping(value = "/doc/page/{pageOffset:\\d+}/{pageSize:\\d+}", method = RequestMethod.POST)
    public Object docPageAjax(@ModelAttribute(LOGIN_ACCOUNT_KEY) Account account,
                              @PathVariable("pageOffset") int pageOffset,
                              @PathVariable("pageSize") int pageSize,
                              @RequestParam(value = "docName", required = false) String docName) {
        return documentService.pagePersonal(account.getId(),
                new PageRequest(pageOffset, pageSize), docName);
    }

    @ResponseBody
    @RequiresRoles(value = {Global.REGISTERED_USER_ROLE_NAME})
    @RequestMapping(value = "/doc/{docId:\\w{32}}/delete", method = RequestMethod.POST)
    public Object docDelete(@PathVariable("docId") String docId,
                            @ModelAttribute(LOGIN_ACCOUNT_KEY) Account account) {
        Document document = documentService.selectById(docId);
        String docName = document.getDocName();
        String accountId = account.getId();
        if (!StringUtils.equals(document.getCreator(), accountId)) {
            return getFailResult(String.format("文档“%s”并非由你所上传！你无法执行删除操作！", docName));
        }
        document.setModifier(accountId);
        document.setModifiedTime(LocalDateTime.now());
        document.setValid(false);
        if (documentService.delete(document) >= 1) {
            return getSuccessResult(String.format("删除文档“%s”成功", docName));
        } else {
            return getFailResult(String.format("删除文档“%s”失败", docName));
        }
    }

    @RequestMapping(value = "/pwd/modify", method = RequestMethod.GET)
    public String modify(@ModelAttribute(LOGIN_ACCOUNT_KEY) Account account, Model model) {
        model.addAttribute("account", account);
        return "common/modifyAccount";
    }

    @ResponseBody
    @RequiresRoles(value = {Global.REGISTERED_USER_ROLE_NAME})
    @RequestMapping(value = "/pwd/modify", method = RequestMethod.POST)
    public Object modify(@ModelAttribute(LOGIN_ACCOUNT_KEY) Account account,
                         @RequestParam String srcPwd,
                         @RequestParam String newPwd) {
        String principal = account.getPrincipal();
        if (!StringUtils.equals(account.getPassword(), EncryptUtils.md5Encrypt(srcPwd, principal))) {
            return getFailResult("原密码不正确！");
        }
        account.setPassword(EncryptUtils.md5Encrypt(newPwd, principal));
        JsonObject res;
        if (accountService.update(account) == 1) {
            res = getSuccessResult("密码修改成功");
        } else {
            res = getFailResult("密码修改失败");
        }
        return res;
    }

/*
    @RequiresRoles(value = {Global.REGISTERED_USER_ROLE_NAME})
    @RequestMapping(value = "/doc/{docId:\\w{32}}/download", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(@PathVariable("docId") String docId) throws IOException {
        Document document = documentService.selectById(docId);
        if (document == null){
        }
        Path path = DocumentUtils.getActualPath(document.getDocUrl());
        if (!Files.exists(path)) {

        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentDispositionFormData("attachment",
                new String(document.getDocName().getBytes(Charsets.UTF8), Charsets.ISO88591));
        return new ResponseEntity<>(Files.readAllBytes(path), httpHeaders, HttpStatus.CREATED);
    }
*/

}
