package me.junbin.gradprj.enumeration;

import me.junbin.commons.util.Args;
import me.junbin.gradprj.util.Global;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static me.junbin.gradprj.enumeration.DocumentType.*;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/1 8:35
 * @description :
 */
public enum DocSuffix {

    DOC(".doc", WORD),

    DOCX(".docx", WORD),

    XLS(".xls", EXCEL),

    XLSX(".xlsx", EXCEL),

    PPT(".ppt", POWERPOINT),

    PPTX(".pptx", POWERPOINT),

    PDF(".pdf", DocumentType.PDF);

    private final String suffix;
    private final DocumentType type;
    private static final Map<String, DocSuffix> suffixMap = new HashMap<>();

    DocSuffix(String suffix, DocumentType type) {
        this.suffix = suffix;
        this.type = type;
    }

    static {
        for (DocSuffix suffix : DocSuffix.values()) {
            suffixMap.put(suffix.getSuffix(), suffix);
        }
    }

    public String getSuffix() {
        return suffix;
    }

    public DocumentType getType() {
        return type;
    }

    public static DocSuffix of(String filename) {
        Args.notNull(filename);
        int idxOfLastDot = filename.lastIndexOf(Global.DOT);
        if (idxOfLastDot == -1) {
            throw new IllegalArgumentException(String.format("filename %s does not contain a dot character", filename));
        }
        String suffix = filename.substring(idxOfLastDot).toLowerCase();
        DocSuffix docSuffix = suffixMap.get(suffix);
        if (docSuffix == null) {
            throw new IllegalArgumentException(String.format("The filename suffix %s is not available of office document", suffix));
        }
        return docSuffix;
    }

    public static DocSuffix of(File file) {
        Args.notNull(file);
        return of(file.getName());
    }

    public static DocSuffix of(Path path) {
        Args.notNull(path);
        String filename = path.getFileName().toString();
        return of(filename);
    }

}
