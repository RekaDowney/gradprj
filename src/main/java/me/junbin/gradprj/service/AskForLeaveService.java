package me.junbin.gradprj.service;

import me.junbin.commons.page.Page;
import me.junbin.commons.page.PageRequest;
import me.junbin.gradprj.domain.AskForLeave;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/23 14:09
 * @description :
 */
public interface AskForLeaveService extends BaseService<AskForLeave, String> {

    Page<AskForLeave> page(String creator, PageRequest pageRequest);

}
