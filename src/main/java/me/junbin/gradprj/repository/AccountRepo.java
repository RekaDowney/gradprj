package me.junbin.gradprj.repository;


import me.junbin.gradprj.annotation.MyBatisMapper;
import me.junbin.gradprj.domain.Account;
import me.junbin.gradprj.domain.Role;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/1 13:11
 * @description :
 */
@MyBatisMapper
public interface AccountRepo extends BaseRepo<Account, String> {

    /**
     * 请使用 {@link #deleteAccount(String, String, LocalDateTime)}
     */
    @Override
    @Deprecated
    int delete(String id);

    int deleteAccount(@Param("id") String id,
                      @Param("modifier") String modifier,
                      @Param("modifiedTime") LocalDateTime modifiedTime);

    int lock(@Param("id") String id,
             @Param("modifier") String modifier,
             @Param("modifiedTime") LocalDateTime modifiedTime);

    int unlock(@Param("id") String id,
               @Param("modifier") String modifier,
               @Param("modifiedTime") LocalDateTime modifiedTime);

    Account selectByPrincipal(String principal);

    /**
     * 授予指定账户指定角色
     *
     * @param accountId 账户 ID
     * @param firstRole 角色 ID，不允许为 {@code null}
     * @param moreRoles 角色 ID 变长数组，即使不传递参数过来依然不为 {@code null}，默认为 []
     * @return 授予的角色条目，正常情况下等于 {@code moreRoles.length + 1}
     */
    int grantRole(@Param("accountId") String accountId,
                  @Param("first") String firstRole,
                  @Param("more") String... moreRoles);

    /**
     * 授予指定账户指定角色
     *
     * @param accountId  账户 ID
     * @param roleIdList 角色 ID 列表，长度必须大于 1
     * @return 授予的角色条目，正常情况下等于 {@code roleIdList.size()}
     */
    int grantRoleList(@Param("accountId") String accountId,
                      @Param("roleIdList") List<String> roleIdList);

    /**
     * 撤销指定账户的指定角色
     *
     * @param accountId 账户 ID
     * @param firstRole 角色 ID，不允许为 {@code null}
     * @param moreRoles 角色 ID 变长数组，即使不传递参数过来依然不为 {@code null}，默认为 []
     * @return 撤销的角色条目，正常情况下等于 {@code moreRoles.length + 1}
     */
    int revokeRole(@Param("accountId") String accountId,
                   @Param("first") String firstRole,
                   @Param("more") String... moreRoles);

    /**
     * 撤销指定账户的所有角色
     *
     * @param accountId 账户 ID
     * @return 撤销的角色条目
     */
    int revokeAllRoles(@Param("accountId") String accountId);

    List<Role> acquireRoles(@Param("accountId") String accountId);

}
