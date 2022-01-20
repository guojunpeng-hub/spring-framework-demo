package com.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xiaoguo
 * @date2022/1/20 0020 18:09
 */


@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoWired {
    String value() default "";
}
