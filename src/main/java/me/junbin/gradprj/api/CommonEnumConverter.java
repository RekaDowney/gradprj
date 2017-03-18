package me.junbin.gradprj.api;

import com.google.gson.*;
import me.junbin.commons.converter.custom.gson.GsonTypeAdapter;
import me.junbin.commons.converter.custom.mybatis.MyBatisEnumTypeHandler;
import me.junbin.gradprj.enumeration.CommonEnum;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/22 21:36
 * @description :
 */
public class CommonEnumConverter<E extends Enum<E> & CommonEnum<E>>
        extends MyBatisEnumTypeHandler<E>
        implements GsonTypeAdapter<E> {

    private final E e;

    public CommonEnumConverter() {
        Class<? extends CommonEnumConverter> thisClazz = this.getClass();
        if (thisClazz == CommonEnumConverter.class) {
            throw new IllegalArgumentException("This constructor must only invokes by the subclass of CommonEnumConverter");
        }

        Type type = thisClazz.getGenericSuperclass();
        if (!(type instanceof ParameterizedType)) {
            throw new IllegalArgumentException("super class must be ParameterizedType");
        }
        ParameterizedType pType = (ParameterizedType) type;
        Type[] types = pType.getActualTypeArguments();
        if (types.length != 1) {
            throw new IllegalArgumentException("Generic parameter must only One!");
        }
        Type enumType = types[0];
        if (!(enumType instanceof Class)) {
            throw new IllegalArgumentException("Generic parameter must be Class");
        }
        Class<?> clazz = (Class<?>) enumType;
        if (!clazz.isEnum()) {
            throw new IllegalArgumentException("Generic parameter must be Enum");
        }
        Object[] enums = clazz.getEnumConstants();
        if (enums.length < 1) {
            throw new IllegalArgumentException("Enum must contains item");
        }
        Object enum0 = enums[0];
        //noinspection unchecked
        this.e = (E) enum0;
    }

    public CommonEnumConverter(E e) {
        super(e);
        this.e = e;
    }

    @Override
    public JsonElement serialize(E src, Type typeOfSrc, JsonSerializationContext context) {
        return null == src ? null : new JsonPrimitive(src.serialize());
    }

    @Override
    public E deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return null == json ? null : e.deserialize(json.getAsString());
    }

}
