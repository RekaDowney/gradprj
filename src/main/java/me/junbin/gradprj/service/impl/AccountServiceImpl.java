package me.junbin.gradprj.service.impl;

import me.junbin.commons.page.Page;
import me.junbin.commons.page.PageRequest;
import me.junbin.commons.util.Args;
import me.junbin.gradprj.domain.Account;
import me.junbin.gradprj.domain.Role;
import me.junbin.gradprj.repository.AccountRepo;
import me.junbin.gradprj.service.AccountService;
import me.junbin.gradprj.util.Global;
import me.junbin.gradprj.util.validate.MyValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/1 13:19
 * @description :
 */
@Service("accountService")
@CacheConfig(cacheNames = "accountCache")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepo accountRepo;
    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);
    private static final List<Account> emptyAccountList = Collections.emptyList();

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "'accountList'")})
    public int insert(Account account) {
        MyValidator.nullThrowsForProperty(account, "id", "principal", "password");
        log.debug("添加新账户{}", account.toString());
        return accountRepo.insert(account);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "'accountList'")})
    public int batchInsert(List<Account> accounts) {
        MyValidator.emptyThrows(accounts);
        for (Account account : accounts) {
            MyValidator.nullThrowsForProperty(account, "id", "principal", "password");
        }
        log.debug("添加多个账户{}", accounts.toString());
        return accountRepo.batchInsert(accounts);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "'accountList'")})
    public void replaceInsert(List<Account> accounts) {
        MyValidator.emptyThrows(accounts);
        for (Account account : accounts) {
            MyValidator.nullThrowsForProperty(account, "id", "principal", "password");
        }
        accountRepo.replaceInsert(accounts);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#p0.id"), @CacheEvict(key = "'accountList'")})
    public int delete(Account account) {
        Args.notNull(account);
        String id = account.getId();
        String modifier = account.getModifier();
        LocalDateTime modifiedTime = account.getModifiedTime();
        MyValidator.nullThrows(id, modifier, modifiedTime);
        log.debug("准备删除账户{}，删除操作发起人{}", id, modifier);
        log.debug("撤销账户{}拥有的所有角色", id);
        accountRepo.revokeAllRoles(id);
        log.debug("删除账户{}", id);
        return accountRepo.deleteAccount(id, modifier, modifiedTime);
    }

    @Transactional
    @Caching(evict = {@CacheEvict(key = "#p0.id"), @CacheEvict(key = "'accountList'")})
    public int update(Account account) {
        Args.notNull(account);
        String id = account.getId();
        String modifier = account.getModifier();
        LocalDateTime modifiedTime = account.getModifiedTime();
        MyValidator.nullThrows(id, modifier, modifiedTime);
        log.debug("更新账户{}，更新操作发起人{}", id, modifier);
        return accountRepo.update(account);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#p0.id"), @CacheEvict(key = "'accountList'")})
    public int lock(Account account) {
        Args.notNull(account);
        String id = account.getId();
        String modifier = account.getModifier();
        LocalDateTime modifiedTime = account.getModifiedTime();
        MyValidator.nullThrows(id, modifier, modifiedTime);
        log.debug("锁定账户{}，锁定操作发起人{}", id, modifier);
        return accountRepo.lock(id, modifier, modifiedTime);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#p0.id"), @CacheEvict(key = "'accountList'")})
    public int unlock(Account account) {
        Args.notNull(account);
        String id = account.getId();
        String modifier = account.getModifier();
        LocalDateTime modifiedTime = account.getModifiedTime();
        MyValidator.nullThrows(id, modifier, modifiedTime);
        log.debug("解锁账户{}，解锁操作发起人{}", id, modifier);
        return accountRepo.unlock(id, modifier, modifiedTime);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#accountId"), @CacheEvict(key = "'accountList'")})
    public int grantRole(String accountId, String firstRole, String... moreRoles) {
        MyValidator.nullThrows(accountId, firstRole);
        log.debug("授予账户{}角色{}", accountId, firstRole, Arrays.toString(moreRoles));
        return accountRepo.grantRole(accountId, firstRole, moreRoles);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#p0.id"), @CacheEvict(key = "'accountList'")})
    public int grantRole(Account account, String firstRole, String... moreRoles) {
        Args.notNull(account);
        String id = account.getId();
        log.debug("获取当前 Aop 对 AccountService 的代理");
        AccountService proxy = (AccountService) AopContext.currentProxy();
        return proxy.grantRole(id, firstRole, moreRoles);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#accountId"), @CacheEvict(key = "'accountList'")})
    public int grantRole(String accountId, List<String> roleIdList) {
        Args.notNull(accountId);
        MyValidator.emptyThrows(roleIdList);
        log.debug("授予账户{}角色{}", accountId, roleIdList);
        return accountRepo.grantRoleList(accountId, roleIdList);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#p0.id"), @CacheEvict(key = "'accountList'")})
    public int grantRole(Account account, List<String> roleIdList) {
        Args.notNull(account);
        String id = account.getId();
        log.debug("获取当前 Aop 对 AccountService 的代理");
        AccountService proxy = (AccountService) AopContext.currentProxy();
        return proxy.grantRole(id, roleIdList);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#accountId"), @CacheEvict(key = "'accountList'")})
    public int revokeAllRoles(String accountId) {
        Args.notNull(accountId);
        log.debug("撤销账户{}拥有的所有角色", accountId);
        return accountRepo.revokeAllRoles(accountId);
    }

    @Override
    @Transactional(readOnly = true)
    @Caching(cacheable = {@Cacheable(key = "#id")})
    public Account selectById(String id) {
        Args.notNull(id);
        log.debug("查询 id 为{}的账户", id);
        return accountRepo.selectById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Caching(cacheable = {@Cacheable(key = "#principal")})
    public Account selectByPrincipal(String principal) {
        Args.notNull(principal);
        log.debug("查询身份为{}的账户", principal);
        return accountRepo.selectByPrincipal(principal);
    }

    @Override
    @Transactional(readOnly = true)
    @Caching(cacheable = {@Cacheable(key = "'accountList'")})
    public List<Account> selectAll() {
        log.debug("查询账户列表");
        return accountRepo.selectAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> acquireRoles(String accountId) {
        Args.notNull(accountId);
        log.debug("获取账户{}的所有角色", accountId);
        return accountRepo.acquireRoles(accountId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Account> page(PageRequest pageRequest) {
        // RoleService 可以，然后 AccountService 竟然出现 ClassCastException
        //  me.junbin.gradprj.web.BackendController$$EnhancerBySpringCGLIB$$217722dd cannot be cast to me.junbin.gradprj.service.AccountService
        AccountService proxy = (AccountService) AopContext.currentProxy();
        return proxy.page(null, pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Account> page(String principal, PageRequest pageRequest) {
        MyValidator.nullThrows(pageRequest);
        int pageOffset = pageRequest.getPageOffset();
        int pageSize = pageRequest.getPageSize();
        String nameLike = Global.toLike(principal);
        long total = accountRepo.total(nameLike);
        if (total == 0) {
            log.debug("没有任何名称中包含{}的记录", principal);
            return new Page.Builder<>(pageRequest, total, emptyAccountList).create();
        }
        log.debug("名称中包含{}的账户共有{}条记录", principal, total);
        int skip = pageOffset * pageSize;
        if (skip >= total) {
            log.debug("查询所跳过的记录数{}大于等于实际总记录数{}", skip, total);
            return new Page.Builder<>(pageRequest, total, emptyAccountList).create();
        }
        log.debug("查询名称中包含{}，第{}页（每页{}条记录）的记录", principal, pageOffset + 1, pageSize);
        List<Account> content = accountRepo.page(skip, pageSize, nameLike);
        return new Page.Builder<>(pageRequest, total, content).create();
    }

}
