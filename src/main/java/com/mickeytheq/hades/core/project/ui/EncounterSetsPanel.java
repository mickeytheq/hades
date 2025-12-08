package com.mickeytheq.hades.core.project.ui;

import com.mickeytheq.hades.core.project.EncounterSetInfo;
import com.mickeytheq.hades.core.project.ProjectConfiguration;

import javax.swing.*;
import java.util.List;

public class EncounterSetsPanel extends BaseEncounterSetsCollectionPanel<EncounterSetInfo> {
    private final ProjectConfiguration projectConfiguration;

    public EncounterSetsPanel(ProjectConfiguration projectConfiguration) {
        this.projectConfiguration = projectConfiguration;
    }

    @Override
    protected boolean performAdd() {
        EncounterSetInfo encounterSetInfo = new EncounterSetInfo();

        if (!openEditorDialog(encounterSetInfo))
            return false;

        projectConfiguration.getEncounterSetConfiguration().getEncounterSetInfos().add(encounterSetInfo);
        projectConfiguration.save();

        return true;
    }

    @Override
    protected boolean performEdit(EncounterSetInfo entity) {
        if (!openEditorDialog(entity))
            return false;

        projectConfiguration.save();
        return true;
    }

    @Override
    protected boolean performDelete(EncounterSetInfo entity) {
        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this encounter set", "Delete encounter set", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
            return false;

        projectConfiguration.getEncounterSetConfiguration().getEncounterSetInfos().remove(entity);
        projectConfiguration.save();

        return true;
    }

    @Override
    protected List<EncounterSetInfo> getEntityList() {
        return projectConfiguration.getEncounterSetConfiguration().getEncounterSetInfos();
    }
}
