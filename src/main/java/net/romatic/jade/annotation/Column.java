package net.romatic.jade.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author zhrlnt@gmail.com
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String value();
}
