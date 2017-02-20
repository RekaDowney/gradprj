package jgravatar;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/14 19:05
 * @description :
 */
public enum GravatarRating {

    GENERAL_AUDIENCES("g"),

    PARENTAL_GUIDANCE_SUGGESTED("pg"),

    RESTRICTED("r"),

    XPLICIT("x");

    private String code;

    private GravatarRating(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
