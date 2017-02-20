package me.junbin.gradprj.util.validate.exception;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/16 21:13
 * @description :
 */
public class CollectionIsEmptyException extends ContainerIsEmptyException {

    public CollectionIsEmptyException() {
    }

    public CollectionIsEmptyException(String message) {
        super(message);
    }

    public CollectionIsEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public CollectionIsEmptyException(Throwable cause) {
        super(cause);
    }

}
