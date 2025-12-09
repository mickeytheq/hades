package com.mickeytheq.hades.core.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mickeytheq.hades.util.JsonUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

public class ProjectConfigurationProviderJson implements ProjectConfigurationProvider {
    public static final String DEFAULT_FILENAME = "hades-project.json";
    private final Supplier<Path> pathSupplier;

    public ProjectConfigurationProviderJson(Supplier<Path> pathSupplier) {
        this.pathSupplier = pathSupplier;
    }

    @Override
    public ProjectConfiguration load() {
        Path pathToProjectFile = pathSupplier.get();

        ObjectMapper objectMapper = createObjectMapper();

        // create an empty default project file if nothing exists
        if (!Files.exists(pathToProjectFile)) {
            ProjectConfiguration projectConfiguration = new ProjectConfiguration();
            projectConfiguration.setProvider(this);
            projectConfiguration.save();
        }

        try {
            ProjectConfiguration projectConfiguration = objectMapper.readValue(pathToProjectFile.toFile(), ProjectConfiguration.class);
            projectConfiguration.setProvider(this);
            return projectConfiguration;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(ProjectConfiguration projectConfiguration) {
        try {
            createObjectMapper().writeValue(pathSupplier.get().toFile(), projectConfiguration);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectMapper createObjectMapper() {
        return JsonUtils.createDefaultObjectMapper(true);
    }
}
