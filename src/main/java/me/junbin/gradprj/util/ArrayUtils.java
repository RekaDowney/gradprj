package me.junbin.gradprj.util;

import me.junbin.commons.util.Args;
import me.junbin.commons.util.CollectionUtils;

import java.lang.reflect.Array;
import java.util.List;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/2/19 9:46
 * @description :
 */
public abstract class ArrayUtils {

    public static <T> T[] toArray(List<T> list, Class<T> typeOfT) {
        return toArray(list, typeOfT, 0);
    }

    public static <T> T[] toArray(List<T> list, Class<T> typeOfT, int startIncludeIndex) {
        Args.notNull(list);
        return toArray(list, typeOfT, startIncludeIndex, list.size());
    }

    public static <T> T[] toArray(List<T> list, Class<T> typeOfT, int startIncludeIndex, int endExcludeIndex) {
        Args.notNull(typeOfT);
        if (startIncludeIndex > endExcludeIndex) {
            throw new IllegalArgumentException(String.format("start index[%d] must smaller or equals than end index[%d]", startIncludeIndex, endExcludeIndex));
        }
        if (CollectionUtils.isEmpty(list) && startIncludeIndex > 0) {
            throw new IllegalArgumentException(String.format("list is empty but start index[%d] is bigger than 0", startIncludeIndex));
        }
        int size = list.size();
        if (endExcludeIndex > size) {
            throw new IllegalArgumentException(String.format("endExcludeIndex[%d] is bigger than list size[%d]", endExcludeIndex, size));
        }
        int arrayLength = endExcludeIndex - startIncludeIndex;
        //noinspection unchecked
        T[] result = (T[]) Array.newInstance(typeOfT, arrayLength);
        int index = 0;
        for (int i = startIncludeIndex; i < endExcludeIndex; i++) {
            result[index++] = list.get(i);
        }
        return result;
    }

}
