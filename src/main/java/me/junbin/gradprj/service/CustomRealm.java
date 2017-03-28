package me.junbin.gradprj.service;

import me.junbin.gradprj.domain.Account;
import me.junbin.gradprj.domain.Perm;
import me.junbin.gradprj.domain.Role;
import me.junbin.gradprj.enumeration.PermType;
import me.junbin.gradprj.util.Global;
import me.junbin.gradprj.util.RelationResolver;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/1 20:40
 * @description :
 */
@Service("customRealm")
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private AccountService accountService;
    @Autowired
    private PermService permService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        if (!(authenticationToken instanceof UsernamePasswordToken)) {
            throw new UnsupportedTokenException();
        }

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String principal = token.getUsername();

        Account visitor = Global.getVisitorAccount();
        // 发现登陆身份是游客，直接将游客作为登陆验证信息
        if (StringUtils.equals(principal, visitor.getPrincipal())) {
            return new SimpleAuthenticationInfo(visitor, visitor.getPassword(), getName());
        }

        Account account;
        try {
            account = accountService.selectByPrincipal(principal);
        } catch (Exception e) {
            account = null;
        }
        if (account == null) {
            throw new UnknownAccountException("账户名或者密码错误");
        }

        String password = new String(token.getPassword());
        if (!StringUtils.equals(password, account.getPassword())) {
            throw new IncorrectCredentialsException("账户名或者密码错误");
        }

        // 登陆信息无误之后，加载用户角色和权限列表
        List<Role> roleList = accountService.acquireRoles(account.getId());
        Set<String> permIdSet = new HashSet<>();
        for (Role role : roleList) {
            permIdSet.addAll(role.getPermIdList());
        }
        List<Perm> totalPerm = permService.selectAll();
        List<Perm> permList = RelationResolver.findIn(totalPerm, permIdSet);

        account.setRoleList(roleList);
        account.setPermList(permList);
        List<Perm> menu = permList.stream().filter(p -> p.getPermType() == PermType.MENU)
                                  .collect(Collectors.toList());
        account.setRelationPermList(RelationResolver.relationalize(menu));

        // 交由 Controller 层进行存储管理
        // AccountMgr.store(account);

        return new SimpleAuthenticationInfo(account, password, getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Account account = (Account) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo =
                new SimpleAuthorizationInfo();
        Set<String> roleNames = account.getRoleList().stream()
                                       .map(Role::getRoleName)
                                       .collect(Collectors.toSet());
        Set<String> permStrings = account.getPermList().stream()
                                         .map(Perm::getPermPattern)
                                         .collect(Collectors.toSet());
        authorizationInfo.setRoles(roleNames);
        authorizationInfo.setStringPermissions(permStrings);
        return authorizationInfo;
    }

}
