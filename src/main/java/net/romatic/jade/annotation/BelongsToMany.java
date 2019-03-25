package net.romatic.jade.annotation;

import net.romatic.jade.relation.BelongsToManyBuilder;
import net.romatic.jade.relation.RelationBuilder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author huiren
 * <p>
 * BelongsToMany 表示多对多的关系
 * 暂时只支持使用主键关联的情况
 * 模型A 通过 表a_b 关联 模型，且关联字段为 a_id 和 b_id
 * 其中，表名(a_b)和关联字段(a_id)的名字可以自定义，但只能关联到 表a 和 表b 的主键(id)
 */
@Retention(RetentionPolicy.RUNTIME)
@Relation
public @interface BelongsToMany {

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
    String relatedKey() default "";

    /**
     * 中间表
     *
     * @return
     */
    String table();

    Class<? extends RelationBuilder> builder() default BelongsToManyBuilder.class;
}
