package me.junbin.gradprj.domain.relation.exception;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/1 13:05
 * @description : 关系处理的最终异常类
 */
public class RelationException extends RuntimeException {

    public RelationException() {
    }

    public RelationException(String message) {
        super(message);
    }

    public RelationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RelationException(Throwable cause) {
        super(cause);
    }

}
