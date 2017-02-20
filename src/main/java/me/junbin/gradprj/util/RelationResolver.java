package me.junbin.gradprj.util;

import me.junbin.commons.util.Args;
import me.junbin.commons.util.CollectionUtils;
import me.junbin.gradprj.domain.relation.Relation;
import me.junbin.gradprj.domain.relation.exception.EmptyRelationsException;
import me.junbin.gradprj.domain.relation.exception.NoSuchItemInRelationsException;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/1/31 21:50
 * @description : 关系解析器
 */
public class RelationResolver {

    public static <R extends Relation<R, ID>, ID extends Serializable> List<ID> idList(final Collection<R> relations) {
        Args.notNull(relations);
        return relations.stream()
                        .parallel()
                        .map(Relation::getId)
                        .collect(Collectors.toList());
    }

    public static <R extends Relation<R, ID>, ID extends Serializable> List<ID> idList(final Map<R, R> subParentMap) {
        Args.notNull(subParentMap);
        return subParentMap.keySet().stream()
                           .parallel()
                           .map(Relation::getId)
                           .collect(Collectors.toList());
    }

    /**
     * 根据关系集合构造父子关系 Map
     *
     * @param relations {@link Relation} 的实现子类的集合，用于构造父子关系的 {@link Map}
     * @param <R>       泛型 R， {@link Relation} 的实现子类
     * @param <ID>      泛型 ID， {@link Serializable} 的实现子类
     * @return 存在父子关系的 {@link Map}，其中 K 为子，V 为父，当 {@code V == null}
     * 时，表示当前的 K 就是最终父类
     */
    public static <R extends Relation<R, ID>, ID extends Serializable> Map<R, R> constructSubParentMap(final Collection<R> relations) {
        // 如果指定的关系集为空，那么直接返回一个空的 HashMap
        if (CollectionUtils.isEmpty(relations)) {
            return new HashMap<>();
        }

        Map<ID, R> idIdentityMap = new HashMap<>(relations.size());
        for (R t : relations) {
            idIdentityMap.put(t.getId(), t);
        }

        Map<R, R> result = new HashMap<>();
        ID parentId;
        R parent;
        for (R t : relations) {
            parentId = t.getParentId();
            if (parentId == null) {
                result.put(t, null);
            } else {
                // 必须确保关系集中存在该父级关系
                parent = idIdentityMap.get(parentId);
                if (parent == null) {
                    throw new NoSuchItemInRelationsException(String.format("关系集{%s}中不存在该ID{%s}的关系", idList(relations), parentId));
                }
/*
                if (!idIdentityMap.containsKey(parentId)) {
                    throw new NoSuchItemInRelationsException(String.format("关系集{%s}中不存在该ID{%s}的关系", idList(relations), parentId));
                }
*/
                result.put(t, parent);
            }
        }
        return result;
    }

    /**
     * 从指定关系集中获取所有最终父级关系集，即不存在父级关系的所有关系。
     *
     * @param relations 关系集
     * @param <R>       泛型 R， {@link Relation} 的实现子类
     * @param <ID>      泛型 ID， {@link Serializable} 的实现子类
     * @return 最终父级关系集
     */
    public static <R extends Relation<R, ID>, ID extends Serializable> List<R> allFinalParent(final List<R> relations) {
        return allFinalParent(constructSubParentMap(relations));
    }

    /**
     * 从指定父子关系 Map 中获取所有最终父级关系集，即不存在父级关系的所有关系
     *
     * @param subParentMap 父子关系 Map
     * @param <R>          泛型 R， {@link Relation} 的实现子类
     * @param <ID>         泛型 ID， {@link Serializable} 的实现子类
     * @return 最终父级关系集
     */
    public static <R extends Relation<R, ID>, ID extends Serializable> List<R> allFinalParent(final Map<R, R> subParentMap) {
        if (CollectionUtils.isEmpty(subParentMap)) {
            return new ArrayList<>();
        }
        return subParentMap.entrySet().stream()
                           .parallel()
                           .filter(entry -> entry.getValue() == null)
                           .map(Map.Entry::getKey)
                           .collect(Collectors.toList());
    }

    /**
     * 从指定关系集中获取指定关系的最终父级关系
     *
     * @param relations 关系集
     * @param current   指定关系
     * @param <R>       泛型 R， {@link Relation} 的实现子类
     * @param <ID>      泛型 ID， {@link Serializable} 的实现子类
     * @return 指定关系的最终父级关系
     */
    public static <R extends Relation<R, ID>, ID extends Serializable> R finalParent(final Collection<R> relations, final R current) {
        Map<R, R> subParentMap = constructSubParentMap(relations);
        return finalParent(subParentMap, current);
    }

    /**
     * 从指定父子关系 Map 中获取指定关系的最终父级关系
     *
     * @param subParentMap 父子关系 Map
     * @param current      指定关系
     * @param <R>          泛型 R， {@link Relation} 的实现子类
     * @param <ID>         泛型 ID， {@link Serializable} 的实现子类
     * @return 指定关系的最终父级关系
     */
    public static <R extends Relation<R, ID>, ID extends Serializable> R finalParent(final Map<R, R> subParentMap, final R current) {
        Args.notNull(current);

        if (current.getParentId() == null) {
            return current;
        }

        // 先获取直接父级关系，此时的直接父级关系必然不为 null，因为前面已经判断过了
        R directParent = parent(subParentMap, current);

        R result = null;
        while (directParent != null) {
            result = directParent;
            directParent = subParentMap.get(directParent);
        }
        assert result != null;
        return result;
    }

    /**
     * 从指定关系集中获取指定关系的直接父级关系
     *
     * @param relations 关系集
     * @param current   指定关系
     * @param <R>       泛型 R， {@link Relation} 的实现子类
     * @param <ID>      泛型 ID， {@link Serializable} 的实现子类
     * @return 直接父级关系
     */
    public static <R extends Relation<R, ID>, ID extends Serializable> R parent(final List<R> relations, final R current) {
        return parent(constructSubParentMap(relations), current);
    }

    /**
     * 从指定父子关系 Map 中获取指定关系的直接父级关系
     *
     * @param subParentMap 父子关系 Map
     * @param current      指定关系
     * @param <R>          泛型 R， {@link Relation} 的实现子类
     * @param <ID>         泛型 ID， {@link Serializable} 的实现子类
     * @return 直接父级关系
     */
    public static <R extends Relation<R, ID>, ID extends Serializable> R parent(final Map<R, R> subParentMap, final R current) {
        Args.notNull(current);

        if (CollectionUtils.isEmpty(subParentMap)) {
            throw new EmptyRelationsException("空关系集无法进行父子关系检索");
        }
        if (!subParentMap.containsKey(current)) {
            throw new NoSuchItemInRelationsException(String.format("关系集{%s}中不存在该ID{%s}的关系", idList(subParentMap), current.getId()));
        }
        return subParentMap.get(current);
    }

    /**
     * 从指定关系集中获取指定关系的所有直属子级关系集，即指定关系的所有直接子级关系
     *
     * @param relations 关系集
     * @param current   指定关系
     * @param <R>       泛型 R， {@link Relation} 的实现子类
     * @param <ID>      泛型 ID， {@link Serializable} 的实现子类
     * @return 指定关系的子级关系集
     */
    public static <R extends Relation<R, ID>, ID extends Serializable> List<R> sub(final List<R> relations, final R current) {
        return sub(constructSubParentMap(relations), current);
    }

    /**
     * 从指定父子关系 Map 中获取指定关系的所有直属子级关系集，即指定关系的所有直接子级关系
     *
     * @param subParentMap 父子关系 Map
     * @param current      指定关系
     * @param <R>          泛型 R， {@link Relation} 的实现子类
     * @param <ID>         泛型 ID， {@link Serializable} 的实现子类
     * @return 指定关系的子级关系集
     */
    public static <R extends Relation<R, ID>, ID extends Serializable> List<R> sub(final Map<R, R> subParentMap, final R current) {
        Args.notNull(current);

        // 如果父子关系 Map 为空，那么直接返回空的直接子级关系集
        if (CollectionUtils.isEmpty(subParentMap)) {
            return new ArrayList<>();
        }
        return subParentMap.entrySet().stream()
                           .filter(entry -> Objects.equals(current, entry.getValue()))
                           .map(Map.Entry::getKey)
                           .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

}
