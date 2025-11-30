package com.mickeytheq.ahlcg4j.strangeeons.tasks;

import ca.cgjennings.apps.arkham.project.TaskActionTree;

public class AhLcg4jActionTree extends TaskActionTree {
    public AhLcg4jActionTree() {
        add(new PerformanceSuite());
        add(new LoadGameComponentPerformanceTest());
        add(new LoadEditorPerformanceTest());
        add(new PaintingPerformanceTest());
    }

    @Override
    public String getLabel() {
        return "AHLCG4J";
    }


}
