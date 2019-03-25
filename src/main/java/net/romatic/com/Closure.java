package net.romatic.com;

/**
 * @author huiren
 */
@FunctionalInterface
public interface Closure<R> {
    R call();
}
