package net.romatic.jade.relation;

import net.romatic.com.collection.Models;
import net.romatic.jade.Model;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author huiren
 */
public class BelongsToManyBuilder extends RelationBuilder {
    @Override
    public void initBy(Field field) {

    }

    @Override
    public void withEagerConstraints() {

    }

    @Override
    public void withEagerConstraints(List<? extends Model> models) {

    }

    @Override
    public Models match(Models<? extends Model> models, Models relateds, String relationName) {
        return null;
    }
}
