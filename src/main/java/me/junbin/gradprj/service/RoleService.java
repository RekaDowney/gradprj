package me.junbin.gradprj.service;

import me.junbin.gradprj.domain.Perm;
import me.junbin.gradprj.domain.Role;

import java.util.List;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/18 21:52
 * @description :
 */
public interface RoleService extends BaseService<Role, String> {

    /**
     * 强制查询数据库并将数据放置到缓存中
     *
     * @param id id
     * @return 角色
     */
    Role forcedSelectById(String id);

    /**
     * @param role 角色信息，要求 {@link Role#id}，{@link Role#modifier} 和 {@link Role#modifiedTime} 必须非空
     * @return 删除操作所影响到的行数
     */
    int deleteRole(Role role);

    int update(Role role);

    /**
     * @param role 角色信息，要求 {@link Role#id}，{@link Role#modifier} 和 {@link Role#modifiedTime} 必须非空
     * @return 激活角色操作所影响到的行数
     */
    int activate(Role role);

    /**
     * @param role 角色信息，要求 {@link Role#id}，{@link Role#modifier} 和 {@link Role#modifiedTime} 必须非空
     * @return 激活角色操作所影响到的行数
     */
    int inactivate(Role role);

/*
    Role selectByRoleName(String roleName);
*/

    /**
     * 授予指定角色指定权限
     *
     * @param roleId    角色 ID
     * @param firstPerm 权限 ID，不允许为 {@code null}
     * @param morePerms 权限 ID 变长数组，即使不传递参数过来依然不为 {@code null}，默认为 []
     * @return 授予的权限条目，正常情况下等于 {@code morePerms.length + 1}
     */
    int grantPerm(String roleId, String firstPerm, String... morePerms);

    /**
     * 授予指定角色指定权限
     *
     * @param roleId     角色 ID
     * @param permIdList 权限 ID 列表，长度必须大于 1
     * @return 授予的权限条目，正常情况下等于 {@code permIdList.size()}
     */
    int grantPerm(String roleId, List<String> permIdList);

    int grantPerm(Role role, String firstPerm, String... morePerms);

    int grantPerm(Role role, List<String> permIdList);

    int grantPerm(Role role, Perm firstPerm, Perm... morePerms);

    /**
     * 撤销指定角色的指定权限
     *
     * @param roleId    角色 ID
     * @param firstPerm 权限 ID，不允许为 {@code null}
     * @param morePerms 权限 ID 变长数组，即使不传递参数过来依然不为 {@code null}，默认为 []
     * @return 撤销的权限条目，正常情况下等于 {@code morePerms.length + 1}
     */
    int revokePerm(String roleId, String firstPerm, String... morePerms);

    int revokePerm(Role role, String firstPerm, String... morePerms);

    int revokePerm(String roleId, List<String> permIdList);

    int revokePerm(Role role, List<String> permIdList);

    /**
     * 撤销指定角色的所有权限
     *
     * @param roleId 角色 ID
     * @return 撤销的权限条目
     */
    int revokeAllPerms(String roleId);

    /**
     * 只有既不被账户所使用，也没有关联到权限的角色才算是不被使用的角色，否则就算是正被使用
     *
     * @param id 角色 ID
     * @return 是否正被使用
     */
    boolean isInUse(String id);

}
