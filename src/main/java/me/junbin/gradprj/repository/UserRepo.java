package me.junbin.gradprj.repository;

import me.junbin.gradprj.annotation.MyBatisMapper;
import me.junbin.gradprj.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/13 22:04
 * @description :
 */
@MyBatisMapper
public interface UserRepo {

    int update(User user);

    int updatePortrait(@Param("userId") String userId, @Param("photoId") String photoId);

    User selectById(String id);

    List<User> selectAll();

}
