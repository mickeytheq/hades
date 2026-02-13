package com.mickeytheq.hades.core.project.ui;

import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.ProjectContexts;
import com.mickeytheq.hades.core.project.configuration.EncounterSetConfiguration;
import com.mickeytheq.hades.core.project.configuration.ProjectConfiguration;

import javax.swing.*;
import java.util.List;

public class EncounterSetsPanel extends TaggedImageInfoPanel<EncounterSetConfiguration> {
    private final ProjectContext projectContext;
    private final ProjectConfiguration projectConfiguration;

    public EncounterSetsPanel(ProjectContext projectContext) {
        this.projectContext = projectContext;
        this.projectConfiguration = projectContext.getProjectConfiguration();
    }

    @Override
    protected boolean performAdd() {
        return ProjectContexts.withContextReturn(projectContext, () -> {
            EncounterSetConfiguration encounterSetConfiguration = new EncounterSetConfiguration();

            if (!openEditorDialog(encounterSetConfiguration))
                return false;

            projectConfiguration.getEncounterSetConfigurations().add(encounterSetConfiguration);
            projectConfiguration.save();

            return true;
        });
    }

    @Override
    protected boolean performEdit(EncounterSetConfiguration entity) {
        return ProjectContexts.withContextReturn(projectContext, () -> {
            if (!openEditorDialog(entity))
                return false;

            projectConfiguration.save();
            return true;
        });
    }

    @Override
    protected boolean performDelete(EncounterSetConfiguration entity) {
        return ProjectContexts.withContextReturn(projectContext, () -> {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this encounter set", "Delete encounter set", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
                return false;

            projectConfiguration.getEncounterSetConfigurations().remove(entity);
            projectConfiguration.save();

            return true;
        });
    }

    @Override
    protected List<EncounterSetConfiguration> getEntityList() {
        return projectConfiguration.getEncounterSetConfigurations();
    }
}
