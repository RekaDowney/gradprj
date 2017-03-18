package me.junbin.gradprj.util;

import me.junbin.commons.util.Args;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/1 8:35
 * @description :
 */
public abstract class DocumentUtils {

    private static final Path docDir = Paths.get(Global.DOC_LOCATION);

    public static String getDocMappingUrl(final String doc) {
        Args.notEmpty(doc);
        if (startWithSlash(doc)) {
            return Global.DOC_PREFIX_URL + doc;
        }
        return Global.DOC_PREFIX_URL + Global.SLASH + doc;
    }

    public static String simpleName(final String docName) {
        Args.notEmpty(docName);
        int idx = docName.lastIndexOf(Global.DOT);
        if (idx != -1) {
            return docName.substring(0, idx);
        }
        return docName;
    }

    public static Path getActualPath(final String doc) {
        Args.notEmpty(doc);
        if (startWithSlash(doc)) {
            return Paths.get(Global.DOC_LOCATION + doc);
        }
        return docDir.resolve(doc);
    }

    public static String getActualPathUri(final String doc) {
        return getActualPath(doc).toUri().toString();
    }

    private static boolean startWithSlash(final String docUrl) {
        return docUrl.charAt(0) == Global.SLASH;
    }

}
