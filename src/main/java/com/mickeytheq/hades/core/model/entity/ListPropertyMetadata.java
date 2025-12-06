package com.mickeytheq.hades.core.model.entity;

// TODO: do we need support for lists? If not remove this and the isList() method and any associated logic
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
