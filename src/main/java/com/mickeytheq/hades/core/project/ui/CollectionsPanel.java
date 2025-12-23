package com.mickeytheq.hades.core.project.ui;

import com.mickeytheq.hades.core.project.configuration.CollectionInfo;
import com.mickeytheq.hades.core.project.configuration.ProjectConfiguration;

import javax.swing.*;
import java.util.List;

public class CollectionsPanel extends BaseEncounterSetsCollectionPanel<CollectionInfo> {
    private final ProjectConfiguration projectConfiguration;

    public CollectionsPanel(ProjectConfiguration projectConfiguration) {
        this.projectConfiguration = projectConfiguration;
    }

    @Override
    protected boolean performAdd() {
        CollectionInfo collectionInfo = new CollectionInfo();

        if (!openEditorDialog(collectionInfo))
            return false;

        projectConfiguration.getCollectionConfiguration().getCollectionInfos().add(collectionInfo);
        projectConfiguration.save();

        return true;
    }

    @Override
    protected boolean performEdit(CollectionInfo entity) {
        if (!openEditorDialog(entity))
            return false;

        projectConfiguration.save();
        return true;
    }

    @Override
    protected boolean performDelete(CollectionInfo entity) {
        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this collection", "Delete collection", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
            return false;

        projectConfiguration.getCollectionConfiguration().getCollectionInfos().remove(entity);
        projectConfiguration.save();

        return true;
    }

    @Override
    protected List<CollectionInfo> getEntityList() {
        return projectConfiguration.getCollectionConfiguration().getCollectionInfos();
    }
}
