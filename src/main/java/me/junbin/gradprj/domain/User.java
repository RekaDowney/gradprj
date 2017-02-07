package me.junbin.gradprj.domain;

import me.junbin.commons.gson.Gsonor;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/1/31 16:54
 * @description :
 */
public class User implements Serializable {

    private String id;
    private String email;
    private String username;
    private String nickname;
    private String qq;
    private String wx;
    private Photo portrait;

    public User() {
    }

    @Override
    public String toString() {
        return Gsonor.SIMPLE.toJson(this);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(id, user.id) &&
                Objects.equals(email, user.email) &&
                Objects.equals(username, user.username) &&
                Objects.equals(nickname, user.nickname) &&
                Objects.equals(qq, user.qq) &&
                Objects.equals(wx, user.wx) &&
                Objects.equals(portrait, user.portrait);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, username, nickname, qq, wx, portrait);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public Photo getPortrait() {
        return portrait;
    }

    public void setPortrait(Photo portrait) {
        this.portrait = portrait;
    }

}
