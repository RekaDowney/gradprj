package me.junbin.gradprj.util;

import me.junbin.commons.util.Args;
import me.junbin.gradprj.domain.Account;
import me.junbin.gradprj.domain.Perm;
import me.junbin.gradprj.domain.Role;
import me.junbin.gradprj.service.AccountService;
import me.junbin.gradprj.service.PermService;
import me.junbin.gradprj.service.RoleService;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/26 19:53
 * @description :
 */
public abstract class AccountMgr {

    private static final Map<String, Account> manager = new ConcurrentHashMap<>();
    private static AccountService accountService;
    private static RoleService roleService;
    private static PermService permService;
    private static Lock lock = new ReentrantLock();

    public static Account get(String accountId) {
        Args.notNull(accountId);
        return manager.get(accountId);
    }

    public static void store(Account account) {
        Args.notNull(account);
        manager.put(account.getId(), account);
    }

    public static Account remove(String accountId) {
        return manager.remove(accountId);
    }

    public static void reload() {
        for (Account account : manager.values()) {
            reload(account.getId());
        }
    }

    public static Account reload(String accountId) {
        lock.lock();
        try {
            AccountService accountService = getAccountService();
            PermService permService = getPermService();
            Account account = accountService.selectById(accountId);
            assert account != null;
            List<Role> roleList = accountService.acquireRoles(accountId);
            account.setRoleList(roleList);
            account.setPermList(rolePerms(roleList, permService.selectAll()));
            account.setRelationPermList(account.getPermList());
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
