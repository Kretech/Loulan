package net.romatic.jade;

import net.romatic.com.ShouldToJson;
import net.romatic.com.carbon.Carbon;
import net.romatic.com.reflect.ReflectUtils;
import net.romatic.jade.annotation.Column;
import net.romatic.jade.annotation.Table;
import net.romatic.query.QueryBuilder;
import net.romatic.query.grammar.MySqlGrammar;
import net.romatic.utils.JsonUtils;
import net.romatic.utils.WordUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author huiren
 */
@Table()
abstract public class Model implements ShouldToJson {

    @Column("created_at")
    protected Carbon createdAt;

    @Column("updated_at")
    protected Carbon updatedAt;

    /**
     * 软删的默认字段
     */
    @Column("deleted_at")
    protected Carbon deletedAt;

    /**
     * 强烈建议重写此方法，把 calledClass 替换为指定的 Model，把计算转移到编译期
     *
     * @param <T>
     * @return
     */
    public static <T extends Query> T query() {
        Class calledClass = Model.class;

        try {
            Model model = ((Model) calledClass.newInstance());
            return model.newQueryProxy(new JadeProxyHandler(model.newQuery()), Class.forName(calledClass.getName() + "Query"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getTable() {
        String table = getTableNameByAnnotation();

        if (null != table && table.length() > 0) {
            return table;
        }

        return WordUtils.snake(getClass().getSimpleName());
    }

    protected String getTableNameByAnnotation() {
        Table table = this.getClass().getAnnotation(Table.class);
        if (table != null) {
            return table.name();
        }

        return "";
    }

    public Carbon getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Carbon createdAt) {
        this.createdAt = createdAt;
    }

    public Carbon getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Carbon updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Carbon getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Carbon deletedAt) {
        this.deletedAt = deletedAt;
    }

    public <T extends Model> T newInstance(Map<String, Object> attr) {
        T obj;
        try {
            obj = this.getClass().newInstance().fill(attr);
        } catch (Exception e) {
            throw (RuntimeException) e;
        }
        return obj;
    }

    public <T extends Model> T fill(Map<String, Object> attr) {

        ReflectUtils.listFields(this.getClass()).stream().forEach(field -> {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                //  自定义的字段名 => 类的属性名
                String search = column.value();
                String replace = field.getName();
                if (attr.get(search) != null) {
                    attr.put(replace, attr.get(search));
                    attr.remove(search);
                }
            }
        });

        String json = JsonUtils.toJson(attr);

        return (T) parseJson(json);
    }

    /**
     * @param key
     * @param value
     * @throws NoSuchFieldException 字段不存在
     */
    public void __set(String key, Object value) throws NoSuchFieldException {
        Class clz = this.getClass();

        Field field = clz.getDeclaredField(key);

        try {
            Method setter = clz.getMethod("set" + WordUtils.upperFirst(key), field.getType());
            setter.invoke(this, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Builder newQuery() {
        return newJadeBuilder();
    }

    /**
     * @param handler 实际的 Jade Builder
     * @param classes
     * @param <T>
     * @return
     */
    protected <T> T newQueryProxy(InvocationHandler handler, Class... classes) {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), classes, handler);
    }

    public Builder newJadeBuilder() {
        Builder builder = new Builder(newQueryBuilder());
        builder.setModel(this);

        return builder;
    }

    public QueryBuilder newQueryBuilder() {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.setGrammar(new MySqlGrammar());

        return queryBuilder;
    }
}
