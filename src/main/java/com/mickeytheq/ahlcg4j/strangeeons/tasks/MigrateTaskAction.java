package com.mickeytheq.ahlcg4j.strangeeons.tasks;

import ca.cgjennings.apps.arkham.project.Member;
import com.mickeytheq.ahlcg4j.strangeeons.ahlcg.migration.Migrator;

import java.io.File;

public class MigrateTaskAction extends BaseTaskAction {
    @Override
    public String getLabel() {
        return "Migrate AHLCG content to AHLCG4J";
    }

    @Override
    public boolean appliesToSelection(Member[] members) {
        return true;
    }

    @Override
    public boolean performOnSelection(Member[] members) {
        for (Member member : members) {
            if (member.isFolder())
                continue;

            File sourceFile = member.getFile();
            File targetFile = new File(sourceFile.getParentFile(), member.getBaseName() + "-migrated.eon");

            Migrator.migrate(member.getFile(), targetFile);
        }

        return true;
    }
}
