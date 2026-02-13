package com.mickeytheq.hades.core.project.ui;

import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.ui.DialogEx;

import javax.swing.*;
import java.awt.*;

public class ProjectConfigurationDialog extends DialogEx {
    public ProjectConfigurationDialog(Frame owner, ProjectContext projectContext) {
        super(owner, false);

        setTitle("Hades project configuration"); // TODO: i18n

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Encounter sets", new EncounterSetsPanel(projectContext)); // TODO: i18n
        tabbedPane.addTab("Collections", new CollectionsPanel(projectContext)); // TODO: i18n

        setContentComponent(tabbedPane);

        addCloseButton(); // TODO: i18n
    }

    public static void openDialog(Frame owner, ProjectContext projectContext) {
        new ProjectConfigurationDialog(owner, projectContext).showDialog();
    }
}
