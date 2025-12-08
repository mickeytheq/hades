package com.mickeytheq.hades.core.project;

public class ProjectConfigurationProviderStatic implements ProjectConfigurationProvider {
    private final ProjectConfiguration projectConfiguration;

    public ProjectConfigurationProviderStatic(ProjectConfiguration projectConfiguration) {
        this.projectConfiguration = projectConfiguration;
    }

    @Override
    public ProjectConfiguration load() {
        return projectConfiguration;
    }

    @Override
    public void save(ProjectConfiguration projectConfiguration) {
        // do nothing
    }
}
