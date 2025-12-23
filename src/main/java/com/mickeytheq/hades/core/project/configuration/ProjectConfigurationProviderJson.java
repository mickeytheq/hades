package com.mickeytheq.hades.core.project.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mickeytheq.hades.util.JsonUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProjectConfigurationProviderJson implements ProjectConfigurationProvider {
    private final Path projectFilePath;

    public ProjectConfigurationProviderJson(Path projectFilePath) {
        this.projectFilePath = projectFilePath;
    }

    @Override
    public ProjectConfiguration load() {
        ObjectMapper objectMapper = createObjectMapper();

        // create an empty default project file if nothing exists
        if (!Files.exists(projectFilePath)) {
            ProjectConfiguration projectConfiguration = new ProjectConfiguration();
            projectConfiguration.setProvider(this);
            projectConfiguration.save();
        }

        try {
            ProjectConfiguration projectConfiguration = objectMapper.readValue(projectFilePath.toFile(), ProjectConfiguration.class);
            projectConfiguration.setProvider(this);
            return projectConfiguration;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(ProjectConfiguration projectConfiguration) {
        try {
            createObjectMapper().writeValue(projectFilePath.toFile(), projectConfiguration);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectMapper createObjectMapper() {
        return JsonUtils.createDefaultObjectMapper(true);
    }
}
