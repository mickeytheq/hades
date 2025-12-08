package com.mickeytheq.hades.core.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mickeytheq.hades.util.JsonUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProjectConfiguration {
    private static ProjectConfiguration INSTANCE;

    private static Path pathToProjectFile;

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

    public static ProjectConfiguration get() {
        return ProjectConfiguration.INSTANCE;
    }

    public static ProjectConfiguration load(Path pathToProjectFile) {
        ProjectConfiguration.pathToProjectFile = pathToProjectFile;

        ObjectMapper objectMapper = createObjectMapper();

        // create an empty default project file if nothing exists
        if (!Files.exists(pathToProjectFile)) {
            ProjectConfiguration projectConfiguration = new ProjectConfiguration();
            projectConfiguration.save();
        }

        try {
            ProjectConfiguration projectConfiguration = objectMapper.readValue(pathToProjectFile.toFile(), ProjectConfiguration.class);
            INSTANCE = projectConfiguration;
            return projectConfiguration;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        if (pathToProjectFile == null)
            throw new RuntimeException("No project file path set");

        try {
            createObjectMapper().writeValue(pathToProjectFile.toFile(), this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectMapper createObjectMapper() {
        return JsonUtils.createDefaultObjectMapper(true);
    }
}
