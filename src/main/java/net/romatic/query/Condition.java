package net.romatic.query;

/**
 * @author zhrlnt@gmail.com
 */
public class Condition<T> {
    static String TYPE_BASIC = "basic";
    static String TYPE_IN = "in";
    static String TYPE_NULL = "null";
    static String TYPE_SUB = "sub";
    protected String type;
    protected String column;
    protected String operator;
    protected T value;
    protected String andOr;

    public static <T> Condition newInstance(String type, String column, String operator, T value, String andOr) {

        Condition instance = new Condition();
        instance.setType(type);
        instance.setColumn(column);
        instance.setOperator(operator);
        instance.setValue(value);
        instance.setAndOr(andOr);
        return instance;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getAndOr() {
        return andOr;
    }

    public void setAndOr(String andOr) {
        this.andOr = andOr.toUpperCase();
    }
}
