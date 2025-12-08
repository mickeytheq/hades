package com.mickeytheq.hades.core.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mickeytheq.hades.util.JsonUtils;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;
import org.apache.commons.lang3.concurrent.Memoizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

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

    private static ProjectConfigurationProvider provider;

    private static final LazyInitializer<ProjectConfiguration> INSTANCE = new LazyInitializer<ProjectConfiguration>() {
        @Override
        protected ProjectConfiguration initialize() {
            if (provider == null)
                throw new RuntimeException("ProjectConfigurationProvider not specified");

            return provider.load();
        }
    };

    public static void setProvider(ProjectConfigurationProvider provider) {
        ProjectConfiguration.provider = provider;
    }

    // this needs to be a lazy compute
    public static ProjectConfiguration get() {
        try {
            return INSTANCE.get();
        } catch (ConcurrentException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        provider.save(this);
    }
}
