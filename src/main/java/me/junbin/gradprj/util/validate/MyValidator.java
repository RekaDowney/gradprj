package me.junbin.gradprj.util.validate;

import me.junbin.commons.util.CollectionUtils;
import me.junbin.gradprj.util.validate.exception.*;
import org.joor.Reflect;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/16 13:04
 * @description :
 */
public abstract class MyValidator {

/*
    public static boolean notNull(Object target, String... properties) {
        try {
            nullThrows(target, properties);
        } catch (ValidationException e) {
            return false;
        }
        return true;
    }
*/

    public static void nullThrows(Object target, String... properties) {
        nullThrows(PropertyIsNullException.class, target, properties);
    }

    /**
     * 验证指定目标对象非空以及所有指定的属性都非空，否则抛出指定异常
     *
     * @param typeOfE    异常类型，该异常类必须有一个能够接受字符串的构造器
     * @param target     验证的目标对象
     * @param properties 验证对象的属性名称
     * @param <E>        异常泛型
     * @throws E 异常
     */
    @SuppressWarnings("unchecked")
    public static <E extends Exception> void nullThrows(Class<E> typeOfE, Object target, String... properties) throws E {
        if (typeOfE == null) {
            throw new IllegalArgumentException("exception type must not be null");
        }
        if (target == null) {
            throw (E) Reflect.on(typeOfE).create("target requires not null").get();
        }
        if (properties.length == 0) {
            return;
        }
        Map<String, Reflect> fieldsMap = Reflect.on(target).fields();
        Object fieldValue;
        for (String property : properties) {
            if (!fieldsMap.containsKey(property)) {
                throw new PropertyNotExistsException(property);
            }
            fieldValue = fieldsMap.get(property).get();
            if (fieldValue == null) {
                throw (E) Reflect.on(typeOfE).create(property + " requires not null").get();
            }
        }
    }

    public static void nullThrows(Object... arguments) {
        for (Object arg : arguments) {
            if (arg == null) {
                throw new ValidationException("argument must not be null");
            }
        }
    }


    public static void throwsIfContainerSizeLessThan(Object target, int size) {
        throwsIfContainerSizeLessThan(target, size, "object is container and requires size greater or equals " + size);
    }

    public static void throwsIfContainerSizeLessThan(Object target, int size, String errorMsg) {
        throwsIfContainerSizeLessThan(target, size, ValidationException.class, errorMsg);
    }

    /**
     * 只有当 {@code target} 为容器（容器包括数组、列表、集合、映射表）才进行验证。
     * 前提：{@code target} 非空
     *
     * @param target   集合对象
     * @param size     最小长度
     * @param typeOfE  异常类型
     * @param errorMsg 异常信息
     * @param <E>      异常泛型
     * @throws E 异常
     */
    @SuppressWarnings("unchecked")
    public static <E extends Exception> void throwsIfContainerSizeLessThan(Object target, int size, Class<E> typeOfE, String errorMsg) throws E {
        if (target == null) {
            throw new ValidationException("the validation object must not null");
        }
        Class<?> targetClass = target.getClass();
        if (Collection.class.isAssignableFrom(targetClass) &&
                ((Collection) target).size() < size) {
            throw (E) Reflect.on(typeOfE).create(errorMsg).get();
        }
        if (Map.class.isAssignableFrom(targetClass) &&
                ((Map) target).size() < size) {
            throw (E) Reflect.on(typeOfE).create(errorMsg).get();
        }
        if (targetClass.isArray() &&
                Array.getLength(target) < size) {
            throw (E) Reflect.on(typeOfE).create(errorMsg).get();
        }
        // 不是容器类
    }

    public static void throwsOnlyIsEmptyContainer(Object target, String errorMsg) {
        if (target == null) {
            return;
        }
        Class<?> targetClass = target.getClass();
        if (Collection.class.isAssignableFrom(targetClass) &&
                ((Collection) target).isEmpty()) {
            throw new CollectionIsEmptyException(errorMsg);
        }
        if (Map.class.isAssignableFrom(targetClass) &&
                ((Map) target).isEmpty()) {
            throw new MapIsEmptyException(errorMsg);
        }
        if (targetClass.isArray() &&
                Array.getLength(target) == 0) {
            throw new ArrayIsEmptyException(errorMsg);
        }
        // 不是容器类
    }

    public static void throwsIfNullOrIsEmptyContainer(Object target, String errorMsg) {
        if (target == null) {
            throw new ValidationException(errorMsg);
        }
        throwsOnlyIsEmptyContainer(target, errorMsg);
    }

    // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public static void emptyThrows(Collection<?> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ValidationException("collection must not be null and empty");
        }
    }

    public static void emptyThrows(Map<?, ?> map) {
        if (CollectionUtils.isEmpty(map)) {
            throw new ValidationException("map must not be null and empty");
        }
    }

/*
    public static boolean notEmpty(Object target, String... properties) {
        try {
            emptyThrows(target, properties);
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    public static void emptyThrows(Object target, String... properties) {
        emptyThrows(PropertyIsEmptyException.class, target, properties);
    }

    public static void emptyThrows(String... arguments) {
        for (String arg : arguments) {
            if (StringUtils.isEmpty(arg)) {
                throw new ValidationException("argument must not be empty");
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <E extends Exception> void emptyThrows(Class<E> typeOfE, Object target, String... properties) throws E {
        if (typeOfE == null) {
            throw new IllegalArgumentException("exception type must not be null");
        }
        if (target == null) {
            throw (E) Reflect.on(typeOfE).create("target requires not null").get();
        }
        if (properties.length == 0) {
            return;
        }
        Map<String, Reflect> fieldsMap = Reflect.on(target).fields();
        Object fieldValue;
        Class<?> fieldClass;
        for (String property : properties) {
            if (!fieldsMap.containsKey(property)) {
                throw new PropertyNotExistsException(property);
            }
            fieldValue = fieldsMap.get(property).get();
            if (fieldValue == null) {
                throw (E) Reflect.on(typeOfE).create(property + " requires not null").get();
            }
            fieldClass = fieldValue.getClass();
            if (String.class.isAssignableFrom(fieldClass) &&
                    StringUtils.isEmpty(fieldValue.toString())) {
                throw (E) Reflect.on(typeOfE).create(property + " requires not empty").get();
            }
            throwsOnlyIsEmptyContainer(fieldValue, property + " requires not empty");
        }
    }

    public static boolean notBlank(Object target, String... properties) {
        try {
            blankThrows(target, properties);
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    public static void blankThrows(Object target, String... properties) {
        blankThrows(PropertyIsBlankException.class, target, properties);
    }

    @SuppressWarnings("unchecked")
    public static <E extends Exception> void blankThrows(Class<E> typeOfE, Object target, String... properties) throws E {
        if (typeOfE == null) {
            throw new IllegalArgumentException("exception type must not be null");
        }
        if (target == null) {
            throw (E) Reflect.on(typeOfE).create("target requires not null").get();
        }
        if (properties.length == 0) {
            return;
        }
        Map<String, Reflect> fieldsMap = Reflect.on(target).fields();
        Object fieldValue;
        for (String property : properties) {
            if (!fieldsMap.containsKey(property)) {
                throw new PropertyNotExistsException(property);
            }
            fieldValue = fieldsMap.get(property).get();
            if (fieldValue == null) {
                throw (E) Reflect.on(typeOfE).create(property + " requires not null").get();
            }
            if (String.class.isAssignableFrom(fieldValue.getClass()) &&
                    StringUtils.isBlank(fieldValue.toString())) {
                throw (E) Reflect.on(typeOfE).create(property + " requires not blank").get();
            }
        }
    }
*/

}