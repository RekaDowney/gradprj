package me.junbin.gradprj.service.impl;

import me.junbin.commons.util.Args;
import me.junbin.gradprj.domain.Photo;
import me.junbin.gradprj.domain.User;
import me.junbin.gradprj.repository.UserRepo;
import me.junbin.gradprj.service.UserService;
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

import java.util.List;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/16 12:48
 * @description : 更新 User 的时候必须同时清除 Account 的缓存
 */
@Service("userService")
@CacheConfig(cacheNames = "userCache")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#user.id"),
            @CacheEvict(key = "'userList'"),
            @CacheEvict(cacheNames = {"accountCache"}, key = "#user.id"),
            @CacheEvict(cacheNames = {"accountCache"}, key = "'accountList'")})
    public int update(User user) {
        Args.notNull(user);
        Args.notEmpty(user.getId());
        log.debug("更新id为{}的用户信息{}", user.getId(), user);
        return userRepo.update(user);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#userId"),
            @CacheEvict(key = "'userList'"),
            @CacheEvict(cacheNames = {"accountCache"}, key = "#userId"),
            @CacheEvict(cacheNames = {"accountCache"}, key = "'accountList'")})
    public int updatePortrait(String userId, String photoId) {
        Args.notEmpty(userId);
        Args.notEmpty(photoId);
        log.debug("更新id为{}的用户头像[新头像id：{}]", userId, photoId);
        return userRepo.updatePortrait(userId, photoId);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(key = "#user.id"),
            @CacheEvict(key = "'userList'"),
            @CacheEvict(cacheNames = {"accountCache"}, key = "#user.id"),
            @CacheEvict(cacheNames = {"accountCache"}, key = "'accountList'")})
    public int updatePortrait(User user, Photo photo) {
        MyValidator.nullThrows(user, "id");
        MyValidator.nullThrows(photo, "id");
        String userId = user.getId();
        String photoId = photo.getId();
        log.debug("更新id为{}的用户头像[新头像id：{}]", userId, photoId);
        return userRepo.updatePortrait(userId, photoId);
    }

    @Override
    @Transactional(readOnly = true)
    @Caching(cacheable = {@Cacheable(key = "#id")})
    public User selectById(String id) {
        Args.notEmpty(id);
        log.debug("查询id为{}的用户", id);
        return userRepo.selectById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Caching(cacheable = {@Cacheable(key = "'userList'")})
    public List<User> selectAll() {
        log.debug("查询所有的用户信息");
        return userRepo.selectAll();
    }

}
