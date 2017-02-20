package me.junbin.gradprj.repository;

import me.junbin.gradprj.annotation.MyBatisMapper;
import me.junbin.gradprj.domain.Role;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/14 20:46
 * @description :
 */
@MyBatisMapper
public interface RoleRepo extends BaseRepo<Role, String> {

    /**
     * @deprecated 请使用 {@link #deleteRole(String, String, LocalDateTime)}
     */
    @Override
    @Deprecated
    int delete(String id);

    int deleteRole(@Param("id") String id,
                   @Param("modifier") String modifier,
                   @Param("modifiedTime") LocalDateTime modifiedTime);

    int activate(@Param("id") String id,
                 @Param("modifier") String modifier,
                 @Param("modifiedTime") LocalDateTime modifiedTime);

    int inactivate(@Param("id") String id,
                   @Param("modifier") String modifier,
                   @Param("modifiedTime") LocalDateTime modifiedTime);

    Role selectByRoleName(String roleName);

    /**
     * 授予指定角色指定权限
     *
     * @param roleId    角色 ID
     * @param firstPerm 权限 ID，不允许为 {@code null}
     * @param morePerms 权限 ID 变长数组，即使不传递参数过来依然不为 {@code null}，默认为 []
     * @return 授予的权限条目，正常情况下等于 {@code morePerms.length + 1}
     */
    int grantPerm(@Param("roleId") String roleId,
                  @Param("first") String firstPerm,
                  @Param("more") String... morePerms);

    /**
     * 授予指定角色指定权限
     *
     * @param roleId     角色 ID
     * @param permIdList 权限 ID 列表，长度必须大于 1
     * @return 授予的权限条目，正常情况下等于 {@code permIdList.size()}
     */
    int grantPermList(@Param("roleId") String roleId,
                      @Param("permIdList") List<String> permIdList);

    /**
     * 撤销指定角色的指定权限
     *
     * @param roleId    角色 ID
     * @param firstPerm 权限 ID，不允许为 {@code null}
     * @param morePerms 权限 ID 变长数组，即使不传递参数过来依然不为 {@code null}，默认为 []
     * @return 撤销的权限条目，正常情况下等于 {@code morePerms.length + 1}
     */
    int revokePerm(@Param("roleId") String roleId,
                   @Param("first") String firstPerm,
                   @Param("more") String... morePerms);

    /**
     * 撤销指定角色的所有权限
     *
     * @param roleId 角色 ID
     * @return 撤销的权限条目
     */
    int revokeAllPerms(@Param("roleId") String roleId);

    int detachAssociateAccount(@Param("roleId") String roleId);

    boolean isAssociateWithAccount(String id);

    boolean isAssociateWithPerm(String id);

}
