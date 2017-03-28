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
    private static final Path imageDir = Paths.get(Global.IMAGE_LOCATION);

    private static final String leaveDocDirName = "askForLeave";
    private static final String leaveDocPath = Global.DOC_LOCATION + Global.SLASH + leaveDocDirName;
    private static final Path leaveDocDir = Paths.get(leaveDocPath);

    private static final String templateDirName = "template";
    private static final String templateFilename = "template-dataTag.docx";
    private static final Path templatePath = Paths.get(Global.DOC_LOCATION, templateDirName, templateFilename);
    private static final String templateUri = templatePath.toUri().toString();

    public static Path getLeaveTemplatePath() {
        return templatePath;
    }

    public static String getLeaveTemplateUri() {
        return templateUri;
    }

    public static String getLeaveDocName(final String id) {
        return id + templateFilename.substring(templateFilename.lastIndexOf(Global.DOT));
    }

    public static void main(String[] args) {
        System.out.println(getLeaveDocName(Global.uuid()));
    }

    public static Path getActualLeaveDocPath(final String doc) {
        Args.notEmpty(doc);
        if (startWithSlash(doc)) {
            return Paths.get(leaveDocPath + doc);
        }
        return leaveDocDir.resolve(doc);
    }

    public static String getActualLeaveDocUri(final String doc) {
        return getActualLeaveDocPath(doc).toUri().toString();
    }

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

    public static Path getActualImagePath(final String image) {
        Args.notEmpty(image);
        if (startWithSlash(image)) {
            return Paths.get(Global.IMAGE_LOCATION + image);
        }
        return imageDir.resolve(image);
    }

    public static String getActualImageUri(final String image) {
        return getActualImagePath(image).toUri().toString();
    }

    private static boolean startWithSlash(final String docUrl) {
        return docUrl.charAt(0) == Global.SLASH;
    }

}
