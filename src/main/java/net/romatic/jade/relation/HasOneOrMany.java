package net.romatic.jade.relation;

import net.romatic.jade.Builder;
import net.romatic.jade.Model;
import net.romatic.jade.annotation.HasMany;
import net.romatic.utils.WordUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author huiren
 */
abstract public class HasOneOrMany extends RelationBuilder {

    @Override
    public void initBy(Field field) {
        HasMany annotation = field.getAnnotation(HasMany.class);

        Model related = RelationUtils.getRelated(field);
        Builder builder = related.newJadeQuery();

        String localKey = annotation.localKey();
        String relatedKey = annotation.relatedKey();
        if ("".equals(relatedKey)) {
            relatedKey = WordUtils.snake(field.getDeclaringClass().getSimpleName()) + "_id";
        }

        init(field.getName(), builder, local, localKey, relatedKey);
    }

    @Override
    public void withEagerConstraints() {
        this.builder.where(
                local.getTable() + "." + localKey,
                related.getTable() + "." + relatedKey
        );
    }

    @Override
    public void withEagerConstraints(List models) {
        this.builder.whereIn(
                relatedKey,
                models.stream().map(model -> ((Model) model).__get(WordUtils.camel(localKey))).toArray()
        );
    }
}
