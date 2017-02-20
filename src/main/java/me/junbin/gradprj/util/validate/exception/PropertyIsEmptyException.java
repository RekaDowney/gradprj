package me.junbin.gradprj.util.validate.exception;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/16 19:59
 * @description :
 */
public class PropertyIsEmptyException extends PropertyIsNullException {

    public PropertyIsEmptyException() {
    }

    public PropertyIsEmptyException(String message) {
        super(message);
    }

    public PropertyIsEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyIsEmptyException(Throwable cause) {
        super(cause);
    }

}
