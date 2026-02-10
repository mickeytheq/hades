package com.mickeytheq.hades.core.project.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ProjectConfiguration {
    public static final int CURRENT_VERSION = 1;

    @JsonProperty(ProjectConfigurationPropertyNames.VERSION)
    private int version = CURRENT_VERSION;

    @JsonProperty("EncounterSets")
    private final List<EncounterSetConfiguration> encounterSetConfigurations = new ArrayList<>();

    @JsonProperty("Collections")
    private final List<CollectionConfiguration> collectionConfigurations = new ArrayList<>();

    @JsonProperty("Scenarios")
    private final List<ScenarioConfiguration> scenarioConfigurations = new ArrayList<>();

    @JsonProperty("Cycles")
    private final List<CycleConfiguration> cycleConfigurations = new ArrayList<>();

    public List<EncounterSetConfiguration> getEncounterSetConfigurations() {
        return encounterSetConfigurations;
    }

    public List<CollectionConfiguration> getCollectionConfigurations() {
        return collectionConfigurations;
    }

    public List<ScenarioConfiguration> getScenarioConfigurations() {
        return scenarioConfigurations;
    }

    public List<CycleConfiguration> getCycleConfigurations() {
        return cycleConfigurations;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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
