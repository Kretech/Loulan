package net.romatic.com;

import net.romatic.jade.Builder;
import net.romatic.jade.Query;

/**
 * @author huiren
 */
@FunctionalInterface
public interface NestedQuery<Q extends Query> {

    void call(Q query);

//    Builder call(Q query, Object... args);

}
