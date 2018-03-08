package net.romatic.jade.relation;

import net.romatic.jade.Builder;
import net.romatic.jade.Model;
import net.romatic.jade.annotation.BelongsTo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author huiren
 */
public class RelationUtils {
    public static Relation getRelation(Model model, Field field) {
        Annotation annotation = getFirstRelation(field);

        Model related = getRelated(field);
        //related.newQuery();
        Builder builder = related.newJadeQuery();

        try {
            if (annotation instanceof BelongsTo) {

                String localKey = ((BelongsTo) annotation).localKey();
                if ("".equals(localKey)) {
                    localKey = field.getName() + "_id";
                }
                String relatedKey = ((BelongsTo) annotation).relatedKey();
                if ("".equals(relatedKey)) {
                    relatedKey = "id";
                }

                return BelongsToBuilder.newBelongsTo(
                        field.getName(),
                        model,
                        localKey,
                        relatedKey,
                        builder
                );
            }
        } finally {

        }

        return null;
    }

    /**
     * 获取字段的有 Relation 意义的注解，如 HasOne
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

    protected static Model getRelated(Field field) {
        Class clz = field.getType();
        if (clz.isArray()) {

        }

        Model related = null;
        try {
            related = (Model) clz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return related;
    }
}
