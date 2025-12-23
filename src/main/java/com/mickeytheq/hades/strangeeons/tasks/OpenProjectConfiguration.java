package com.mickeytheq.hades.strangeeons.tasks;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.project.Member;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.StandardProjectContext;
import com.mickeytheq.hades.core.project.configuration.ProjectConfiguration;
import com.mickeytheq.hades.core.project.ui.ProjectConfigurationDialog;

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
        ProjectContext projectContext = StandardProjectContext.getContextForContentPath(StrangeEons.getOpenProject().getFile().toPath());
        ProjectConfiguration projectConfiguration = projectContext.getProjectConfiguration();
        ProjectConfigurationDialog.openDialog(StrangeEons.getWindow(), projectConfiguration);

        return true;
    }
}
