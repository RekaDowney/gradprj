package me.junbin.gradprj.repository;

import me.junbin.gradprj.annotation.MyBatisMapper;
import me.junbin.gradprj.domain.Photo;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/13 21:47
 * @description :
 */
@MyBatisMapper
public interface PhotoRepo extends BaseRepo<Photo, String> {

}
