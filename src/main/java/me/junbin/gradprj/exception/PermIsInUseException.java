package me.junbin.gradprj.exception;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/19 15:16
 * @description :
 */
public class PermIsInUseException extends ServiceException {

    public PermIsInUseException() {
    }

    public PermIsInUseException(String message) {
        super(message);
    }

    public PermIsInUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermIsInUseException(Throwable cause) {
        super(cause);
    }

}
