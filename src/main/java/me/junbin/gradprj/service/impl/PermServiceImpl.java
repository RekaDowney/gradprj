package me.junbin.gradprj.service.impl;

import me.junbin.commons.util.Args;
import me.junbin.gradprj.domain.Perm;
import me.junbin.gradprj.repository.PermRepo;
import me.junbin.gradprj.service.PermService;
import me.junbin.gradprj.util.validate.MyValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/18 22:11
 * @description :
 * -- 权限被删除或者变更时，将会清空 roleCache 缓存的所有数据 --
 * 缓存处理不过界原则：缓存的数据处理应该尽可能不涉及到两个不同的缓存的管理
 */
@Service("permService")
@CacheConfig(cacheNames = "permCache")
public class PermServiceImpl implements PermService {

    @Autowired
    private PermRepo permRepo;
    private static final Logger log = LoggerFactory.getLogger(PermServiceImpl.class);

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "'permList'")})
    public int insert(Perm perm) {
        MyValidator.nullThrowsForProperty(perm, "permName", "permPattern", "permType");
        log.debug("添加新权限{}", perm);
        return permRepo.insert(perm);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "'permList'")})
    public int batchInsert(List<Perm> permList) {
        Args.notNull(permList);
        for (Perm perm : permList) {
            MyValidator.nullThrowsForProperty(perm, "permName", "permPattern", "permType");
        }
        log.debug("批量添加权限{}", permList);
        return permRepo.batchInsert(permList);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#perm.id"), @CacheEvict(key = "'permList'"),
            @CacheEvict(cacheNames = {"roleCache"}, allEntries = true)})
    public int delete(Perm perm) {
        Args.notNull(perm);
        String id = perm.getId();
        String modifier = perm.getModifier();
        LocalDateTime modifiedTime = perm.getModifiedTime();
        log.debug("准备删除权限：{}，删除操作发起人：{}", id, modifier);
        log.debug("撤销权限{}与角色的关联", id);
        permRepo.detachAssociatedRole(id);
        log.debug("删除权限：{}", id);
        return permRepo.deletePerm(id, modifier, modifiedTime);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#perm.id"), @CacheEvict(key = "'permList'"),
            @CacheEvict(cacheNames = {"roleCache"}, allEntries = true)})
    public int update(Perm perm) {
        Args.notNull(perm);
        String id = perm.getId();
        String modifier = perm.getModifier();
        LocalDateTime modifiedTime = perm.getModifiedTime();
        MyValidator.nullThrows(id, modifier, modifiedTime);
        log.debug("更新权限：{}，更新操作发起人：{}", id, modifier);
        return permRepo.update(perm);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#perm.id"), @CacheEvict(key = "'permList'")})
    public int activate(Perm perm) {
        Args.notNull(perm);
        String id = perm.getId();
        String modifier = perm.getModifier();
        LocalDateTime modifiedTime = perm.getModifiedTime();
        MyValidator.nullThrows(id, modifier, modifiedTime);
        log.debug("激活权限：{}，激活操作发起人：{}", id, modifier);
        return permRepo.activate(id, modifier, modifiedTime);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#perm.id"), @CacheEvict(key = "'permList'"),
            @CacheEvict(cacheNames = {"roleCache"}, allEntries = true)})
    public int inactivate(Perm perm) {
        Args.notNull(perm);
        String id = perm.getId();
        String modifier = perm.getModifier();
        LocalDateTime modifiedTime = perm.getModifiedTime();
        MyValidator.nullThrows(id, modifier, modifiedTime);
        log.debug("准备使权限：{}失活，失活操作发起人：{}", id, modifier);
        log.debug("撤销权限{}与角色的关联", id);
        permRepo.detachAssociatedRole(id);
        log.debug("使权限{}失活", id);
        return permRepo.inactivate(id, modifier, modifiedTime);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#id"), @CacheEvict(key = "'permList'"),
            @CacheEvict(cacheNames = {"roleCache"}, allEntries = true)})
    public int detachAssociatedRole(String id) {
        Args.notNull(id);
        log.debug("撤销权限{}与角色的关联", id);
        return permRepo.detachAssociatedRole(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAssociateWithRole(String id) {
        log.debug("查询权限{}是否正被角色所关联", id);
        return permRepo.isAssociateWithRole(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Caching(cacheable = {@Cacheable(key = "#id")})
    public Perm selectById(String id) {
        Args.notNull(id);
        log.debug("查询id为{}的权限信息", id);
        return permRepo.selectById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Caching(cacheable = {@Cacheable(key = "'permList'")})
    public List<Perm> selectAll() {
        log.debug("查询所有权限信息");
        return permRepo.selectAll();
    }

}
