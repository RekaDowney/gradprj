package me.junbin.gradprj.domain.relation;

import java.io.Serializable;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/1/31 21:39
 * @description :
 */
public interface OnlyParentRelation<ID extends Serializable> {

    /**
     * 获取当前关系的 ID
     *
     * @return 当前关系的 ID
     */
    ID getId();

    /**
     * 获取当前关系的直接父关系的 ID
     *
     * @return 当前关系的直接父关系的 ID
     */
    ID getParentId();

}
