package com.mickeytheq.hades.core.model.entity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Property {
    String value() default "";

    boolean flatten() default false;
}
