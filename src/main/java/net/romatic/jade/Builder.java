package net.romatic.jade;

import net.romatic.com.Closure;
import net.romatic.com.NestedQuery;
import net.romatic.com.collection.Models;
import net.romatic.com.exception.JadeException;
import net.romatic.jade.annotation.Relation;
import net.romatic.jade.relation.RelationBuilder;
import net.romatic.jade.relation.RelationUtils;
import net.romatic.query.QueryBuilder;
import net.romatic.utils.JsonUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhrlnt@gmail.com
 */
public class Builder<M extends Model> implements Query {

    private QueryBuilder query;

    private M model;

    private String[] eagerRelations;

    public Builder(QueryBuilder query) {
        this.query = query;
    }

    public Builder take(long limit) {
        return limit(limit);
    }

    @Override
    public Builder limit(long limit) {
        return this;
    }

    @Override
    public M find(long id) {
        return (M) where("id", id).first();
    }

    @Override
    public M first() {
        return (M) limit(1).get().get(0);
    }

    @Override
    public Models<M> get() {

        //  attach scope

        //
        Map<String, Object>[] items = query.get();
        Models<M> models = hydrate(items);

        //  eager loading relation
        if (items.length > 0) {
            eagerLoadRelations(models);
        }

        return models;
    }

    public Models<M> hydrate(Map<String, Object>[] items) {
        return Stream.of(items).map(attr -> (M) model.newInstance(attr)).collect(Collectors.toCollection(Models::new));
    }

    @Override
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

        return this;
    }

    @Override
    public Builder has(String relationName, NestedQuery closure) {

        RelationBuilder relation = null;

        try {
            relation = RelationUtils.getRelation(model, relationName);
        } catch (NoSuchFieldException e) {
            JadeException.panic(e);
        }

        Builder hasQuery = relation.getHasInQuery(relation.getBuilder());
        hasQuery.callScope(closure);

        System.out.println("T: " + hasQuery.getQuery().toSQL());

        where(relation.getLocalKey(),"in", hasQuery.getQuery());

        return this;
    }

    @Override
    public QueryBuilder getQuery() {
        return query;
    }

    public void setModel(M model) {
        this.model = model;

        query.from(model.getTable());
    }

    public Model getModel() {
        return model;
    }

    @Override
    public Builder with(String... eagerRelations) {
        this.eagerRelations = eagerRelations;

        return this;
    }

    protected Models<M> eagerLoadRelations(Models<M> models) {

        if (null != this.eagerRelations) {
            for (String relation : this.eagerRelations) {
                models = eagerLoadRelation(models, relation);
            }
        }

        return models;
    }

    public Models<M> eagerLoadRelation(Models<M> models, String relationName) {

        RelationBuilder relation = getRelation(relationName);
        if (relation != null) {
            relation.withEagerConstraints(models);
            //System.out.println("relation SQL: " + relation.getBuilder().getQuery().toSQL());
            Models objs = relation.getBuilder().get();
            System.out.println(JsonUtils.toJson(objs));

            return relation.match(models, objs, relationName);
        }

        return models;
    }

    public <R extends RelationBuilder> R getRelation(String relation) {

        Field field = null;
        try {
            field = model.getClass().getDeclaredField(relation);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        R relationBuilder = RelationUtils.getRelation(field);

        return relationBuilder;
    }

    public void callScope(NestedQuery query) {
        query.call(this);
    }
}
