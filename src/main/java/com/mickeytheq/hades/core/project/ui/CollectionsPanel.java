package com.mickeytheq.hades.core.project.ui;

import com.mickeytheq.hades.core.project.configuration.CollectionConfiguration;
import com.mickeytheq.hades.core.project.configuration.ProjectConfiguration;

import javax.swing.*;
import java.util.List;

public class CollectionsPanel extends BaseEncounterSetsCollectionPanel<CollectionConfiguration> {
    private final ProjectConfiguration projectConfiguration;

    public CollectionsPanel(ProjectConfiguration projectConfiguration) {
        this.projectConfiguration = projectConfiguration;
    }

    @Override
    protected boolean performAdd() {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration();

        if (!openEditorDialog(collectionConfiguration))
            return false;

        projectConfiguration.getCollectionConfigurations().add(collectionConfiguration);
        projectConfiguration.save();

        return true;
    }

    @Override
    protected boolean performEdit(CollectionConfiguration entity) {
        if (!openEditorDialog(entity))
            return false;

        projectConfiguration.save();
        return true;
    }

    @Override
    protected boolean performDelete(CollectionConfiguration entity) {
        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this collection", "Delete collection", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
            return false;

        projectConfiguration.getCollectionConfigurations().remove(entity);
        projectConfiguration.save();

        return true;
    }

    @Override
    protected List<CollectionConfiguration> getEntityList() {
        return projectConfiguration.getCollectionConfigurations();
    }
}
