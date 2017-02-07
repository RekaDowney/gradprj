package me.junbin.gradprj.domain.relation;

import java.io.Serializable;
import java.util.List;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/1/31 21:35
 * @description : 对子级关系进行空实现
 */
public abstract class AbstractRelation
        <R extends Relation<R, ID>,
                ID extends Serializable>
        implements Relation<R, ID> {

    @Override
    public List<R> getSub() {
        return null;
    }

    @Override
    public void setSub(List<R> subRelations) {
    }

}
