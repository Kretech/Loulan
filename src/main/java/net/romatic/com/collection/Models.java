package net.romatic.com.collection;

import net.romatic.jade.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author huiren
 */
public class Models<M extends Model> extends ArrayList<M> {

    public Models<M> filter(Predicate<M> filter) {
        return this.stream().filter(filter).collect(Collectors.toCollection(Models::new));
    }

    public <K> Map<K, M> keyBy(String column) {
        Map<K, M> dict = new TreeMap<>();

        forEach(x -> dict.put(x.__get(column), x));

        return dict;
    }

    public <K> Map<K, Models<M>> groupBy(String column) {
        Map<K, Models<M>> dict = new TreeMap<>();

        forEach(model -> {
            K key = model.__get(column);
            Models<M> list = dict.get(key);
            if (list == null) {
                list = new Models<>();
            }

            list.add(model);

            dict.put(key, list);
        });

        return dict;
    }

    public <T> List<T> pluck(String column) {
        return stream().map(model -> (T) model.__get(column)).collect(Collectors.toList());
    }
}
