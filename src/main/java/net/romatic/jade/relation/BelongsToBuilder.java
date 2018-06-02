package net.romatic.jade.relation;

import net.romatic.com.collection.Models;
import net.romatic.jade.Builder;
import net.romatic.jade.Model;
import net.romatic.jade.annotation.BelongsTo;
import net.romatic.utils.WordUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author huiren
 */
public class BelongsToBuilder extends RelationBuilder {

    @Deprecated
    public static BelongsToBuilder newBelongsTo(String name, Model local, String localKey, String relatedKey, Builder foreignBuilder) {
        BelongsToBuilder belongsTo = new BelongsToBuilder();

        belongsTo.init(name, foreignBuilder, local, localKey, relatedKey);

        return belongsTo;
    }

    @Override
    public void initBy(Field field) {
        BelongsTo annotation = field.getAnnotation(BelongsTo.class);

        Model related = RelationUtils.getRelated(field);
        Builder builder = related.newJadeQuery();

        String localKey = annotation.localKey();
        String relatedKey = annotation.relatedKey();
        if ("".equals(localKey)) {
            localKey = field.getName() + "_id";
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
    public Models match(Models models, Models relateds, String relationName) {

        Map<String, Model> dict = relateds.keyBy("id");

        models.forEach(model -> {
            try {
                ((Model) model).__set(relationName, dict.get(((Model) model).__get(WordUtils.camel(localKey))));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        });

        return models;
    }

    @Override
    public void withEagerConstraints(List models) {
//        System.out.println(models.get(0) + "relatedKey:" + relatedKey);
//        System.out.println(JsonUtils.toJson(models.get(0)));
        this.builder.whereIn(
                //related.getTable() + "." + relatedKey,
                relatedKey,
                models.stream().map(model -> ((Model) model).__get(WordUtils.camel(localKey))).toArray()
        );
    }
}
