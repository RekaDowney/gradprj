package me.junbin.gradprj.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.junbin.commons.gson.GsonUtils;

import java.io.Reader;
import java.lang.reflect.Type;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/15 21:56
 * @description :
 */
public interface Jdk8GsonUtils extends GsonUtils {

    Gson get();

    @Override
    default String toJson(Object source) {
        return this.get().toJson(source);
    }

    @Override
    default String toJson(Object src, Type typeOfSrc) {
        return this.get().toJson(src, typeOfSrc);
    }

    @Override
    default String toJson(JsonElement jsonElement) {
        return this.get().toJson(jsonElement);
    }

    @Override
    default void toJson(Object src, Appendable writer) {
        this.get().toJson(src, writer);
    }

    @Override
    default void toJson(Object src, Type typeOfSrc, Appendable writer) {
        this.get().toJson(src, typeOfSrc, writer);
    }

    @Override
    default void toJson(Object src, Type typeOfSrc, JsonWriter writer) {
        this.get().toJson(src, typeOfSrc, writer);
    }

    @Override
    default void toJson(JsonElement jsonElement, Appendable writer) {
        this.get().toJson(jsonElement, writer);
    }

    @Override
    default void toJson(JsonElement jsonElement, JsonWriter writer) {
        this.get().toJson(jsonElement, writer);
    }

    @Override
    default JsonElement toJsonTree(Object src) {
        return this.get().toJsonTree(src);
    }

    @Override
    default JsonElement toJsonTree(Object src, Type typeOfSrc) {
        return this.get().toJsonTree(src, typeOfSrc);
    }

    @Override
    default <T> T fromJson(String json, Class<T> classOfT) {
        return this.get().fromJson(json, classOfT);
    }

    @Override
    default <T> T fromJson(String json, Type typeOfT) {
        return this.get().fromJson(json, typeOfT);
    }

    @Override
    default <T> T fromJson(Reader json, Class<T> classOfT) {
        return this.get().fromJson(json, classOfT);
    }

    @Override
    default <T> T fromJson(Reader json, Type typeOfT) {
        return this.get().fromJson(json, typeOfT);
    }

    @Override
    default <T> T fromJson(JsonReader reader, Type typeOfT) {
        return this.get().fromJson(reader, typeOfT);
    }

    @Override
    default <T> T fromJson(JsonElement json, Class<T> classOfT) {
        return this.get().fromJson(json, classOfT);
    }

    @Override
    default <T> T fromJson(JsonElement json, Type typeOfT) {
        return this.get().fromJson(json, typeOfT);
    }

}
