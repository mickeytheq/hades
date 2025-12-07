package com.mickeytheq.hades.core.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mickeytheq.hades.util.JsonUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProjectConfigurationImpl implements ProjectConfiguration {
    private Path projectConfiguationPath;

    public void setProjectConfiguationPath(Path projectConfiguationPath) {
        this.projectConfiguationPath = projectConfiguationPath;
    }

    @JsonProperty("EncounterSets")
    private EncounterSetConfiguration encounterSetConfiguration = new EncounterSetConfiguration();

    @JsonProperty("Collections")
    private CollectionConfiguration collectionConfiguration = new CollectionConfiguration();

    @Override
    public EncounterSetConfiguration getEncounterSetConfiguration() {
        return encounterSetConfiguration;
    }

    @Override
    public CollectionConfiguration getCollectionConfiguration() {
        return collectionConfiguration;
    }

    public static ProjectConfiguration load(Path pathToProjectFile) {
        ObjectMapper objectMapper = createObjectMapper();

        // create an empty default project file if nothing exists
        if (!Files.exists(pathToProjectFile)) {
            ProjectConfigurationImpl projectConfiguration = new ProjectConfigurationImpl();
            projectConfiguration.setProjectConfiguationPath(pathToProjectFile);
            projectConfiguration.save();
        }

        try {
            ProjectConfigurationImpl projectConfiguration = objectMapper.readValue(pathToProjectFile.toFile(), ProjectConfigurationImpl.class);
            projectConfiguration.setProjectConfiguationPath(pathToProjectFile);
            return projectConfiguration;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        try {
            createObjectMapper().writeValue(projectConfiguationPath.toFile(), this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectMapper createObjectMapper() {
        return JsonUtils.createDefaultObjectMapper(true);
    }
}
