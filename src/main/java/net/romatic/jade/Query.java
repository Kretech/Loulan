package net.romatic.jade;

import net.romatic.com.collection.Models;
import net.romatic.query.QueryBuilder;

/**
 * @author zhrlnt@gmail.com
 */
public interface Query<M extends Model> {

    M find(long id);

    M first();

    <Q extends Query<M>> Q with(String... eagerRelations);

    <Q extends Query<M>> Q where();

    <E extends Query<M>, V> E where(String column, V value);

    <Q extends Query<M>> Q limit(long limit);

    Models<M> get();

    QueryBuilder getQuery();
}
