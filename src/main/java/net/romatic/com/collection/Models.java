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

    /**
     * 提取索引
     *
     * @param column
     * @param <K>
     * @return
     * @see
     */
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

    /**
     * 提取字段
     * 数据源：[{"id":3},{"id":4,"other":"..."}]
     * 输入：id
     * 输出：[3, 4]
     *
     * @param column
     * @param <T>
     * @return
     */
    public <T> List<T> pluck(String column) {
        return stream().map(model -> (T) model.__get(column)).collect(Collectors.toList());
    }

    public <K, V> Map<K, V> pluck(String keyColumn, String valueColumn) {
        Map<K, V> dict = new TreeMap<>();
        {
            stream().forEach(model -> {
                dict.put(
                        model.__get(keyColumn),
                        model.__get(valueColumn)
                );
            });
        }

        return dict;
    }

    public <V> Models<M> where(String key, V value) {
        return filter(model -> value.equals(model.__get(key)));
    }

    public <V> Models<M> where(String key, String op, V value) {
        return filter(model -> {
            switch (op) {
                case "!=":
                case "<>":
                    return !value.equals(model.__get(key));

                case ">":
                    //  @todo not null
                    return Long.valueOf(value.toString()) > Long.valueOf(model.__get(key).toString());

                case "<":
                    //  @todo not null
                    return Long.valueOf(value.toString()) < Long.valueOf(model.__get(key).toString());

                case "=":
                case "==":
                default:
                    return value.equals(model.__get(key));
            }
        });
    }
}
