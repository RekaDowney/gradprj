package me.junbin.gradprj.service;

import me.junbin.gradprj.domain.Photo;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/16 21:43
 * @description :
 */
public interface PhotoService extends BaseService<Photo, String> {

    int delete(String id);

    int delete(Photo photo);

    int update(Photo photo);

}
