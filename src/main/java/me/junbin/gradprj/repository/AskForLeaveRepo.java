package me.junbin.gradprj.repository;

import me.junbin.gradprj.annotation.MyBatisMapper;
import me.junbin.gradprj.domain.AskForLeave;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/28 20:18
 * @description :
 */
@MyBatisMapper
public interface AskForLeaveRepo extends BaseRepo<AskForLeave, String> {

    @Override
    @Deprecated
    int delete(String id);

    int deleteAfl(@Param("id") String id,
                  @Param("modifier") String modifier,
                  @Param("modifiedTime") LocalDateTime modifiedTime);

    long total(@Param("creator") String creator);

    List<AskForLeave> page(@Param("skip") int skip,
                        @Param("pageSize") int pageSize,
                        @Param("creator") String creator);

}
