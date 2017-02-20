package me.junbin.gradprj.repository;


import me.junbin.gradprj.domain.BaseDomain;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/1 13:12
 * @description :
 */
public interface BaseRepo<DOMAIN extends BaseDomain, ID extends Serializable> {

    int insert(DOMAIN domain);

    int batchInsert(@Param("domainList") List<DOMAIN> domainList);

    int delete(ID id);

    int update(DOMAIN domain);

    DOMAIN selectById(ID id);

    List<DOMAIN> selectAll();

}
