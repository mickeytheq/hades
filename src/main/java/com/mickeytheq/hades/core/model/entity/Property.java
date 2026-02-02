package com.mickeytheq.hades.core.model.entity;

import com.mickeytheq.hades.serialise.discriminator.EmptyValueDiscriminator;
import com.mickeytheq.hades.serialise.discriminator.NeverEmptyValueDiscriminator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Property {
    String value() default "";

    Class<? extends EmptyValueDiscriminator> discriminator() default NeverEmptyValueDiscriminator.class;

    boolean flatten() default false;
}
