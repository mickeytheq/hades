package com.mickeytheq.ahlcg4j.core.model.entity;

import java.util.List;

public interface EntityMetadata {

    Class<?> getEntityClass();

    List<PropertyMetadata> getProperties();

    default PropertyMetadata getProperty(String name) {
        return getProperties().stream().filter(o -> o.getName().equals(name)).findFirst()
                .orElseThrow(() -> new RuntimeException("No property '" + name + "' present on entity classs '" + getEntityClass() + "'"));
    }
}
