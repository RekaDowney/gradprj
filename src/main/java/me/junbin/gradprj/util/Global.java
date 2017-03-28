package me.junbin.gradprj.util;

import com.google.gson.JsonObject;
import me.junbin.commons.prop.KVTranslator;
import me.junbin.commons.util.PathUtils;
import me.junbin.gradprj.domain.Account;
import me.junbin.gradprj.domain.Perm;
import me.junbin.gradprj.domain.Role;
import me.junbin.gradprj.enumeration.PermType;
import me.junbin.gradprj.service.AccountService;
import me.junbin.gradprj.service.PermService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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
    public static final String REGISTERED_USER_ROLE_NAME = "user";
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
    public static final int DOC_EDIT_TIME_SLICE;
    public static final int DEFAULT_PAGE_SIZE;
    public static final String DEFAULT_PASSWORD = "123456";

    static {
        DOC_PREFIX_URL = kvTranslator.getAsString("doc.mapping.url");
        IMAGE_PREFIX_URL = kvTranslator.getAsString("image.mapping.url");
        KAPTCHA_SESSION_KEY = kvTranslator.getAsString("kaptcha.session.key");
        DOC_EDIT_TIME_SLICE = kvTranslator.getAsInt("default.edit.slice");
        DEFAULT_PAGE_SIZE = kvTranslator.getAsInt("default.page.size");
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

    public static String getVisitorName() {
        return VISITOR_NAME;
    }

    public static String getRegisteredUserRoleName() {
        return REGISTERED_USER_ROLE_NAME;
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

            List<Perm> menu = permList.stream().filter(p -> p.getPermType() == PermType.MENU)
                                      .collect(Collectors.toList());
            visitor.setRelationPermList(RelationResolver.relationalize(menu));

            VISITOR = visitor;

            // 不存储，直接由 AccountMgr 直接更新
            // AccountMgr.store(visitor);
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
            permList = RelationResolver.relationalize(permList);
            Optional<Perm> perm = permList.stream().filter(p -> p.getPermName().equals("分类文档")).findFirst();
            assert perm.isPresent();
            //CATEGORY_TREE = getCategoryMenuTree(Collections.singletonList(perm.get()));
            Queue<Perm> permQueue = new ArrayDeque<>();
            permQueue.add(perm.get());
            Perm p;
            while ((p = permQueue.poll()) != null) {
                CATEGORY_MENU.add(p);
                if (p.getSub() != null)
                    permQueue.addAll(p.getSub());
            }

            CATEGORY_TREE = CATEGORY_MENU.stream()
                                         .map(Global::permMenuAsTreeNode)
                                         .collect(Collectors.toList());
        }
    }

    /**
     * 根据权限树获取栏目树
     *
     * @param relationalPermList 权限树，必须是子级相关的，即父子关系是正常的而不是独立的（此时子级元素都放在 {@link Perm#sub}）中
     *                           //* @param ignoreCategory     是否忽略分类文档这个栏目，默认该栏目为根，如果忽略则根变为该栏目的直接子级栏目
     * @return 栏目树
     */
    public static List<JsonObject> getCategoryMenuTree(List<Perm> relationalPermList) {
        Optional<Perm> perm = relationalPermList.stream().filter(p -> p.getPermName().equals("分类文档")).findFirst();
        String id = null;
        if (perm.isPresent()) {
            Queue<Perm> permQueue = new ArrayDeque<>();
            Perm root = perm.get();
            id = root.getId();
            permQueue.add(root);
            Perm p;
            List<Perm> menu = new ArrayList<>();
            while ((p = permQueue.poll()) != null) {
/*
                if (!ignoreCategory) {
                    menu.add(p);
                }
*/
                menu.add(p);
                if (p.getSub() != null)
                    permQueue.addAll(p.getSub());
            }
/*
            if (ignoreCategory) {
                for (Perm node : menu) {
                    if (StringUtils.equals(node.getParentId(), id)) {
                        node.setParentId(null);
                    }
                }
            }
*/
            return menu.stream().map(Global::permMenuAsTreeNode).collect(Collectors.toList());
        }
        return null;
    }

    public static List<Perm> getCategoryMenu() {
        return CategoryHolder.CATEGORY_MENU;
    }

    public static List<JsonObject> getCategoryTree() {
        return CategoryHolder.CATEGORY_TREE;
    }

    public static JsonObject permMenuAsTreeNode(Perm menu) {
        return copyPermMenuAsTreeNode(menu, new JsonObject());
    }

    public static JsonObject copyPermMenuAsTreeNode(Perm menu, JsonObject category) {
        category.addProperty("id", menu.getId());
        category.addProperty("name", menu.getPermName());
        category.addProperty("parentId", menu.getParentId());
        if (menu.isAttachable()) {
            category.addProperty("attachable", true);
        } else {
            category.addProperty("isParent", true);
            category.addProperty("open", true);
        }
        return category;
    }

    /**
     * 添加栏目到全局的栏目列表和栏目树
     *
     * @param category 栏目
     * @return 添加成功与否
     */
    public static boolean addCategory(Perm category) {
        return Global.getCategoryMenu().add(category) &&
                Global.getCategoryTree().add(Global.permMenuAsTreeNode(category));
    }

    /**
     * 将指定栏目从全局的栏目列表和栏目树中删除（如果存在的话）
     *
     * @param category 栏目
     */
    public static void removeCategory(Perm category) {
        String categoryId = category.getId();

        List<Perm> menuList = Global.getCategoryMenu();
        Iterator<Perm> menuIterator = menuList.iterator();
        Perm menu;
        while (menuIterator.hasNext()) {
            menu = menuIterator.next();
            if (StringUtils.equals(menu.getId(), categoryId)) {
                menuIterator.remove();
            }
        }

        List<JsonObject> tree = Global.getCategoryTree();
        Iterator<JsonObject> treeIterator = tree.iterator();
        JsonObject node;
        while (treeIterator.hasNext()) {
            node = treeIterator.next();
            if (StringUtils.equals(node.get("id").getAsString(), categoryId)) {
                treeIterator.remove();
            }
        }
    }

    /**
     * 从全局的栏目列表和栏目树中更新指定栏目
     *
     * @param category 栏目
     */
    public static void updateCategory(Perm category) {
        String id = category.getId();
        Global.getCategoryMenu().stream().parallel()
              .filter(p -> StringUtils.equals(p.getId(), id))
              .findFirst()
              .ifPresent(p -> {
                  try {
                      BeanUtils.copyProperties(p, category);
                  } catch (IllegalAccessException | InvocationTargetException e) {
                      e.printStackTrace();
                  }
              });
        Global.getCategoryTree().stream().parallel()
              .filter(node -> StringUtils.equals(node.get("id").getAsString(), id))
              .findFirst()
              .ifPresent(node -> copyPermMenuAsTreeNode(category, node));
    }

    public static String toLike(String column) {
        if (StringUtils.isEmpty(column)) {
            return "%%";
        }
        return '%' + column + '%';
    }

}
