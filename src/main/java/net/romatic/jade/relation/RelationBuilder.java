package net.romatic.jade.relation;

import net.romatic.com.collection.Models;
import net.romatic.jade.Builder;
import net.romatic.jade.Model;
import net.romatic.query.QueryBuilder;
import net.romatic.utils.JsonUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author huiren
 */
public abstract class RelationBuilder {

    /**
     * 关联表的查询
     */
    protected Builder builder;

    /**
     * 关联的名字，左表通过此属性访问右表
     */
    protected String name;

    /**
     * 左表
     */
    protected Model local;
    /**
     * 左表字段名
     */
    protected String localKey;
    /**
     * 右表
     */
    protected Model related;
    /**
     * 右表字段名
     */
    protected String relatedKey;

    public String getLocalKey() {
        return localKey;
    }

    public String getRelatedKey() {
        return relatedKey;
    }

    public Model getRelated() {
        return related;
    }

    public Builder getBuilder() {
        return builder;
    }

    public void setBuilder(Builder builder) {
        this.builder = builder;
    }

    @Deprecated
    protected void init(Builder builder, Model local, String localKey, String relatedKey) {
        init(this.name, builder, local, localKey, relatedKey);
    }

    protected void init(String name, Builder builder, Model local, String localKey, String relatedKey) {
        this.name = name;
        this.builder = builder;

        this.local = local;
        this.localKey = localKey;
        this.related = builder.getModel();
        this.relatedKey = relatedKey;

        System.out.println(String.format("init[%s] localKey[%s] relatedKey[%s]", this.getClass().getSimpleName(), localKey, relatedKey));
    }

    /**
     * 以 Model + field 初始化
     *
     * @param field
     */
    public abstract void initBy(Field field);

    /**
     * 给 builder 添加关联约束
     */
    public abstract void withEagerConstraints();

    /**
     * 添加关联Query的关联约束
     *
     * @param models
     */
    public abstract void withEagerConstraints(List<? extends Model> models);

    public Builder getHasInQuery(Builder parent) {
        Builder builder = getRelated().newJadeQuery();
        builder.getQuery().select(relatedKey);

        return builder;
    }

    /**
     * 匹配到 Models 的对应属性上
     *
     * @param models
     * @param relateds
     * @param relationName
     * @return
     */
    public abstract Models match(Models<? extends Model> models, Models relateds, String relationName);
}
