package com.mickeytheq.hades.serialise;

public class BooleanFalseDiscriminator implements EmptyValueDiscriminator {
    @Override
    public boolean isEmpty(Object value) {
        return !Boolean.TRUE.equals(value);
    }
}
