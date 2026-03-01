package com.mickeytheq.hades.core.project.ui;

import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.ui.DialogEx;

import javax.swing.*;
import java.awt.*;

public class ProjectConfigurationDialog extends DialogEx {
    public ProjectConfigurationDialog(Window owner, ProjectContext projectContext, OpenAt openAt) {
        super(owner, false);

        setTitle("Hades project configuration"); // TODO: i18n

        EncounterSetsPanel encounterSetsPanel = new EncounterSetsPanel(projectContext);
        CollectionsPanel collectionsPanel = new CollectionsPanel(projectContext);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Encounter sets", encounterSetsPanel); // TODO: i18n
        tabbedPane.addTab("Collections", collectionsPanel); // TODO: i18n

        setContentComponent(tabbedPane);

        if (openAt != null) {
            switch (openAt) {
                case EncounterSet:
                    tabbedPane.setSelectedComponent(encounterSetsPanel);
                    break;

                case Collection:
                    tabbedPane.setSelectedComponent(collectionsPanel);
                    break;
            }
        }

        addCloseButton(); // TODO: i18n
    }

    public static void openDialog(Window owner, ProjectContext projectContext, OpenAt openAt) {
        new ProjectConfigurationDialog(owner, projectContext, openAt).showDialog();
    }

    public enum OpenAt {
        EncounterSet, Collection
    }
}
