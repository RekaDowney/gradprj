package me.junbin.gradprj.exception;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/12 19:33
 * @description : 文档不存在或者文档已被删除，此时操作（读/写/下载）该文档都将抛出这个异常
 */
public class DocumentNotExistsException extends IllegalStateException {

    public DocumentNotExistsException() {
    }

    public DocumentNotExistsException(String s) {
        super(s);
    }

    public DocumentNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentNotExistsException(Throwable cause) {
        super(cause);
    }

}
