package me.junbin.gradprj.util.validate.exception;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/16 19:34
 * @description :
 */
public class PropertyNotExistsException extends ValidationException {

    public PropertyNotExistsException() {
    }

    public PropertyNotExistsException(String message) {
        super(message);
    }

    public PropertyNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyNotExistsException(Throwable cause) {
        super(cause);
    }

}
