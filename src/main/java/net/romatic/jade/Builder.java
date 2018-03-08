package net.romatic.jade;

import net.romatic.com.collection.Models;
import net.romatic.jade.relation.Relation;
import net.romatic.jade.relation.RelationUtils;
import net.romatic.query.QueryBuilder;

import java.lang.reflect.Field;
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

    private String[] eagerRelations;

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

    public Models<T> get() {

        //  attach scope

        //
        Map<String, Object>[] items = query.get();
        Models<T> models = hydrate(items);

        //  eager loading relation
        if (items.length > 0) {
            eagerLoadRelations(models);
        }

        return models;
    }

    public Models<T> hydrate(Map<String, Object>[] items) {
        return Stream.of(items).map(attr -> (T) model.newInstance(attr)).collect(Collectors.toCollection(Models::new));
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

    public <In> Builder whereIn(String column, In... values) {
        query.whereIn(column, values);

        return this;
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

    public Model getModel() {
        return model;
    }

    public Builder with(String... eagerRelations) {
        this.eagerRelations = eagerRelations;

        return this;
    }


    protected Models<T> eagerLoadRelations(Models<T> models) {

        if (null != this.eagerRelations) {
            for (String relation : this.eagerRelations) {
                models = eagerLoadRelation(models, relation);
            }
        }

        return models;
    }

    public Models<T> eagerLoadRelation(Models<T> models, String relationName) {

        Relation relation = getRelation(relationName);
        if (relation != null) {
            relation.withEagerConstraints(models);
            //System.out.println("relation SQL: " + relation.getBuilder().getQuery().toSQL());
            Models objs = relation.getBuilder().get();
            //System.out.println(JsonUtils.toJson(objs));

            return relation.match(models, objs, relationName);
        }

        return models;
    }

    public Relation getRelation(String relation) {

        Field field = null;
        try {
            field = model.getClass().getDeclaredField(relation);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        Relation relationBuilder = RelationUtils.getRelation(model, field);

        return relationBuilder;
    }
}
