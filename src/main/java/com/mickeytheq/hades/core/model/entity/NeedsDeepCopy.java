package com.mickeytheq.hades.core.model.entity;

// interface that indicates an value type requires a deep copy
public interface NeedsDeepCopy {
    Object deepCopy();
}
