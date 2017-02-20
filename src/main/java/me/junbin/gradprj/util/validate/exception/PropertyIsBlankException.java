package me.junbin.gradprj.util.validate.exception;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/16 20:00
 * @description :
 */
public class PropertyIsBlankException extends PropertyIsEmptyException {

    public PropertyIsBlankException() {
    }

    public PropertyIsBlankException(String message) {
        super(message);
    }

    public PropertyIsBlankException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyIsBlankException(Throwable cause) {
        super(cause);
    }

}
