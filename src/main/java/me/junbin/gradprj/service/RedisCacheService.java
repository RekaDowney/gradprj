package me.junbin.gradprj.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/22 14:36
 * @description :
 */
public interface RedisCacheService<K extends String, V extends Serializable> {

    /**
     * 缓存键值对，如果键名已经存在则不执行缓存操作（即不覆盖原来的键值对）；
     * 过期时间由 {@literal redis} 连接配置决定（默认永不过期）
     *
     * @param key   键
     * @param value 值
     * @return 如果之前不存在该键值对并且缓存成功则返回 {@code true}，否则返回 {@code false}
     */
    boolean putIfAbsent(K key, V value);

    /**
     * 缓存键值对，同时设置过期时间；如果键名已经存在则不执行缓存操作（即不覆盖原来的键值对）；
     * 当超过指定时长时自动删除该键值对
     *
     * @param key     键
     * @param value   值
     * @param timeout 有效时长
     * @param unit    有效时长单位
     * @return 如果之前不存在该键值对并且缓存成功则返回 {@code true}，否则返回 {@code false}
     */
    boolean putIfAbsent(K key, V value, long timeout, TimeUnit unit);

    /**
     * 删除指定键名的缓存键值对，如果键名在缓存中不存在，则不执行该键的删除操作
     *
     * @param key 要删除的键
     */
    void remove(K key);


    /**
     * 删除包含在 {@code keys} 中的所有缓存键值对，如果 {@code keys} 中的键名在缓存中不存在，则不执行该键的删除操作
     *
     * @param keys 要删除的键的集合
     */
    void remove(K... keys);


    /**
     * 删除包含在 {@code keys} 中的所有缓存键值对，如果 {@code keys} 中的键名在缓存中不存在，则不执行该键的删除操作
     *
     * @param keys 要删除的键的集合
     */
    void remove(Collection<K> keys);

    /**
     * 缓存键值对，如果键名已经存在则覆盖该键值对；过期时间由 {@literal redis} 连接配置决定（默认永不过期）
     *
     * @param key   键
     * @param value 值
     */
    void put(K key, V value);

    /**
     * 缓存键值对，同时设置过期时间；如果键名已经存在则覆盖该键值对；当超过指定时长时自动删除该键值对
     *
     * @param key     键
     * @param value   值
     * @param timeout 有效时长
     * @param unit    有效时长单位
     */
    void put(K key, V value, long timeout, TimeUnit unit);

    /**
     * 确认是否存在键名等于指定 {@code key} 的缓存键值对
     *
     * @param key 要检查的键名
     * @return 存在则返回 {@code true}，否则返回 {@code false}
     */
    boolean exists(K key);

    /**
     * 根据指定键名获取值，如果该缓存的键值对中不存在该键，那么返回 {@code null}
     *
     * @param key 键名
     * @return 如果键名存在，那么返回该键对应的缓存值，否则返回 {@code null}
     */
    V get(K key);

}
