package me.junbin.gradprj.enumeration;

import com.zhuozhengsoft.pageoffice.OpenModeType;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/27 22:17
 * @description :
 */
public enum WordOpenMode implements CommonEnum<WordOpenMode> {

    READ_ONLY(OpenModeType.docReadOnly),

    NORMAL_EDIT(OpenModeType.docNormalEdit);

    private final OpenModeType type;

    WordOpenMode(OpenModeType type) {
        this.type = type;
    }

    @Override
    public String convert() {
        return null;
    }

    @Override
    public WordOpenMode recover(String data) {
        return null;
    }

}
