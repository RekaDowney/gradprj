package me.junbin.gradprj.service;

import me.junbin.gradprj.domain.Perm;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/18 22:08
 * @description :
 */
public interface PermService extends BaseService<Perm, String> {

    /**
     * @param perm 权限信息，要求 {@link Perm#id}，{@link Perm#modifier}，{@link Perm#modifiedTime} 都非空
     * @return 激活权限操作所影响的行数
     */
    int activate(Perm perm);

    int inactivate(Perm perm);

    int update(Perm perm);

    int detachAssociatedRole(String id);

    boolean isAssociateWithRole(String id);

}
