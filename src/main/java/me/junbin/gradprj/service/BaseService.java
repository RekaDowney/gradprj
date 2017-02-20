package me.junbin.gradprj.service;


import me.junbin.gradprj.domain.BaseDomain;

import java.io.Serializable;
import java.util.List;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/1 13:15
 * @description :
 */
public interface BaseService<DOMAIN extends BaseDomain, ID extends Serializable> {

    int insert(DOMAIN domain);

    int batchInsert(List<DOMAIN> domains);

/*
    int delete(ID id);

    int delete(DOMAIN domain);
*/

    DOMAIN selectById(ID id);

    List<DOMAIN> selectAll();

/*
    int update(DOMAIN domain);
*/

}
