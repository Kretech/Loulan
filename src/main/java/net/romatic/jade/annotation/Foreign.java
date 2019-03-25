package net.romatic.jade.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author huiren
 */
@Retention(RetentionPolicy.RUNTIME)
@Relation
public @interface Foreign {
    String keyName() default "";
}
