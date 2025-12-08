package com.mickeytheq.hades.core.project;

public interface ProjectConfigurationProvider {
    ProjectConfiguration load();

    void save(ProjectConfiguration projectConfiguration);
}
