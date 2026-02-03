package com.mickeytheq.hades.core.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Model {
    // unique type code used to identify the model in serialised form
    String typeCode();

    // version of this model
    // when breaking changes are made to the model this number must be incremented and an upgrade provided from the previous
    // version to the new version
    int version();
}
