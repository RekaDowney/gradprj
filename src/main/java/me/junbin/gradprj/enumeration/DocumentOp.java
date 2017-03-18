package me.junbin.gradprj.enumeration;

import com.zhuozhengsoft.pageoffice.OpenModeType;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/27 22:19
 * @description :
 */
public interface DocumentOp {

    OpenModeType readOnly();

    OpenModeType normalEdit();


}
