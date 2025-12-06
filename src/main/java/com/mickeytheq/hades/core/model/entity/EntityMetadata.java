package com.mickeytheq.hades.core.model.entity;

import java.util.List;

/**
 * Represents metadata on top of a Java Class. Typically used for serialisation or other logic that wants to operate
 * once step removed from the specific Class.
 */
public interface EntityMetadata {

    Class<?> getEntityClass();

    List<PropertyMetadata> getProperties();

    default PropertyMetadata getProperty(String name) {
        return getProperties().stream().filter(o -> o.getName().equals(name)).findFirst()
                .orElseThrow(() -> new RuntimeException("No property '" + name + "' present on entity class '" + getEntityClass() + "'"));
    }
}
