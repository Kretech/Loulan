package net.romatic.jade.annotation;

import net.romatic.jade.relation.BelongsToBuilder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author huiren
 * <p>
 * BelongsTo 表示属于某个模型的关系
 */
@Retention(RetentionPolicy.RUNTIME)
@Relation
public @interface BelongsTo {

    /**
     * 当前表的关联字段
     * 传属性名的好处：取值方便
     * 传DB字段名的好处：构造SQL方便
     *
     * @return
     */
    String localKey() default "";

    /**
     * 关联表的关联字段
     *
     * @return
     */
    String relatedKey() default "id";

    Class<? extends net.romatic.jade.relation.Relation> builder() default BelongsToBuilder.class;
}
