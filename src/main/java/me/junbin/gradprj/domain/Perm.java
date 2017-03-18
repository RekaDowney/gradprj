package me.junbin.gradprj.domain;


import me.junbin.gradprj.domain.relation.Relation;
import me.junbin.gradprj.enumeration.PermType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/1/31 19:46
 * @description :
 */
public class Perm extends DetailDomain implements Relation<Perm, String> {

    private String permName;
    private String permUrl;
    private String permPattern;
    private PermType permType;
    private int weight = 1000;
    private boolean attachable = false;
    private boolean active = true;
    // 这里只存储上级权限的 ID，他们之间的关联关系通过
    private String parentId;
    private List<Perm> sub = new ArrayList<>();
    public static final Comparator<Perm> COMPARE_BY_WEIGHT = Comparator.comparing(Perm::getWeight);

    public Perm() {
    }

    public Perm(String id, String permName) {
        this.id = id;
        this.permName = permName;
    }

    public Perm(String id, String permName, String parentId) {
        this.id = id;
        this.permName = permName;
        this.parentId = parentId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Perm perm = (Perm) object;
        return valid == perm.valid &&
                weight == perm.weight &&
                active == perm.active &&
                attachable == perm.attachable &&
                permType == perm.permType &&
                Objects.equals(id, perm.id) &&
                Objects.equals(permName, perm.permName) &&
                Objects.equals(permUrl, perm.permUrl) &&
                Objects.equals(permPattern, perm.permPattern) &&
                Objects.equals(creator, perm.creator) &&
                Objects.equals(createdTime, perm.createdTime) &&
                Objects.equals(modifier, perm.modifier) &&
                Objects.equals(modifiedTime, perm.modifiedTime) &&
                Objects.equals(parentId, perm.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, permName, permUrl, permPattern, permType, creator, createdTime, modifier, modifiedTime, weight, attachable, active, parentId, valid);
    }

    public String getPermName() {
        return permName;
    }

    public void setPermName(String permName) {
        this.permName = permName;
    }

    public String getPermUrl() {
        return permUrl;
    }

    public void setPermUrl(String permUrl) {
        this.permUrl = permUrl;
    }

    public String getPermPattern() {
        return permPattern;
    }

    public void setPermPattern(String permPattern) {
        this.permPattern = permPattern;
    }

    public PermType getPermType() {
        return permType;
    }

    public void setPermType(PermType permType) {
        this.permType = permType;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isAttachable() {
        return attachable;
    }

    public void setAttachable(boolean attachable) {
        this.attachable = attachable;
    }

    @Override
    public List<Perm> getSub() {
        return sub;
    }

    @Override
    public void setSub(List<Perm> subRelations) {
        this.sub = subRelations;
    }

}
