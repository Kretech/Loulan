package net.romatic.query.grammar;

import net.romatic.com.meta.MetaCaller;
import net.romatic.query.Condition;
import net.romatic.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author zhrlnt@gmail.com
 */
public class Grammar implements MetaCaller {

    protected static String[] components = {
            "aggregate",
            "columns",
            "from",
            "joins",
            "wheres",
            "groups",
            "havings",
            "orders",
            "limit",
            "offset",
            "lock",
    };

    public String compileSelect(QueryBuilder queryBuilder) {
        String sql = String.join(" ", filterNull(compileComponents(queryBuilder)));
        return sql;
    }

    protected String[] compileComponents(QueryBuilder queryBuilder) {
        String[] sqls = new String[components.length];
        for (int idx = 0; idx < components.length; idx++) {
            String component = components[idx];
            String methodName = "compile_" + component;
            sqls[idx] = __call(methodName, queryBuilder);
        }
        return sqls;
    }

    public String compile_aggregate(QueryBuilder queryBuilder) {
        return "";
    }

    public String compile_columns(QueryBuilder queryBuilder) {
        String[] columns = queryBuilder.getColumns();
        if (columns == null || columns.length < 1) {
            columns = new String[]{"*"};
        }
        return "SELECT " + String.join(", ", wrapColumn(columns));
    }

    public String compile_from(QueryBuilder queryBuilder) {
        return "FROM " + wrapColumn(queryBuilder.getTable());
    }

    public String compile_wheres(QueryBuilder queryBuilder) {

        if (null == queryBuilder.getWheres() || queryBuilder.getWheres().size() < 1) {
            return "";
        }

        String[] sqls = compileWheresToArray(queryBuilder);

        return "WHERE " + String.join(" ", sqls).replaceFirst("(?i)^(and |or )", "");
    }

    protected String[] compileWheresToArray(QueryBuilder queryBuilder) {
        return queryBuilder.getWheres().stream()
                .map(where ->
                        where.getAndOr() + " " + __call("compileWhere_" + where.getType(), queryBuilder, where)
                ).toArray(String[]::new);
    }

    public String compileWhere_basic(QueryBuilder queryBuilder, Condition where) {
        return wrapColumn(where.getColumn()) + " " + where.getOperator() + " " + wrapValue(where.getValue());
    }

    public String compileWhere_in(QueryBuilder queryBuilder, Condition where) {
        return wrapColumn(where.getColumn()) + " IN (" + wrapValue((Object[]) where.getValue()) + ")";
    }

    public String compileWhere_null(QueryBuilder queryBuilder, Condition where) {
        return wrapColumn(where.getColumn()) + " IS NULL";
    }

    public String compileWhere_sub(QueryBuilder queryBuilder, Condition where) {
        String sub = compileSelect((QueryBuilder) where.getValue());

        return wrapColumn(where.getColumn()) + " " + where.getOperator() + " (" + sub + ") ";
    }

    protected String wrapColumn(String column) {

        if (column == "*") {
            return column;
        }

        return "`" + column + "`";
    }

    protected String[] wrapColumn(String... columns) {
        return Stream.of(columns).map(this::wrapColumn).toArray(String[]::new);
    }

    public String wrapValue(Object value) {
        return wrapValue(value, true);
    }

    /**
     * 把数据包装成value
     *
     * @param value
     * @param asParam 是否作为SQL参数，会显示成"?"
     * @return
     */
    public String wrapValue(Object value, boolean asParam) {

        if (value instanceof Object[]) {
            return wrapValue((Object[]) value, asParam);
        }

        if (asParam) {
            return "?";
        }

        if (value instanceof String) {
            return (String) value;
        }

        return String.valueOf(value);
    }

    protected String wrapValue(Object[] value) {
        return wrapValue(value, true);
    }

    protected String wrapValue(Object[] value, boolean asParam) {
        return String.join(", ", Stream.of(value)
                .map(x -> wrapValue(x, asParam))
                //.map(x -> parameter(x, asParam))
                .toArray(String[]::new));
    }

    public String compile_limit(QueryBuilder queryBuilder) {
        if (queryBuilder.getLimit() < 1) {
            return "";
        }

        return "LIMIT " + queryBuilder.getLimit();
    }

    public String compile_offset(QueryBuilder queryBuilder) {
        if (queryBuilder.getOffset() < 1) {
            return "";
        }

        return "OFFSET " + queryBuilder.getOffset();
    }

    public String compile_orders(QueryBuilder queryBuilder) {
        if (null == queryBuilder.getOrders() || queryBuilder.getOrders().size() == 0) {
            return "";
        }

        String[] orders = queryBuilder.getOrders().stream().map(order -> wrapColumn(order.getColumn()) + " " + (order.isInASC() ? "ASE" : "DESC")).toArray(String[]::new);

        return "ORDER BY " + String.join(", ", orders);
    }

    protected String[] filterNull(String[] src) {
        List<String> dst = new ArrayList<>();
        for (String ele : src) {
            if (ele != null && ele.length() > 0) {
                dst.add(ele);
            }
        }
        String[] a = new String[dst.size()];
        return dst.toArray(a);
    }
}
