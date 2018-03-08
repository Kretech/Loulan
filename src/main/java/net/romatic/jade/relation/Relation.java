package net.romatic.jade.relation;

import net.romatic.com.collection.Models;
import net.romatic.jade.Builder;
import net.romatic.jade.Model;

import java.util.List;

/**
 * @author huiren
 */
abstract public class Relation<B> {

    /**
     * 关联表的查询
     */
    protected Builder builder;
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

    public Builder getBuilder() {
        return builder;
    }

    public void setBuilder(Builder builder) {
        this.builder = builder;
    }

    protected void init(Builder builder, Model local, String localKey, String relatedKey) {
        this.builder = builder;

        this.local = local;
        this.localKey = localKey;
        this.related = builder.getModel();
        this.relatedKey = relatedKey;
    }

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

    /**
     * 匹配
     *
     * @param models
     * @param relateds
     * @param relationName
     * @return
     */
    public abstract Models match(Models<? extends Model> models, Models relateds, String relationName);
}
