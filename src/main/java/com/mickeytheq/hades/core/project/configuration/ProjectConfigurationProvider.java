package com.mickeytheq.hades.core.project.configuration;

public interface ProjectConfigurationProvider {
    ProjectConfiguration load();

    void save(ProjectConfiguration projectConfiguration);
}
