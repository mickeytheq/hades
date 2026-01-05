package com.mickeytheq.hades.serialise;

public class NeverEmptyValueDiscriminator implements EmptyValueDiscriminator {
    @Override
    public boolean isEmpty(Object value) {
        return false;
    }
}
