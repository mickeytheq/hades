package com.mickeytheq.ahlcg4j.strangeeons.tasks;

import ca.cgjennings.apps.arkham.project.Member;
import ca.cgjennings.apps.arkham.project.Project;
import ca.cgjennings.apps.arkham.project.Task;
import ca.cgjennings.apps.arkham.project.TaskAction;

public abstract class BaseTaskAction extends TaskAction {
    @Override
    public abstract boolean appliesToSelection(Member[] members);

    @Override
    public abstract boolean performOnSelection(Member[] members);

    @Override
    public boolean perform(Project project, Task task, Member member) {
        throw new UnsupportedOperationException("Implement performOnSelection");
    }

    @Override
    public boolean appliesTo(Project project, Task task, Member member) {
        throw new UnsupportedOperationException("Implement appliesToSelection");
    }
}
