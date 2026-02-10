package com.mickeytheq.hades.core.project.ui;

import com.mickeytheq.hades.core.project.configuration.EncounterSetConfiguration;
import com.mickeytheq.hades.core.project.configuration.ProjectConfiguration;

import javax.swing.*;
import java.util.List;

public class EncounterSetsPanel extends BaseEncounterSetsCollectionPanel<EncounterSetConfiguration> {
    private final ProjectConfiguration projectConfiguration;

    public EncounterSetsPanel(ProjectConfiguration projectConfiguration) {
        this.projectConfiguration = projectConfiguration;
    }

    @Override
    protected boolean performAdd() {
        EncounterSetConfiguration encounterSetConfiguration = new EncounterSetConfiguration();

        if (!openEditorDialog(encounterSetConfiguration))
            return false;

        projectConfiguration.getEncounterSetConfigurations().add(encounterSetConfiguration);
        projectConfiguration.save();

        return true;
    }

    @Override
    protected boolean performEdit(EncounterSetConfiguration entity) {
        if (!openEditorDialog(entity))
            return false;

        projectConfiguration.save();
        return true;
    }

    @Override
    protected boolean performDelete(EncounterSetConfiguration entity) {
        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this encounter set", "Delete encounter set", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
            return false;

        projectConfiguration.getEncounterSetConfigurations().remove(entity);
        projectConfiguration.save();

        return true;
    }

    @Override
    protected List<EncounterSetConfiguration> getEntityList() {
        return projectConfiguration.getEncounterSetConfigurations();
    }
}
