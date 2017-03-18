package me.junbin.gradprj.domain.relation;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/1/31 21:34
 * @description : Relation 必须实现 {@code equals(Object)} 和 {@code hashCode} 方法，
 * 因为它将会在 {@link java.util.Map} 中作为 Key 出现
 */
public interface Relation
        <R extends Relation<R, ID>,
                ID extends Serializable>
        extends OnlyParentRelation<ID> {

    /**
     * 得到所有子级关系
     */
    List<R> getSub();

    /**
     * 设置子级关系
     *
     * @param subRelations 子级关系
     */
    void setSub(List<R> subRelations);

    int getWeight();

    boolean equals(Object o);

    int hashCode();

    Comparator<Relation> WEIGHT_COMPARATOR = Comparator.comparing(Relation::getWeight);

}
