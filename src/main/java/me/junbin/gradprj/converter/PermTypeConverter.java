package me.junbin.gradprj.converter;

import me.junbin.commons.converter.custom.CustomEnumConverter;
import me.junbin.gradprj.enumeration.PermType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/15 21:44
 * @description :
 */
@MappedTypes(PermType.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class PermTypeConverter extends CustomEnumConverter<PermType> {

    public PermTypeConverter() {
        super(PermType.FUNCTION);
    }

}
