package me.junbin.gradprj.util;

import me.junbin.commons.util.Args;
import me.junbin.gradprj.domain.Account;
import me.junbin.gradprj.domain.Perm;
import me.junbin.gradprj.domain.Role;
import me.junbin.gradprj.service.AccountService;
import me.junbin.gradprj.service.PermService;
import me.junbin.gradprj.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/26 19:53
 * @description :
 */
public abstract class AccountMgr {

    // Map<SessionId, MutablePair<String, Account>>
    private static final Map<String, MutablePair<String, Account>> manager = new HashMap<>();
    private static AccountService accountService;
    private static RoleService roleService;
    private static PermService permService;
    private static Lock lock = new ReentrantLock();

    public static Account getByAccountId(String accountId) {
        Args.notNull(accountId);
        for (Pair<String, Account> pair : manager.values()) {
            if (pair != null && StringUtils.equals(pair.getKey(), accountId)) {
                return pair.getValue();
            }
        }
        return null;
    }

    public static Account getBySessionId(String sessionId) {
        Args.notNull(sessionId);
        Pair<String, Account> pair = manager.get(sessionId);
        return pair == null ? null : pair.getValue();
    }

    public static void store(String sessionId) {
        Args.notNull(sessionId);
        manager.put(sessionId, null);
    }

    public static void store(String sessionId, Account account) {
        Args.notNull(sessionId);
        Args.notNull(account);
        MutablePair<String, Account> pair = manager.get(sessionId);
        if (pair == null) {
            pair = new MutablePair<>(account.getId(), account);
            manager.put(sessionId, pair);
        } else {
            pair.setLeft(sessionId);
            pair.setValue(account);
        }
    }

    public static Account removeByAccountId(String accountId) {
        Args.notNull(accountId);
        String sessionId = null;
        Account account = null;
        for (Map.Entry<String, MutablePair<String, Account>> entry : manager.entrySet()) {
            MutablePair<String, Account> pair = entry.getValue();
            if (pair != null && StringUtils.equals(pair.getKey(), accountId)) {
                sessionId = entry.getKey();
                account = pair.getValue();
                break;
            }
        }
        if (sessionId != null) {
            manager.put(sessionId, null);
        }
        return account;
    }

    public static Account removeBySessionId(String sessionId) {
        Args.notNull(sessionId);
        Account account = null;
        MutablePair<String, Account> pair = manager.get(sessionId);
        if (pair != null) {
            account = pair.getValue();
            manager.put(sessionId, null);
        }
        return account;
    }

    public static Pair<String, Account> remove(String sessionId) {
        Args.notNull(sessionId);
        return manager.remove(sessionId);
    }

    public static void reloadAll() {
        manager.values().stream().filter(pair -> pair != null).forEach(pair -> {
            reload(pair.getValue());
        });
        reloadVisitor();
    }

    public static Account reloadByAccountId(String accountId) {
        Account account = getByAccountId(accountId);
        if (account != null) {
            return reload(account);
        }
        return null;
    }

    public static Account reloadBySessionId(String sessionId) {
        Account account = getBySessionId(sessionId);
        if (account != null) {
            return reload(account);
        }
        return null;
    }

    public static Account reloadVisitor() {
        return reload(Global.getVisitorAccount());
    }

    public static Account reload(Account account) {
        lock.lock();
        String accountId = account.getId();
        try {
            AccountService accountService = getAccountService();
            PermService permService = getPermService();
            Account db = accountService.selectById(accountId);
            assert db != null;
            account.setPrincipal(db.getPrincipal());
            account.setPassword(db.getPassword());
            account.setLocked(db.isLocked());
            account.setModifier(db.getModifier());
            account.setModifiedTime(db.getModifiedTime());
            account.setUser(db.getUser());
            List<Role> roleList = accountService.acquireRoles(accountId);
            account.setRoleList(roleList);
            account.setPermList(rolePerms(roleList, permService.selectAll()));
            // 注意这里需要调用关系化方法将关系先父子关联起来，否则后面的执行过程会有问题
            account.setRelationPermList(RelationResolver.relationalize(account.getPermList()));
            return account;
        } finally {
            lock.unlock();
        }
    }

    private static List<Perm> rolePerms(List<Role> roleList, List<Perm> totalPerm) {
        Set<String> permIdSet = new HashSet<>();
        for (Role role : roleList) {
            permIdSet.addAll(role.getPermIdList());
        }
        return RelationResolver.findIn(totalPerm, permIdSet);
    }


    public static AccountService getAccountService() {
        if (accountService == null) {
            accountService = WebAppCtxHolder.getBean(AccountService.class);
            assert accountService != null;
        }
        return accountService;
    }

    public static RoleService getRoleService() {
        if (roleService == null) {
            roleService = WebAppCtxHolder.getBean(RoleService.class);
            assert roleService != null;
        }
        return roleService;
    }

    public static PermService getPermService() {
        if (permService == null) {
            permService = WebAppCtxHolder.getBean(PermService.class);
            assert permService != null;
        }
        return permService;
    }

}
