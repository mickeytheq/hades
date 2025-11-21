package com.mickeytheq.strangeeons.ahlcg4j.entity;

import java.util.List;
import java.util.Map;

public interface EntityMetadata {

    Class<?> getEntityClass();

    List<PropertyMetadata> getProperties();

    default PropertyMetadata getProperty(String name) {
        return getProperties().stream().filter(o -> o.getName().equals(name)).findFirst()
                .orElseThrow(() -> new RuntimeException("No property '" + name + "' present on entity classs '" + getEntityClass() + "'"));
    }
}
