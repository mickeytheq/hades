package com.mickeytheq.hades.strangeeons.tasks;

import ca.cgjennings.apps.arkham.project.TaskActionTree;

public class HadesActionTree extends TaskActionTree {
    public HadesActionTree() {
        add(new NewCard());
        add(new OpenProjectConfiguration());
        add(new OpenGlobalConfiguration());
//        add(new ReviewCardsTaskAction());
//        add(new CompareCardsTaskAction());
        add(new MigrateTaskAction());
        add(new GenerateComparisonImagesTaskAction());
        add(new ChangeCardFaceTypeTaskAction());
        add(new ViewLog());
//        add(new PerformanceSuite());
//        add(new LoadGameComponentPerformanceTest());
//        add(new LoadEditorPerformanceTest());
        add(new PaintingPerformanceTest());
    }

    @Override
    public String getLabel() {
        return "Hades";
    }


}
