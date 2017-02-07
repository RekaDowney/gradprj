package me.junbin.gradprj.domain;

import me.junbin.commons.gson.Gsonor;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/1/31 16:56
 * @description :
 */
public class Photo extends BaseDomain {

    private String photoName;
    private String path;
    private LocalDateTime createdTime;

    public Photo() {
    }

    public Photo(String path) {
        this.path = path;
    }

    public Photo(String photoName, String path) {
        this.photoName = photoName;
        this.path = path;
    }

    public Photo(String path, LocalDateTime createdTime) {
        this.path = path;
        this.createdTime = createdTime;
    }

    public Photo(String photoName, String path, LocalDateTime createdTime) {
        this.photoName = photoName;
        this.path = path;
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return Gsonor.SIMPLE.toJson(this);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Photo photo = (Photo) object;
        return valid == photo.valid &&
                Objects.equals(id, photo.id) &&
                Objects.equals(photoName, photo.photoName) &&
                Objects.equals(path, photo.path) &&
                Objects.equals(createdTime, photo.createdTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, photoName, path, createdTime, valid);
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

}
