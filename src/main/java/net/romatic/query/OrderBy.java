package net.romatic.query;

/**
 * @author zhrlnt@gmail.com
 */
public class OrderBy {
    protected String column;
    protected boolean inASC;

    public static OrderBy newInstance(String column, boolean inASC) {
        OrderBy order = new OrderBy();
        order.setColumn(column);
        order.setInASC(inASC);
        return order;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public boolean isInASC() {
        return inASC;
    }

    public void setInASC(boolean inASC) {
        this.inASC = inASC;
    }
}
