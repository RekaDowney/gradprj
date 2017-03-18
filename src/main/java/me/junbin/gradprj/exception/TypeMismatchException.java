package me.junbin.gradprj.exception;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/13 19:54
 * @description : 当访问链接可解析的文档类型与实际文件类型不一致时抛出本异常。
 * 例如：链接 /{*}/{docId}/pdf/{*} 只处理 PDF 文件，但实际 docId 所指向的文档为 Office 文档时将抛出此异常
 */
public class TypeMismatchException extends IllegalStateException {

    public TypeMismatchException() {
    }

    public TypeMismatchException(String s) {
        super(s);
    }

    public TypeMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeMismatchException(Throwable cause) {
        super(cause);
    }

}
