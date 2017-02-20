package me.junbin.gradprj.domain;

import me.junbin.commons.gson.Gsonor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/1/31 17:07
 * @description :
 */
public class DetailDomain extends BaseDomain implements Serializable {

    // 仅存储 Account 的 ID
    protected String creator;
    protected LocalDateTime createdTime;
    // 仅存储 Account 的 ID
    protected String modifier;
    protected LocalDateTime modifiedTime;

    public DetailDomain() {
    }

    @Override
    public String toString() {
        return Gsonor.SIMPLE.toJson(this);
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

}
