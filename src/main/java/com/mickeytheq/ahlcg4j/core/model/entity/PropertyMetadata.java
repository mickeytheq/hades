package com.mickeytheq.ahlcg4j.core.model.entity;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface PropertyMetadata {
    String getName();

    Class<?> getPropertyClass();

    boolean isValue();

    boolean isEntity();

    boolean isList();

    Object getPropertyValue(Object parent);

    void setPropertyValue(Object parent, Object newValue);
}
