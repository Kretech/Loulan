package net.romatic.jade.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author huiren
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {
    Class value() default net.romatic.jade.Query.class;
}
