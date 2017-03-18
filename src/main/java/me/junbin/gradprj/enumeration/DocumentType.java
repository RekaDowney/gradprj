package me.junbin.gradprj.enumeration;

import com.zhuozhengsoft.pageoffice.OpenModeType;
import me.junbin.commons.util.Args;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.zhuozhengsoft.pageoffice.OpenModeType.*;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/27 22:15
 * @description :
 */
public enum DocumentType implements CommonEnum<DocumentType>, DocumentOp {

    WORD("Word", "docx", "doc") {
        @Override
        public OpenModeType readOnly() {
            return docReadOnly;
        }

        @Override
        public OpenModeType normalEdit() {
            return docNormalEdit;
        }
    },

    EXCEL("Excel", "xlsx", "xls") {
        @Override
        public OpenModeType readOnly() {
            return xlsReadOnly;
        }

        @Override
        public OpenModeType normalEdit() {
            return xlsNormalEdit;
        }
    },

    POWERPOINT("Powerpoint", "pptx", "ppt") {
        @Override
        public OpenModeType readOnly() {
            return pptReadOnly;
        }

        @Override
        public OpenModeType normalEdit() {
            return pptNormalEdit;
        }
    },

    PDF("Pdf", "pdf") {
        @Override
        @Deprecated
        public OpenModeType readOnly() {
            throw new UnsupportedOperationException("Please use com.zhuozhengsoft.pageoffice.PDFCtrl to operate pdf file");
        }

        @Override
        @Deprecated
        public OpenModeType normalEdit() {
            throw new UnsupportedOperationException("Please use com.zhuozhengsoft.pageoffice.PDFCtrl to operate pdf file");
        }
    };

    private final String name;
    private final String[] aliases;
    private static final Map<String, DocumentType> cache = new HashMap<>();

    static {
        for (DocumentType type : DocumentType.values()) {
            cache.put(type.name.toLowerCase(), type);
            for (String alias : type.aliases) {
                cache.put(alias, type);
            }
        }
    }

    DocumentType(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public static DocumentType parse(final String docType) {
        String notNullDocType = Args.notNull(docType).trim().toLowerCase();
        DocumentType type = cache.get(notNullDocType);
        if (type == null) {
            throw new NoSuchElementException(docType);
        }
        return type;
    }

    @Override
    public String convert() {
        return this.name;
    }

    @Override
    public DocumentType recover(String data) {
        return parse(data);
    }

}
