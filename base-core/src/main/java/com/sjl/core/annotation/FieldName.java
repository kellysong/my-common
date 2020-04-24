package com.sjl.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename FieldName.java
 * @time 2019/6/13 17:26
 * @copyright(C) 2019 song
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface FieldName {
    String value() default "";
    boolean Ignore() default false;
}