package com.mickeytheq.hades.serialise.discriminator;

public class BooleanEmptyWhenFalseDiscriminator implements EmptyValueDiscriminator {
    @Override
    public boolean isEmpty(Object value) {
        return !Boolean.TRUE.equals(value);
    }
}
