package com.mickeytheq.ahlcg4j.core.model.entity;

public interface ListPropertyMetadata extends PropertyMetadata {
    @Override
    default boolean isValue() {
        return false;
    }

    @Override
    default boolean isEntity() {
        return false;
    }

    @Override
    default boolean isList() {
        return true;
    }

    Class<?> getListItemClass();
}
