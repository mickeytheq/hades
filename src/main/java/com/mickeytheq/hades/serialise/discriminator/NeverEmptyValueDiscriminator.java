package com.mickeytheq.hades.serialise.discriminator;

public class NeverEmptyValueDiscriminator implements EmptyValueDiscriminator {
    @Override
    public boolean isEmpty(Object value) {
        return false;
    }
}
