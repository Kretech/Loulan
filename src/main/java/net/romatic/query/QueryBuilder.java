package net.romatic.query;

import net.romatic.query.grammar.Grammar;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;
import java.util.function.Function;

public class QueryBuilder {

    /**
     * @todo 考虑简化 json 语法，比如 attr->auth_info->wx_open_id
     */
    public static String JSON_SEP = "->";
    protected static String[] allowedBindingTypes = new String[]{"where"};
    //protected Connection conn;
    protected Grammar grammar;
    protected String[] columns;
    protected List<OrderBy> orders = new ArrayList<>();
    protected String table;
    protected List<Condition> wheres = new ArrayList<>();
    protected long limit;
    protected long offset;
    protected Map<String, List<Object>> bindings = new HashMap<>();

    public List<OrderBy> getOrders() {
        return orders;
    }

    public QueryBuilder orderBy(String column) {
        return orderBy(column, true);
    }

    public QueryBuilder orderBy(String column, boolean inASC) {
        orders.add(OrderBy.newInstance(column, inASC));
        return this;
    }

    public List<Condition> getWheres() {
        return wheres;
    }

    public void setGrammar(Grammar grammar) {
        this.grammar = grammar;
    }

    public QueryBuilder select(String... columns) {
        this.columns = columns;

        return this;
    }

    public QueryBuilder from(String from) {
        table = from;

        return this;
    }

    public String[] getColumns() {
        return columns;
    }

    public String getTable() {
        return table;
    }

    public long getLimit() {
        return limit;
    }

    public long getOffset() {
        return offset;
    }

    public Map<String, List<Object>> getBindings() {
        return bindings;
    }

    public String[] getFlatBindings() {
        return getBindings().values().stream().flatMap(x -> x.stream()).map(x -> grammar.wrapValue(x, false)).toArray(String[]::new);
    }

    protected QueryBuilder appendBindings(Map<String, List<Object>> others) {
        for (Map.Entry entry : others.entrySet()) {
            ((List<Object>) entry.getValue()).forEach(x -> {
                addBinding((String) entry.getKey(), x);
            });
        }

        return this;
    }

    protected QueryBuilder addBinding(String type, Object... values) {
        List<Object> list = bindings.get(type);
        if (null == list) {
            list = new ArrayList<>();
        }

        for (Object value : values) {
            list.add(value);
        }

        bindings.put(type, list);

        return this;
    }

    public <T> QueryBuilder where(String column, T value) {
        return where(column, "=", value, "and");
    }

    public <T> QueryBuilder where(String column, String operator, T value) {
        return where(column, operator, value, "and");
    }

    public <T> QueryBuilder where(String column, String operator, T value, String andOr) {
        if (null == value) {
            return whereNull(column, andOr);
        }

        if (column.contains(JSON_SEP)) {
        }

        if (value instanceof Function) {
            return whereSub(column, operator, (Function) value, andOr);
        }

        return whereBasic(column, operator, value, andOr);
    }

    public <T> QueryBuilder whereIn(String column, T... values) {
        return whereIn(column, values, "and", true);
    }

    public <T> QueryBuilder whereNotIn(String column, T... values) {
        return whereIn(column, values, "and", false);
    }

    public <T> QueryBuilder orWhereIn(String column, T... values) {
        return whereIn(column, values, "or", true);
    }

    public <T> QueryBuilder orWhereNotIn(String column, T... values) {
        return whereIn(column, values, "or", false);
    }

    protected <T> QueryBuilder whereIn(String column, T[] value, String andOr, boolean inOrNot) {

        wheres.add(Condition.newInstance(Condition.TYPE_IN, column, (inOrNot ? "" : "not ") + "in", value, andOr));
        addBinding("where", value);

        return this;
    }

    protected <T> QueryBuilder whereBasic(String column, String operator, T value, String andOr) {
        wheres.add(Condition.newInstance(Condition.TYPE_BASIC, column, operator, value, andOr));
        addBinding("where", value);

        return this;
    }

    protected QueryBuilder newSub() {
        QueryBuilder sub = new QueryBuilder();
        sub.setGrammar(grammar);
        return sub;
    }

    protected QueryBuilder whereSub(String column, String operator, Function<QueryBuilder, QueryBuilder> value, String andOr) {
        QueryBuilder sub = newSub();

        sub = value.apply(sub);

        wheres.add(Condition.newInstance(Condition.TYPE_SUB, column, operator, sub, andOr));
        appendBindings(sub.getBindings());

        return this;
    }

    public QueryBuilder whereNull(String column) {
        return whereNull(column, "and");
    }

    public QueryBuilder whereNull(String column, String andOr) {

        wheres.add(Condition.newInstance(Condition.TYPE_NULL, column, null, null, andOr));

        return this;
    }

    public QueryBuilder limit(long limit) {
        this.limit = limit;

        return this;
    }

    public QueryBuilder offset(long offset) {
        this.offset = offset;

        return this;
    }

    public <T> T[] get() {
        T[] queryResults = (T[]) runSelect();
        return queryResults;
    }

    protected Map<String, String>[] runSelect() {
        List<Map<String, String>> results = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test", "root", "");

            String sql = toSQL();
            PreparedStatement statement = connection.prepareStatement(sql);
            String[] args = getFlatBindings();

            for (int i = 1; i <= args.length; i++) {
                statement.setObject(i, args[i - 1]);
            }

            statement.execute();
            ResultSet rs = statement.getResultSet();
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                Map<String, String> result = new TreeMap<>();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    result.put(meta.getColumnName(i), rs.getString(i));
                }
                results.add(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results.toArray(new Map[0]);
    }

    public String toSQL() {
        return grammar.compileSelect(this);
    }

    public String toSQLWithBound() {


        System.out.println(getFlatBindings());


        return "";
        //return grammar.compileSelect(this);
    }
}
