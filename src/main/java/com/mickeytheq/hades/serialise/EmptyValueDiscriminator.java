package com.mickeytheq.hades.serialise;

// implementations must have a no-arg constructor
public interface EmptyValueDiscriminator {
    // return true is this value is considered empty/default and does not need to be serialised
    boolean isEmpty(Object value);
}
