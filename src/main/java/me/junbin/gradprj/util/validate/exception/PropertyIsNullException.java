package me.junbin.gradprj.util.validate.exception;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/16 19:51
 * @description :
 */
public class PropertyIsNullException extends ValidationException {

    public PropertyIsNullException() {
    }

    public PropertyIsNullException(String message) {
        super(message);
    }

    public PropertyIsNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyIsNullException(Throwable cause) {
        super(cause);
    }

}
