package me.junbin.gradprj.util;

import com.google.gson.JsonObject;
import me.junbin.commons.prop.KVTranslator;
import me.junbin.commons.util.PathUtils;
import me.junbin.gradprj.domain.Account;
import me.junbin.gradprj.domain.Perm;
import me.junbin.gradprj.domain.Role;
import me.junbin.gradprj.service.AccountService;
import me.junbin.gradprj.service.PermService;
import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/1/23 22:36
 * @description :
 */
public enum Global {

    ;

    private static final KVTranslator kvTranslator;

    static {
        Path path = PathUtils.classpath("bundle/app.properties");
        try {
            kvTranslator = KVTranslator.properties(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String DOC_PREFIX_URL;
    public static final String DOC_LOCATION;
    public static final String IMAGE_PREFIX_URL;
    public static final String IMAGE_LOCATION;
    public static final String KAPTCHA_SESSION_KEY;
    public static final String LOGIN_ACCOUNT_KEY = "loginAccount";
    public static final String COOKIE_NAME = "shiroCookie";
    public static final String LOGIN_ERROR_KEY = "loginError";
    public static final String VISITOR_NAME = "visitor";
    public static final String ERROR_TIME_KEY = "errorTime";
    public static final String CAPTCHA_KEY = "kaptcha";
    public static final String ERROR_50X_KEY = "error_50x";
    public static final String ERROR_40X_KEY = "error_40x";
    public static final String ERROR_TIP_PRINCIPAL = "用户名/密码错误";
    public static final String ERROR_TIP_CAPTCHA = "验证码错误";
    public static final String PRINCIPAL = "principal";
    public static final char DOT = '.';
    public static final char SLASH = '/';
    public static final char BACKSLASH = '\\';
    public static final String JSON_STATUS_KEY = "status";
    public static final String JSON_ERROR_MSG_KEY = "errorMsg";
    public static final String JSON_SUCCESS_MSG_KEY = "successMsg";
    // 不采用 true 和 false 是因为可能在页面上需要进行二次解析
    // 如果 JSON.parse(data).status == false，那么需要执行二次解析
    public static final String STATUS_OK = "success";
    public static final String STATUS_ERROR = "error";

    static {
        DOC_PREFIX_URL = kvTranslator.getAsString("doc.mapping.url");
        IMAGE_PREFIX_URL = kvTranslator.getAsString("image.mapping.url");
        KAPTCHA_SESSION_KEY = kvTranslator.getAsString("kaptcha.session.key");
        if (SystemUtils.IS_OS_WINDOWS) {
            DOC_LOCATION = kvTranslator.getAsString("windows.doc.mapping.location");
            IMAGE_LOCATION = kvTranslator.getAsString("windows.image.mapping.location");
        } else {
            DOC_LOCATION = kvTranslator.getAsString("linux.doc.mapping.location");
            IMAGE_LOCATION = kvTranslator.getAsString("linux.image.mapping.location");
        }
        try {
            FileUtils.forceMkDirIfNotExists(DOC_LOCATION);
            FileUtils.forceMkDirIfNotExists(IMAGE_LOCATION);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String normalize(String filePath) {
        if (filePath == null) {
            return null;
        }
        return Paths.get(filePath).normalize().toString();
    }

    public static String slashPath(String filePath) {
        if (filePath == null) {
            return null;
        }
        return Paths.get(filePath).normalize().toString().replaceAll("\\\\+", "/");
    }

    public static String imagePrefixUrl() {
        return IMAGE_PREFIX_URL;
    }

    public static String imageLocation() {
        return IMAGE_LOCATION;
    }

    public static String docPrefixUrl() {
        return DOC_PREFIX_URL;
    }

    public static String docLocation() {
        return DOC_LOCATION;
    }

    public static String captchaSessionKey() {
        return KAPTCHA_SESSION_KEY;
    }

    public static String loginAccountKey() {
        return LOGIN_ACCOUNT_KEY;
    }

    public static String cookieName() {
        return COOKIE_NAME;
    }

    public static String loginErrorKey() {
        return LOGIN_ERROR_KEY;
    }

    public static String errorTimeKey() {
        return ERROR_TIME_KEY;
    }

    public static String errorTipPrincipal() {
        return ERROR_TIP_PRINCIPAL;
    }

    public static String errorTipCaptcha() {
        return ERROR_TIP_CAPTCHA;
    }

    public static String error40xKey() {
        return ERROR_40X_KEY;
    }

    public static String error50xKey() {
        return ERROR_50X_KEY;
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getJsonSuccessMsgKey() {
        return JSON_SUCCESS_MSG_KEY;
    }

    public static String getJsonErrorMsgKey() {
        return JSON_ERROR_MSG_KEY;
    }

    public static String getJsonStatusKey() {
        return JSON_STATUS_KEY;
    }

    public static String getStatusOk() {
        return STATUS_OK;
    }

    public static String getStatusError() {
        return STATUS_ERROR;
    }

    private static final class VisitorHolder {
        private static final Account VISITOR;

        static {
            AccountService accountService = WebAppCtxHolder.getBean(AccountService.class);
            PermService permService = WebAppCtxHolder.getBean(PermService.class);
            Account visitor = accountService.selectByPrincipal(VISITOR_NAME);
            assert visitor != null;
            List<Role> roleList = accountService.acquireRoles(visitor.getId());
            Set<String> permIdSet = new HashSet<>();
            for (Role role : roleList) {
                permIdSet.addAll(role.getPermIdList());
            }
            List<Perm> totalPerm = permService.selectAll();
            List<Perm> permList = RelationResolver.findIn(totalPerm, permIdSet);
            visitor.setPermList(permList);
            visitor.setRoleList(roleList);
            VISITOR = visitor;
            AccountMgr.store(visitor);
        }
    }

    public static Account getVisitorAccount() {
        return VisitorHolder.VISITOR;
    }

    private static final class CategoryHolder {
        private static final List<Perm> CATEGORY_MENU;
        public static final List<JsonObject> CATEGORY_TREE;

        static {
            CATEGORY_MENU = new ArrayList<>();
            PermService permService = WebAppCtxHolder.getBean(PermService.class);
            List<Perm> permList = permService.selectAll();
            RelationResolver.relationalize(permList);
            Optional<Perm> perm = permList.stream().filter(p -> p.getPermName().equals("分类文档")).findFirst();
            assert perm.isPresent();
            Queue<Perm> permQueue = new ArrayDeque<>();
            permQueue.add(perm.get());
            Perm p;
            while ((p = permQueue.poll()) != null) {
                CATEGORY_MENU.add(p);
                if (p.getSub() != null)
                    permQueue.addAll(p.getSub());
            }

            CATEGORY_TREE = new ArrayList<>(CATEGORY_MENU.size());
            JsonObject category;
            for (Perm menu : CATEGORY_MENU) {
                category = new JsonObject();
                category.addProperty("id", menu.getId());
                category.addProperty("name", menu.getPermName());
                category.addProperty("parentId", menu.getParentId());
                if (menu.isAttachable()) {
                    category.addProperty("attachable", true);
                } else {
                    category.addProperty("open", true);
                }
                CATEGORY_TREE.add(category);
            }
        }
    }

    public static List<Perm> getCategoryMenu() {
        return CategoryHolder.CATEGORY_MENU;
    }

    public static List<JsonObject> getCategoryTree() {
        return CategoryHolder.CATEGORY_TREE;
    }

}
