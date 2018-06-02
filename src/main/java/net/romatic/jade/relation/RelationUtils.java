package net.romatic.jade.relation;

import com.sun.org.apache.xml.internal.security.encryption.ReferenceList;
import net.romatic.com.reflect.ReflectUtils;
import net.romatic.jade.Model;
import net.romatic.jade.annotation.MappingClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author huiren
 */
public class RelationUtils {

    public static <R extends RelationBuilder, M extends Model> R getRelation(M model, String relationName) throws NoSuchFieldException {
        Field field = model.getClass().getDeclaredField(relationName);
        return getRelation(field);
    }

    public static <R extends RelationBuilder> R getRelation(Field field) {
        Annotation annotation = getFirstRelation(field);

        try {
            Class relationClass = (Class) annotation.getClass().getMethod("builder").invoke(annotation);
            RelationBuilder relationHandler = (RelationBuilder) relationClass.newInstance();
            {
                relationHandler.initBy(field);
            }

            return (R) relationHandler;

        } catch (NoSuchMethodException e) {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

        return null;
    }

    /**
     * 获取字段的有 RelationBuilder 意义的注解，如 HasOne
     *
     * @param field
     * @return
     */
    protected static Annotation getFirstRelation(Field field) {
        for (Annotation annotation : field.getAnnotations()) {
            if (annotation.annotationType().getAnnotation(net.romatic.jade.annotation.Relation.class) != null) {
                return annotation;
            }
        }

        return null;
    }

    /**
     * 获取一个关联模型的实例
     *
     * @param field
     * @return
     */
    protected static Model getRelated(Field field) {
        Class clz = field.getType();
        MappingClass annotation = field.getAnnotation(MappingClass.class);
        if (annotation != null) {
            clz = annotation.value();
        }

        Model related = null;
        try {
            related = (Model) clz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return related;
    }
}
