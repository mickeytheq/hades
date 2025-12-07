package com.mickeytheq.hades.core.project;

public interface ProjectConfiguration {
    EncounterSetConfiguration getEncounterSetConfiguration();

    CollectionConfiguration getCollectionConfiguration();

    // TODO: cycle/project name?
    // TODO: look at the arkham.build project metadata for ideas of what could be here

    void save();
}
