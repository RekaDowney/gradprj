package me.junbin.gradprj.util;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.Sha384Hash;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/1 19:29
 * @description : 常用哈希加密工具类
 */
public abstract class EncryptUtils {

    private static final int DEFAULT_HASH_ITERATIONS = 2;

    public static String md5Encrypt(final String text, final String salt) {
        Md5Hash md5Hash = new Md5Hash(text);
        md5Hash = new Md5Hash(md5Hash.toHex(), salt, DEFAULT_HASH_ITERATIONS);
        return md5Hash.toHex();
    }

    /**
     * @deprecated 不建议使用 Sha1 加密
     */
    public static String sha1Encrypt(final String text, final String salt) {
        Sha1Hash sha1Hash = new Sha1Hash(text);
        sha1Hash = new Sha1Hash(sha1Hash.toHex(), salt, DEFAULT_HASH_ITERATIONS);
        return sha1Hash.toHex();
    }

    public static String sha256Encrypt(final String text, final String salt) {
        Sha256Hash sha256Hash = new Sha256Hash(text);
        sha256Hash = new Sha256Hash(sha256Hash.toHex(), salt, DEFAULT_HASH_ITERATIONS);
        return sha256Hash.toHex();
    }

    public static String sha384Encrypt(final String text, final String salt) {
        Sha384Hash sha384Hash = new Sha384Hash(text);
        sha384Hash = new Sha384Hash(sha384Hash.toHex(), salt, DEFAULT_HASH_ITERATIONS);
        return sha384Hash.toHex();
    }

}
