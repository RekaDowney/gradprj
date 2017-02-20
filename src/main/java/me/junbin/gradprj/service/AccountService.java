package me.junbin.gradprj.service;


import me.junbin.gradprj.domain.Account;
import me.junbin.gradprj.domain.Role;

import java.util.List;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/1 13:18
 * @description :
 */
public interface AccountService extends BaseService<Account, String> {

    int lock(Account account);

    int unlock(Account account);

    int grantRole(String accountId, String firstRole, String... moreRoles);

    int grantRole(Account account, String firstRole, String... moreRoles);

    int grantRole(String accountId, List<String> roleIdList);

    int grantRole(Account account, List<String> roleIdList);

    int revokeAllRoles(String accountId);

    Account selectByPrincipal(String principal);

    List<Role> acquireRoles(String accountId);
}
