package com.mickeytheq.hades.core.project.ui;

import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.ProjectContexts;
import com.mickeytheq.hades.core.project.configuration.CollectionConfiguration;
import com.mickeytheq.hades.core.project.configuration.ProjectConfiguration;

import javax.swing.*;
import java.util.List;

public class CollectionsPanel extends TaggedImageInfoPanel<CollectionConfiguration> {
    private final ProjectContext projectContext;
    private final ProjectConfiguration projectConfiguration;

    public CollectionsPanel(ProjectContext projectContext) {
        this.projectContext = projectContext;
        this.projectConfiguration = projectContext.getProjectConfiguration();
    }

    @Override
    protected boolean performAdd() {
        return ProjectContexts.withContextReturn(projectContext, () -> {
            CollectionConfiguration collectionConfiguration = new CollectionConfiguration();

            if (!openEditorDialog(collectionConfiguration))
                return false;

            projectConfiguration.getCollectionConfigurations().add(collectionConfiguration);
            projectConfiguration.save();

            return true;
        });
    }

    @Override
    protected boolean performEdit(CollectionConfiguration entity) {
        return ProjectContexts.withContextReturn(projectContext, () -> {
            if (!openEditorDialog(entity))
                return false;

            projectConfiguration.save();
            return true;
        });
    }

    @Override
    protected boolean performDelete(CollectionConfiguration entity) {
        return ProjectContexts.withContextReturn(projectContext, () -> {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this collection", "Delete collection", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
                return false;

            projectConfiguration.getCollectionConfigurations().remove(entity);
            projectConfiguration.save();

            return true;
        });
    }

    @Override
    protected List<CollectionConfiguration> getEntityList() {
        return projectConfiguration.getCollectionConfigurations();
    }
}
