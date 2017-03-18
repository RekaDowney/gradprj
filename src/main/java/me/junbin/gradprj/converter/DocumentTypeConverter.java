package me.junbin.gradprj.converter;

import me.junbin.gradprj.api.CommonEnumConverter;
import me.junbin.gradprj.enumeration.DocumentType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/28 8:28
 * @description :
 */
@MappedTypes(DocumentType.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class DocumentTypeConverter extends CommonEnumConverter<DocumentType> {

    public DocumentTypeConverter() {
        super(DocumentType.WORD);
    }

}
