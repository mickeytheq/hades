package com.mickeytheq.hades.core.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;

public class ProjectConfiguration {
    @JsonProperty("EncounterSets")
    private EncounterSetConfiguration encounterSetConfiguration = new EncounterSetConfiguration();

    @JsonProperty("Collections")
    private CollectionConfiguration collectionConfiguration = new CollectionConfiguration();

    public EncounterSetConfiguration getEncounterSetConfiguration() {
        return encounterSetConfiguration;
    }

    public CollectionConfiguration getCollectionConfiguration() {
        return collectionConfiguration;
    }

    // provider is stored to callback to the save function
    // this can't be in the constructor as we serialise this class
    private ProjectConfigurationProvider provider;

    public void setProvider(ProjectConfigurationProvider provider) {
        this.provider = provider;
    }

    public void save() {
        if (provider == null)
            throw new RuntimeException("No provider available to allow saving");

        provider.save(this);
    }
}
