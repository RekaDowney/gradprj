package me.junbin.gradprj.domain.relation.exception;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/1/31 23:26
 * @description : 存储 {@link me.junbin.gradprj.domain.relation.Relation}
 * 的集合 {@link java.util.Collection} 或者 {@link java.util.Map} 如果为 {@code null}
 * 或者 {@code Collection.isEmpty() || Map.isEmpty()} 为 {@code true} 将抛出这个异常
 */
public class EmptyRelationsException extends RelationException {

    public EmptyRelationsException() {
    }

    public EmptyRelationsException(String message) {
        super(message);
    }

    public EmptyRelationsException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyRelationsException(Throwable cause) {
        super(cause);
    }

}
