package net.romatic.jade.annotation;

import net.romatic.jade.relation.HasManyBuilder;
import net.romatic.jade.relation.RelationBuilder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author huiren
 * HasMany 表示拥有某个对象的关系，跟 HasOne 的区别是
 */
@Retention(RetentionPolicy.RUNTIME)
@Relation
public @interface HasMany {

    /**
     * 当前表的关联字段
     * 传属性名的好处：取值方便。构造SQL时借用统一的转换工具
     * 传DB字段名的好处：构造SQL方便
     *
     * @return
     */
    String localKey() default "id";

    /**
     * 关联表的关联字段
     *
     * @return
     */
    String relatedKey() default "";

    Class<? extends RelationBuilder> builder() default HasManyBuilder.class;
}
