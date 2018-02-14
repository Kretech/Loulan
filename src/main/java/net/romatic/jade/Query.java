package net.romatic.jade;

import net.romatic.query.QueryBuilder;

import java.util.List;

/**
 * @author zhrlnt@gmail.com
 */
public interface Query<M> {

    M find(long id);

    M first();

    <Q extends Query<M>> Q where();

    <E extends Query<M>, V> E where(String column, V value);

    <Q extends Query<M>> Q limit(long limit);

    List<M> get();

    QueryBuilder getQuery();
}
