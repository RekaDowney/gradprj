package me.junbin.gradprj.service;

import me.junbin.gradprj.domain.Photo;
import me.junbin.gradprj.domain.User;

import java.util.List;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/16 12:47
 * @description :
 */
public interface UserService {

    int update(User user);

    int updatePortrait(String userId, String photoId);

    int updatePortrait(User user, Photo photo);

    User selectById(String id);

    List<User> selectAll();

}
