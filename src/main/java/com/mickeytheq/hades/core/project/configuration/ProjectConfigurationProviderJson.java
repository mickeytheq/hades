package com.mickeytheq.hades.core.project.configuration;

import com.fasterxml.jackson.databind.JsonNode;
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
            JsonNode projectConfigurationRootNode = objectMapper.readTree(projectFilePath.toFile());

            checkVersionAndPerformUpgrades(projectConfigurationRootNode);

            ProjectConfiguration projectConfiguration = objectMapper.treeToValue(projectConfigurationRootNode, ProjectConfiguration.class);
            projectConfiguration.setProvider(this);
            return projectConfiguration;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkVersionAndPerformUpgrades(JsonNode projectConfigurationRootNode) {
        JsonNode versionNode = projectConfigurationRootNode.get(ProjectConfigurationPropertyNames.VERSION);

        if (versionNode == null)
            throw new RuntimeException("Project configuration has no " + ProjectConfigurationPropertyNames.VERSION + " property at the root level");

        if (!versionNode.isIntegralNumber())
            throw new RuntimeException("Project configuration has a " + ProjectConfigurationPropertyNames.VERSION + " property at the root level but it is not a number");

        int version = versionNode.asInt();

        if (version > ProjectConfiguration.CURRENT_VERSION)
            throw new RuntimeException("Project configuration has a " + ProjectConfigurationPropertyNames.VERSION + " property with a value of " + version + " which is newer than the current software version of " + ProjectConfiguration.CURRENT_VERSION + ". Upgrading to a newer version of Hades may solve this");

        // version matches nothing to do
        if (version == ProjectConfiguration.CURRENT_VERSION)
            return;

        // otherwise it is an older version so perform upgrades
        // TODO: add upgrade capability when needed - similar to how CardFaces work, each upgrade should handle one version step
        throw new UnsupportedOperationException("Project configuration has a " + ProjectConfigurationPropertyNames.VERSION + " property with a value of " + version + " which is older than the current software version of " + ProjectConfiguration.CURRENT_VERSION + ". However upgrades have not been implemented yet.");
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
