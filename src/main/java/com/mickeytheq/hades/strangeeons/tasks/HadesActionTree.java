package com.mickeytheq.hades.strangeeons.tasks;

import ca.cgjennings.apps.arkham.project.TaskActionTree;

public class HadesActionTree extends TaskActionTree {
    public HadesActionTree() {
        add(new NewCard());
        add(new MigrateTaskAction());
        add(new PerformanceSuite());
        add(new LoadGameComponentPerformanceTest());
        add(new LoadEditorPerformanceTest());
        add(new PaintingPerformanceTest());
    }

    @Override
    public String getLabel() {
        return "Hades";
    }


}
