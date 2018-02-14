package net.romatic.com.reflect;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhrlnt@gmail.com
 */
public class ReflectUtils {

    /**
     * 获取类的所有字段，包括父类的和非public
     *
     * @param clz
     * @return
     */
    public static List<Field> listFields(Class clz) {
        List<Field> fields = Arrays.stream(clz.getDeclaredFields()).collect(Collectors.toList());

        if (clz.getSuperclass() != Object.class) {
            fields.addAll(listFields(clz.getSuperclass()));
        }

        return fields;
    }

    public static List<String> listFieldNames(Class clz) {
        return listFields(clz).stream().map(field -> field.getName()).distinct().collect(Collectors.toList());
    }
}
