package jgravatar;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/14 19:06
 * @description :
 */
public enum GravatarDefaultImage {

    GRAVATAR_ICON(""),

    IDENTICON("identicon"),

    MONSTERID("monsterid"),

    WAVATAR("wavatar"),

    HTTP_404("404");

    private String code;

    private GravatarDefaultImage(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
