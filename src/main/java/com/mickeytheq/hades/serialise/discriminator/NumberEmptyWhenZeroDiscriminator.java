package com.mickeytheq.hades.serialise.discriminator;

public class NumberEmptyWhenZeroDiscriminator implements EmptyValueDiscriminator {
    @Override
    public boolean isEmpty(Object value) {
        if (!(value instanceof Number))
            throw new RuntimeException("Can only be applied to a number class");

        return ((Number)value).longValue() == 0;
    }
}
