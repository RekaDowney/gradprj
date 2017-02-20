package me.junbin.gradprj.repository;

import me.junbin.gradprj.annotation.MyBatisMapper;
import me.junbin.gradprj.domain.Perm;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/14 20:47
 * @description :
 */
@MyBatisMapper
public interface PermRepo extends BaseRepo<Perm, String> {

    /**
     * @deprecated 请使用 {@link #deletePerm(String, String, LocalDateTime)}
     */
    @Override
    @Deprecated
    int delete(String id);

    int deletePerm(@Param("id") String id,
                   @Param("modifier") String modifier,
                   @Param("modifiedTime") LocalDateTime modifiedTime);

    int activate(@Param("id") String id,
                 @Param("modifier") String modifier,
                 @Param("modifiedTime") LocalDateTime modifiedTime);

    int inactivate(@Param("id") String id,
                   @Param("modifier") String modifier,
                   @Param("modifiedTime") LocalDateTime modifiedTime);

    boolean isInUse(String id);
}
