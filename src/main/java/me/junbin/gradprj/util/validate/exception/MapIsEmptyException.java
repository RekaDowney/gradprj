package me.junbin.gradprj.util.validate.exception;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/16 21:13
 * @description :
 */
public class MapIsEmptyException extends ContainerIsEmptyException {

    public MapIsEmptyException() {
    }

    public MapIsEmptyException(String message) {
        super(message);
    }

    public MapIsEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapIsEmptyException(Throwable cause) {
        super(cause);
    }

}
