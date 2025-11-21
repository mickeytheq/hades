package com.mickeytheq.strangeeons.ahlcg4j.entity;

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
