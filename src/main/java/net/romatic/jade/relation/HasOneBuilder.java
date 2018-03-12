package net.romatic.jade.relation;

import net.romatic.com.collection.Models;
import net.romatic.jade.Builder;
import net.romatic.jade.Model;
import net.romatic.jade.annotation.HasOne;
import net.romatic.utils.JsonUtils;
import net.romatic.utils.WordUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author huiren
 */
public class HasOneBuilder extends HasOneOrMany {

    @Deprecated
    public static HasOneBuilder newHasOne(String name, Model local, String localKey, String relatedKey, Builder foreignBuilder) {

        HasOneBuilder hasOne = new HasOneBuilder();

        hasOne.init(name, foreignBuilder, local, localKey, relatedKey);

        return hasOne;
    }

    @Override
    public Models match(Models models, Models relateds, String relationName) {

        //  字段名 —> 属性名
        String propertyName = WordUtils.camel(relatedKey);

        Map<String, Model> dict = relateds.keyBy(propertyName);
        System.out.println(JsonUtils.toJson(dict));

        models.forEach(model -> {

            System.out.println(JsonUtils.toJson(model));

            try {
                ((Model) model).__set(relationName, dict.get(((Model) model).__get(localKey)));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        });

        return models;
    }

}
