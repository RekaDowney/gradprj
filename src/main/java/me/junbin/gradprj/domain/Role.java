package me.junbin.gradprj.domain;

import me.junbin.commons.gson.Gsonor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/1/31 17:08
 * @description :
 */
public class Role extends DetailDomain {

    private String roleName;
    private String roleNameCn;
    private String remarks;
    private boolean active = true;
    // 只需要存储权限的 ID 列表即可，不需要存储权限，当页面需要展示时可以通过其他方法进行加载
    private List<String> permIdList = new ArrayList<>();

    public Role() {
    }

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public Role(String roleName, String remarks) {
        this.roleName = roleName;
        this.remarks = remarks;
    }

    public Role(String roleName, String roleNameCn, String remarks) {
        this.roleName = roleName;
        this.roleNameCn = roleNameCn;
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return Gsonor.SIMPLE.toJson(this);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Role role = (Role) object;
        return active == role.active &&
                valid == role.valid &&
                Objects.equals(id, role.id) &&
                Objects.equals(roleName, role.roleName) &&
                Objects.equals(roleNameCn, role.roleNameCn) &&
                Objects.equals(creator, role.creator) &&
                Objects.equals(createdTime, role.createdTime) &&
                Objects.equals(modifier, role.modifier) &&
                Objects.equals(modifiedTime, role.modifiedTime) &&
                Objects.equals(remarks, role.remarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleName, roleNameCn, remarks, creator, createdTime, modifier, modifiedTime, active, valid);
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleNameCn() {
        return roleNameCn;
    }

    public void setRoleNameCn(String roleNameCn) {
        this.roleNameCn = roleNameCn;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<String> getPermIdList() {
        return permIdList;
    }

    public void setPermIdList(List<String> permIdList) {
        this.permIdList = permIdList;
    }

}
