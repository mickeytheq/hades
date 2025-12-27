package com.mickeytheq.hades.serialise;

// used during Serialisation to allow entities
// to self-identify whether they are 'null'/empty and serialisation can be skipped
// for example a Statistic with a null value can be skipped entirely as the perInvestigator setting
// is irrelevant in that case
public interface NullDiscriminator {
    boolean isNull();
}
