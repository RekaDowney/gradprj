package me.junbin.gradprj.util.validate.exception;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/16 21:12
 * @description :
 */
public class ContainerIsEmptyException extends ValidationException {

    public ContainerIsEmptyException() {
    }

    public ContainerIsEmptyException(String message) {
        super(message);
    }

    public ContainerIsEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContainerIsEmptyException(Throwable cause) {
        super(cause);
    }

}
