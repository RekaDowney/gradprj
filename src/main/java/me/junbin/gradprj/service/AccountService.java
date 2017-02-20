package me.junbin.gradprj.service;


import me.junbin.gradprj.domain.Account;
import me.junbin.gradprj.domain.Role;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/1 13:18
 * @description :
 */
public interface AccountService extends BaseService<Account, String> {

    int delete(String id, String modifier, LocalDateTime modifiedTime);

    int delete(Account account);

    int update(Account account);

    Account selectByPrincipal(String principal);

    int lock(Account account);

    //int unlock(Account account);
    //
    //int grantRole(String accountId, String firstRole, String... moreRoles);
    //
    //int grantRol(String accountId, List<String> roleIdList);
    //
    //int revokeRole(String accountId, String firstRole, String... moreRoles);
    //
    //int revokeAllRoles(String accountId);

    List<Role> acquireRoles(String accountId);
}
