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
     * 为开发者方便书写的静态方法 ^_^
     * * 强烈建议重写此方法，把 model 替换为 this，把计算转移到编译期，大大提高性能
     *
     * @param <T>
     * @return
     */
    public static <T extends Query, C> T query() {

        StackTraceElement[] called = new Throwable().getStackTrace();

        System.out.println(called);

        Class calledClass = Model.class;

        try {
            Model model = ((Model) calledClass.newInstance());
            return model.newQuery();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 由 Model 构建新的查询构造器（代理），里面会自动携带 model 的连接信息
     *
     * @param <Q>
     * @return
     */
    public <Q extends Query> Q newQuery() {
        return newQuery(getQueryProxyClazz());
    }

    public <Q extends Query> Q newQuery(Class queryProxyClass) {
        return newQueryProxy(new JadeProxyHandler(newJadeQuery()), queryProxyClass);
    }

    /**
     * 获取当前 Model 的 QueryProxy
     * <p>
     * 规则约定：
     * - 默认 Model 同包下的 ModelQuery，例如 User -> UserQuery
     * - ModelQuery 类不存在时，取 @see net.romatic.jade.Query
     * - 如需手动指定 @see net.romatic.jade.annotation.Query
     *
     * @return
     */
    private <Q extends Class<Query>> Q getQueryProxyClazz() {

        //  hard code for abstract model
        if (this.getClass().equals(Model.class)) {
            return (Q) Query.class;
        }

        //  custom by annotation
        net.romatic.jade.annotation.Query annotation = this.getClass().getAnnotation(net.romatic.jade.annotation.Query.class);
        if (annotation != null) {
            return (Q) annotation.value();
        }

        //  default ModelQuery or Query
        Class queryProxyClz = null;
        try {
            queryProxyClz = Class.forName(this.getClass().getName() + "Query");
        } catch (ClassNotFoundException e) {
            return (Q) Query.class;
        }

        return (Q) queryProxyClz;
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

    public <R> R __get(String key) {
        Class clz = this.getClass();

        try {
            Method getter = clz.getMethod("get" + WordUtils.upperFirst(key));
            return (R) getter.invoke(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            throw new RuntimeException(String.format("failed_setter: %s.set(%s, %s %s)", clz.getSimpleName(), key, value.getClass().getSimpleName(), value), e);
        }
    }

    public Builder newJadeQuery() {
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

    protected Builder newJadeBuilder() {
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
