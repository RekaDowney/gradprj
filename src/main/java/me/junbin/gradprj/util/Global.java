package me.junbin.gradprj.util;

import me.junbin.commons.prop.KVTranslator;
import me.junbin.commons.util.PathUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/1/23 22:36
 * @description :
 */
public enum Global {

    ;

    private static final KVTranslator kvTranslator;

    static {
        Path path = PathUtils.classpath("bundle/app.properties");
        try {
            kvTranslator = KVTranslator.properties(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String DOC_PREFIX_URL;
    public static final String DOC_LOCATION;
    public static final String IMAGE_PREFIX_URL;
    public static final String IMAGE_LOCATION;
    public static final String KAPTCHA_SESSION_KEY;
    public static final String LOGIN_ACCOUNT_KEY = "login_account";
    public static final String COOKIE_NAME = "shiroCookie";

    static {
        DOC_PREFIX_URL = kvTranslator.getAsString("doc.mapping.url");
        IMAGE_PREFIX_URL = kvTranslator.getAsString("image.mapping.url");
        KAPTCHA_SESSION_KEY = kvTranslator.getAsString("kaptcha.session.key");
        if (SystemUtils.IS_OS_WINDOWS) {
            DOC_LOCATION = kvTranslator.getAsString("windows.doc.mapping.location");
            IMAGE_LOCATION = kvTranslator.getAsString("windows.image.mapping.location");
        } else {
            DOC_LOCATION = kvTranslator.getAsString("linux.doc.mapping.location");
            IMAGE_LOCATION = kvTranslator.getAsString("linux.image.mapping.location");
        }
    }

    public static String normalize(String filePath) {
        if (filePath == null) {
            return null;
        }
        return Paths.get(filePath).normalize().toString();
    }

    public static String slashPath(String filePath) {
        if (filePath == null) {
            return null;
        }
        return Paths.get(filePath).normalize().toString().replaceAll("\\\\+", "/");
    }

    public static String imagePrefixUrl() {
        return IMAGE_PREFIX_URL;
    }

    public static String imageLocation() {
        return IMAGE_LOCATION;
    }

    public static String docPrefixUrl() {
        return DOC_PREFIX_URL;
    }

    public static String docLocation() {
        return DOC_LOCATION;
    }

    public static String captchaSessionKey() {
        return KAPTCHA_SESSION_KEY;
    }

    public static String loginAccountKey() {
        return LOGIN_ACCOUNT_KEY;
    }

    public static String cookieName() {
        return COOKIE_NAME;
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
