package jgravatar;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/14 19:06
 * @description :
 */
public class GravatarDownloadException extends RuntimeException {

    public GravatarDownloadException(Throwable cause) {
        super("Gravatar could not be downloaded: " + cause.getMessage(), cause);
    }

}