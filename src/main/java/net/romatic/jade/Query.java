package net.romatic.jade;

import net.romatic.com.collection.Models;
import net.romatic.query.QueryBuilder;

/**
 * @author zhrlnt@gmail.com
 */
public interface Query<M extends Model> {

    /**
     * 主键查询
     *
     * @param id
     * @return
     */
    M find(long id);

    /**
     * 获取第一个数据
     *
     * @return
     */
    M first();

    /**
     * 预加载 Relation
     *
     * @param eagerRelations
     * @param <Q>
     * @return
     */
    <Q extends Query<M>> Q with(String... eagerRelations);

    <Q extends Query<M>> Q where();

    <E extends Query<M>, V> E where(String column, V value);

    <Q extends Query<M>> Q limit(long limit);

    /**
     * 执行查询，返回结果集合
     *
     * @return
     */
    Models<M> get();

    /**
     * 获取查询构造器
     *
     * @return
     * @see QueryBuilder
     */
    QueryBuilder getQuery();
}
