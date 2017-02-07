package me.junbin.gradprj.domain.relation.exception;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/1 0:04
 * @description : 如果某个 {@link me.junbin.gradprj.domain.relation.Relation}
 * 没有落在 {@code Relation} 的集合 {@link java.util.Collection} 或者 {@link java.util.Map}
 * 中，那么将会抛出这个异常
 */
public class NoSuchItemInRelationsException extends RelationException {

    public NoSuchItemInRelationsException() {
    }

    public NoSuchItemInRelationsException(String message) {
        super(message);
    }

    public NoSuchItemInRelationsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchItemInRelationsException(Throwable cause) {
        super(cause);
    }

}
