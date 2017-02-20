package me.junbin.gradprj.exception;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/19 15:15
 * @description :
 */
public class RoleIsInUseException extends ServiceException {

    public RoleIsInUseException() {
    }

    public RoleIsInUseException(String message) {
        super(message);
    }

    public RoleIsInUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoleIsInUseException(Throwable cause) {
        super(cause);
    }

}
