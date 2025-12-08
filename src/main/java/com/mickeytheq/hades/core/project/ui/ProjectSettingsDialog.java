package com.mickeytheq.hades.core.project.ui;

import com.mickeytheq.hades.core.project.ProjectConfiguration;
import com.mickeytheq.hades.ui.DialogWithButtons;

import javax.swing.*;
import java.awt.*;

public class ProjectSettingsDialog extends DialogWithButtons {
    private final ProjectConfiguration projectConfiguration;

    public ProjectSettingsDialog(Frame owner, ProjectConfiguration projectConfiguration) {
        super(owner, false);
        this.projectConfiguration = projectConfiguration;

        setTitle("Hades project configuration");

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Encounter sets", new EncounterSetsPanel(projectConfiguration));
        tabbedPane.addTab("Collections", new CollectionsPanel(projectConfiguration));

        setContentComponent(tabbedPane);

        addDialogClosingButton("Close", 0, () -> Boolean.TRUE);
    }

    public static void openDialog(Frame owner, ProjectConfiguration projectConfiguration) {
        new ProjectSettingsDialog(owner, projectConfiguration).showDialog();
    }
}
