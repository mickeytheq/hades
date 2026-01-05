package com.mickeytheq.hades.core.model.entity;

/**
 * A property of a Java class that is owned by an {@link EntityMetadata}
 */
public interface PropertyMetadata {
    String getName();

    Class<?> getPropertyClass();

    boolean isValue();

    boolean shouldInclude(Object value);

    boolean isEntity();

    boolean isList();

    Object getPropertyValue(Object parent);

    void setPropertyValue(Object parent, Object newValue);

    default EntityMetadata asEntity() {
        if (!isEntity())
            throw new RuntimeException("Property '" + getName() + "' is not an entity");

        return ((EntityPropertyMetadata)this).getEntityMetadata();
    }
}
