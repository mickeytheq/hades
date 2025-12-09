package com.mickeytheq.hades.strangeeons.tasks;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.project.Member;
import com.mickeytheq.hades.core.project.ProjectConfiguration;
import com.mickeytheq.hades.core.project.ProjectConfigurations;
import com.mickeytheq.hades.core.project.ui.ProjectSettingsDialog;

public class OpenProjectConfiguration extends BaseTaskAction {
    @Override
    public String getLabel() {
        return "Project configuration";
    }

    @Override
    public boolean appliesToSelection(Member[] members) {
        return true;
    }

    @Override
    public boolean performOnSelection(Member[] members) {
        ProjectConfiguration projectConfiguration = ProjectConfigurations.get();
        ProjectSettingsDialog.openDialog(StrangeEons.getWindow(), projectConfiguration);

        return true;
    }
}
