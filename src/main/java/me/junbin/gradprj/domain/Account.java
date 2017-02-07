package me.junbin.gradprj.domain;

import me.junbin.commons.gson.Gsonor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/1/31 15:09
 * @description :
 */
public class Account extends BaseDomain {

    private String principal;
    private String password;
    private LocalDateTime createdTime;
    private boolean locked = false;
    private User user;
    // 一条关联查询语句可以解决
    private List<Role> roleList = new ArrayList<>();
    // 首先查询所有的权限，之后通过 roleList 和 RelationResolver 可以得到账户的权限列表
    private List<Perm> permList = new ArrayList<>();

    public Account() {
    }

    public Account(String principal, String password) {
        this.principal = principal;
        this.password = password;
    }

    public Account(String principal, String password, LocalDateTime createdTime) {
        this.principal = principal;
        this.password = password;
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return Gsonor.EXCLUDE_PWD_FIELD_SIMPLE.toJson(this);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Account account = (Account) object;
        return locked == account.locked &&
                valid == account.valid &&
                Objects.equals(id, account.id) &&
                Objects.equals(principal, account.principal) &&
                Objects.equals(password, account.password) &&
                Objects.equals(createdTime, account.createdTime) &&
                Objects.equals(user, account.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, principal, password, createdTime, locked, valid, user);
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public List<Perm> getPermList() {
        return permList;
    }

    public void setPermList(List<Perm> permList) {
        this.permList = permList;
    }

}
