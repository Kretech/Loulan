package net.romatic.jade;

import net.romatic.query.QueryBuilder;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhrlnt@gmail.com
 */
public class Builder<T extends Model> {

    private QueryBuilder query;

    private T model;

    public Builder(QueryBuilder query) {
        this.query = query;
    }

    public Builder take(long limit) {
        return limit(limit);
    }

    public Builder limit(long limit) {
        return this;
    }

    public T find(long id) {
        return (T) where("id", id).first();
    }

    public T first() {
        return (T) limit(1).get().get(0);
    }

    public List<T> get() {

        Map<String, Object>[] items = query.get();

        return hydrate(items);
    }

    public List<T> hydrate(Map<String, Object>[] items) {
        return Stream.of(items).map(attr -> (T) model.newInstance(attr)).collect(Collectors.toList());
    }

    public Builder where(String column, Object value) {
        return where(column, "=", value);
    }

    public Builder orWhere(String column, Object value) {
        return where(column, "like", value, false);
    }

    public Builder where(String column, String operator, Object value) {
        return where(column, operator, value, true);
    }

    public Builder where(String column, String operator, Object value, Boolean isAnd) {
        query.where(column, operator, value, isAnd ? "AND" : "OR");

        return this;
    }

    public Builder where(Function callable) {
        return this;
    }

    public Builder whereIn(String column, Object value) {
        return where(column, "in", value);
    }

    public Builder whereHas(String relation, Function closure) {

        Builder rel = new Builder(query);
        closure.apply(rel);

        return this;
    }

    public QueryBuilder getQuery() {
        return query;
    }

    public void setModel(T model) {
        this.model = model;

        query.from(model.getTable());
    }
}
