package com.mickeytheq.hades.strangeeons.tasks;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.project.Member;
import com.mickeytheq.hades.core.global.ui.GlobalConfigurationDialog;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.StandardProjectContext;
import com.mickeytheq.hades.core.project.ui.ProjectConfigurationDialog;

public class OpenGlobalConfiguration extends BaseTaskAction {
    @Override
    public String getLabel() {
        return "Global configuration";
    }

    @Override
    public boolean appliesToSelection(Member[] members) {
        return true;
    }

    @Override
    public boolean performOnSelection(Member[] members) {
        GlobalConfigurationDialog.openDialog(StrangeEons.getWindow());

        return true;
    }
}
