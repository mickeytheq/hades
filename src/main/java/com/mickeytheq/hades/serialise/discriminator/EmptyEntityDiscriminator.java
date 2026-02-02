package com.mickeytheq.hades.serialise.discriminator;

// used during Serialisation to allow entities
// to self-identify whether they are 'null'/empty and serialisation can be skipped
// for example a Statistic with a null or empty value can be skipped entirely as the perInvestigator setting
// is irrelevant in that case
public interface EmptyEntityDiscriminator {
    boolean isEmpty();
}
