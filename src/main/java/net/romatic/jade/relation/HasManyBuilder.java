package net.romatic.jade.relation;

import net.romatic.com.collection.Models;
import net.romatic.jade.Builder;
import net.romatic.jade.Model;
import net.romatic.jade.annotation.HasMany;
import net.romatic.jade.annotation.HasOne;
import net.romatic.utils.JsonUtils;
import net.romatic.utils.WordUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author huiren
 */
public class HasManyBuilder extends HasOneOrMany {

    @Override
    public Models match(Models models, Models relateds, String relationName) {

        //  字段名 —> 属性名
        String propertyName = WordUtils.camel(relatedKey);

        Map<String, Models<Model>> dict = relateds.groupBy(propertyName);
        System.out.println(JsonUtils.toJson(dict));

        models.forEach(model -> {

            try {
                ((Model) model).__set(relationName, dict.get(((Model) model).__get(localKey)));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        });

        return models;
    }
}
