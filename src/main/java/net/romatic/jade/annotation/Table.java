package net.romatic.jade.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author huiren
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    String name() default "";
}
