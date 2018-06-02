package net.romatic.com.exception;

import net.romatic.com.Closure;
import net.romatic.jade.relation.RelationUtils;

import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * @author huiren
 */
public class JadeException extends RuntimeException {

    @Override
    public String getMessage() {
        return message;
    }

    protected String message;

    protected Exception ex;

    public static JadeException of(String message, Exception e) {
        JadeException ex = of(e);
        ex.message = message;
        return ex;
    }

    public static JadeException of(Exception e) {

        if (e instanceof JadeException) {
            e = ((JadeException) e).getEx();
        }

        JadeException ex = new JadeException();
        ex.message = e.getMessage();
        ex.setEx(e);
        return ex;
    }

    public static void doIt(Closure closure) {
        try {
            closure.call();
        } catch (Exception e) {
            panic(e);
        }
    }

    public static void panic(Exception e) {
        throw of(e);
    }

    public Exception getEx() {
        return ex;
    }

    public void setEx(Exception ex) {
        this.ex = ex;
    }
}
