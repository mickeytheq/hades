package com.mickeytheq.hades.strangeeons.tasks;

import ca.cgjennings.apps.arkham.project.Member;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.ProjectMigrator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MigrateTaskAction extends BaseTaskAction {
    private static final Logger logger = LogManager.getLogger(MigrateTaskAction.class);

    @Override
    public String getLabel() {
        return "Migrate AHLCG plugin content to Hades";
    }

    @Override
    public boolean appliesToSelection(Member[] members) {
        return true;
    }

    @Override
    public boolean performOnSelection(Member[] members) {
        new ProjectMigrator(members).run();
        return true;
    }
}
