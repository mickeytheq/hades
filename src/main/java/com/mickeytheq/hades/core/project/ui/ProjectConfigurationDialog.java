package com.mickeytheq.hades.core.project.ui;

import com.mickeytheq.hades.core.project.configuration.ProjectConfiguration;
import com.mickeytheq.hades.ui.DialogWithButtons;

import javax.swing.*;
import java.awt.*;

public class ProjectConfigurationDialog extends DialogWithButtons {
    private final ProjectConfiguration projectConfiguration;

    public ProjectConfigurationDialog(Frame owner, ProjectConfiguration projectConfiguration) {
        super(owner, false);
        this.projectConfiguration = projectConfiguration;

        setTitle("Hades project configuration"); // TODO: i18n

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Encounter sets", new EncounterSetsPanel(projectConfiguration)); // TODO: i18n
        tabbedPane.addTab("Collections", new CollectionsPanel(projectConfiguration)); // TODO: i18n

        setContentComponent(tabbedPane);

        addDialogClosingButton("Close", 0, () -> Boolean.TRUE); // TODO: i18n
    }

    public static void openDialog(Frame owner, ProjectConfiguration projectConfiguration) {
        new ProjectConfigurationDialog(owner, projectConfiguration).showDialog();
    }
}
