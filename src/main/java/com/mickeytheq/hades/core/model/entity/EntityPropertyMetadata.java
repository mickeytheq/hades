package com.mickeytheq.hades.core.model.entity;

public interface EntityPropertyMetadata extends PropertyMetadata {
    @Override
    default boolean isValue() {
        return false;
    }

    @Override
    default boolean isEntity() {
        return true;
    }

    @Override
    default boolean isList() {
        return false;
    }

    EntityMetadata getEntityMetadata();
}
