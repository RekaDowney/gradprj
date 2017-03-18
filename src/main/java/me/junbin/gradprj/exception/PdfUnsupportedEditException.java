package me.junbin.gradprj.exception;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/12 19:35
 * @description : 尝试编辑或者保存 PDF 文件时将会抛出本异常
 */
public class PdfUnsupportedEditException extends UnsupportedOperationException {

    public PdfUnsupportedEditException() {
    }

    public PdfUnsupportedEditException(String message) {
        super(message);
    }

    public PdfUnsupportedEditException(String message, Throwable cause) {
        super(message, cause);
    }

    public PdfUnsupportedEditException(Throwable cause) {
        super(cause);
    }

}
