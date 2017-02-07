package me.junbin.gradprj.domain.relation;

import java.io.Serializable;
import java.util.List;

/**
 * @param <R>     关系
 * @param <ID>    关系的主键类型
 * @param <OWNER> 关系的持有者
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/1 11:51
 * @description :
 */
public interface RelationHolder
        <R extends Relation<R, ID>,
                ID extends Serializable,
                OWNER extends RelationHolder<R, ID, OWNER>> {

    /**
     * 获取关系持有者
     *
     * @return 关系持有者
     */
    OWNER identity();

    /**
     * 获取持有的关系集
     *
     * @return 持有的关系集
     */
    List<R> getRelations();

    /**
     * 设置持有的关系集
     *
     * @param relations 持有的关系集
     */
    void setRelations(List<R> relations);

}
