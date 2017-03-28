package me.junbin.gradprj.service.impl;

import me.junbin.commons.page.Page;
import me.junbin.commons.page.PageRequest;
import me.junbin.commons.util.Args;
import me.junbin.gradprj.domain.Role;
import me.junbin.gradprj.enumeration.PermType;
import me.junbin.gradprj.repository.RoleRepo;
import me.junbin.gradprj.service.RoleService;
import me.junbin.gradprj.util.Global;
import me.junbin.gradprj.util.validate.MyValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/19 8:18
 * @description :
 */
@Service("roleService")
@CacheConfig(cacheNames = "roleCache")
public class RoleServiceImpl implements RoleService {

    /*
        @Autowired
        private ApplicationContext appCtx;
        private RoleService proxy;
    */
    @Autowired
    private RoleRepo roleRepo;
    private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);
    private static final List<Role> emptyRoleList = Collections.emptyList();

/*
    @PostConstruct
    public void postConstruct() {
        proxy = appCtx.getBean(RoleService.class);
    }
*/

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "'roleList'")})
    public int insert(Role role) {
        MyValidator.nullThrowsForProperty(role, "id", "roleName", "roleNameCn");
        log.debug("添加角色{}", role);
        return roleRepo.insert(role);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "'roleList'")})
    public int batchInsert(List<Role> roles) {
        MyValidator.emptyThrows(roles);
        for (Role role : roles) {
            MyValidator.nullThrowsForProperty(role, "id", "roleName", "roleNameCn");
        }
        log.debug("批量添加角色{}", roles);
        return roleRepo.batchInsert(roles);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#role.id"), @CacheEvict(key = "'roleList'"),
            @CacheEvict(cacheNames = {"accountCache"}, allEntries = true)})
    public int delete(Role role) {
        Args.notNull(role);
        String id = role.getId();
        String modifier = role.getModifier();
        LocalDateTime modifiedTime = role.getModifiedTime();
        MyValidator.nullThrows(id, modifier, modifiedTime);
        log.debug("准备删除角色{}，删除操作发起人{}", id, modifier);
        log.debug("移除角色{}关联的所有权限", id);
        roleRepo.revokeAllPerms(id);
        log.debug("移除角色{}关联的所有账户", id);
        roleRepo.detachAssociateAccount(id);
        log.debug("删除角色{}", id);
        return roleRepo.deleteRole(id, modifier, modifiedTime);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#role.id"), @CacheEvict(key = "'roleList'"),
            @CacheEvict(cacheNames = {"accountCache"}, allEntries = true)})
    public int update(Role role) {
        Args.notNull(role);
        String id = role.getId();
        String modifier = role.getModifier();
        LocalDateTime modifiedTime = role.getModifiedTime();
        MyValidator.nullThrows(id, modifier, modifiedTime);
        log.debug("更新角色{}，更新操作发起人{}", id, modifier);
        return roleRepo.update(role);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#role.id"), @CacheEvict(key = "'roleList'")})
    public int activate(Role role) {
        Args.notNull(role);
        String id = role.getId();
        String modifier = role.getModifier();
        LocalDateTime modifiedTime = role.getModifiedTime();
        MyValidator.nullThrows(id, modifier, modifiedTime);
        log.debug("激活{}角色，激活操作发起人：{}", id, modifier);
        return roleRepo.activate(id, modifier, modifiedTime);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#role.id"), @CacheEvict(key = "'roleList'"),
            @CacheEvict(cacheNames = {"accountCache"}, allEntries = true)})
    public int inactivate(Role role) {
        Args.notNull(role);
        String id = role.getId();
        String modifier = role.getModifier();
        LocalDateTime modifiedTime = role.getModifiedTime();
        MyValidator.nullThrows(id, modifier, modifiedTime);
        log.debug("准备使角色{}失活，失活操作发起人：{}", id, modifier);
        log.debug("移除角色{}关联的所有权限", id);
        roleRepo.revokeAllPerms(id);
/*
        log.debug("移除角色{}关联的所有账户", id);
        roleRepo.detachAssociateAccount(id);
        log.debug("使角色{}失活", id);
*/
        return roleRepo.inactivate(id, modifier, modifiedTime);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#roleId"), @CacheEvict(key = "'roleList'")})
    public int grantPerm(String roleId, String firstPerm, String... morePerms) {
        MyValidator.nullThrows(roleId, firstPerm);
        log.debug("授予角色{}权限{}，{}", roleId, firstPerm, Arrays.toString(morePerms));
        return roleRepo.grantPerm(roleId, firstPerm, morePerms);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#role.id"), @CacheEvict(key = "'roleList'")})
    public int grantPerm(Role role, String firstPerm, String... morePerms) {
        Args.notNull(role);
        String id = role.getId();
        log.debug("获取当前 Aop 对 RoleService 的代理");
        RoleService proxy = ((RoleService) AopContext.currentProxy());
        return proxy.grantPerm(id, firstPerm, morePerms);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#roleId"), @CacheEvict(key = "'roleList'")})
    public int grantPerm(String roleId, List<String> permIdList) {
        Args.notNull(roleId);
        MyValidator.emptyThrows(permIdList);
        log.debug("授予角色{}权限{}", roleId, permIdList);
        return roleRepo.grantPermList(roleId, permIdList);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#role.id"), @CacheEvict(key = "'roleList'")})
    public int grantPerm(Role role, List<String> permIdList) {
        Args.notNull(role);
        log.debug("获取当前 Aop 对 RoleService 的代理");
        RoleService proxy = ((RoleService) AopContext.currentProxy());
        return proxy.grantPerm(role.getId(), permIdList);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#roleId"), @CacheEvict(key = "'roleList'"),
            @CacheEvict(cacheNames = {"accountCache"}, allEntries = true)})
    public int revokeAllPerms(String roleId) {
        Args.notNull(roleId);
        log.debug("清空角色{}拥有的所有权限", roleId);
        roleRepo.revokeAllPerms(roleId);
        return roleRepo.revokeAllPerms(roleId);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#roleId"), @CacheEvict(key = "'roleList'"),
            @CacheEvict(cacheNames = {"accountCache"}, allEntries = true)})
    public int detachAssociateAccount(String roleId) {
        Args.notNull(roleId);
        log.debug("撤销角色{}关联的所有用户", roleId);
        return roleRepo.detachAssociateAccount(roleId);
    }

    @Override
    @Transactional(readOnly = true)
    @Caching(cacheable = {@Cacheable(key = "#id")})
    public Role selectById(String id) {
        Args.notNull(id);
        log.debug("查询id为{}的角色信息", id);
        return roleRepo.selectById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Caching(put = {@CachePut(key = "#id")}, evict = {@CacheEvict(key = "'roleList'")})
    public Role forcedSelectById(String id) {
        Args.notNull(id);
        log.debug("强制刷新id为{}的角色信息", id);
        return roleRepo.selectById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getPermIdList(String roleId) {
        return roleRepo.getPermIdList(roleId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getCategoryPermId(String roleId) {
        return roleRepo.getPermIdWithType(roleId, PermType.MENU);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Role> page(PageRequest pageRequest) {
        RoleService proxy = (RoleService) AopContext.currentProxy();
        return proxy.page(null, pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Role> page(String name, PageRequest pageRequest) {
        MyValidator.nullThrows(pageRequest);
        int pageOffset = pageRequest.getPageOffset();
        int pageSize = pageRequest.getPageSize();
        String nameLike = Global.toLike(name);
        long total = roleRepo.total(nameLike);
        if (total == 0) {
            log.debug("没有任何名称中包含{}的记录", name);
            return new Page.Builder<>(pageRequest, total, emptyRoleList).create();
        }
        log.debug("名称中包含{}的角色共有{}条记录", name, total);
        int skip = pageOffset * pageSize;
        if (skip >= total) {
            log.debug("查询所跳过的记录数{}大于等于实际总记录数{}", skip, total);
            return new Page.Builder<>(pageRequest, total, emptyRoleList).create();
        }
        log.debug("查询名称中包含{}，第{}页（每页{}条记录）的记录", name, pageOffset + 1, pageSize);
        List<Role> content = roleRepo.page(skip, pageSize, nameLike);

/*
        if (CollectionUtils.notEmpty(content)) {
            RoleService proxy = (RoleService) AopContext.currentProxy();
            log.debug("拼接角色所关联到的权限ID列表");
            for (Role role : content) {
                log.debug("获取角色{}的权限ID列表", role.getId());
                role.setPermIdList(proxy.getPermIdList(role.getId()));
            }
        }
*/
        return new Page.Builder<>(pageRequest, total, content).create();
    }

    @Override
    @Transactional(readOnly = true)
    @Caching(cacheable = {@Cacheable(key = "'roleList'")})
    public List<Role> selectAll() {
        log.debug("查询所有的角色信息");
        return roleRepo.selectAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAssociateWithAccount(String id) {
        Args.notNull(id);
        log.debug("查询角色{}是否正被账户所使用", id);
        return roleRepo.isAssociateWithAccount(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAssociateWithPerm(String id) {
        Args.notNull(id);
        log.debug("查询角色{}是否正分配有权限", id);
        return roleRepo.isAssociateWithPerm(id);
    }

}
