package com.mickeytheq.hades.strangeeons.tasks;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.project.Member;
import ca.cgjennings.apps.arkham.project.Project;
import ca.cgjennings.apps.arkham.project.Task;
import ca.cgjennings.apps.arkham.project.TaskAction;
import org.apache.commons.lang3.time.StopWatch;

import javax.swing.*;

public class PerformanceSuite extends TaskAction {
    @Override
    public String getLabel() {
        return "Run all performance tests";
    }

    @Override
    public boolean perform(Project project, Task task, Member member) {
        StopWatch stopWatch1 = PerformanceTests.performGameComponentLoadTest(member.getFile(), 100);
        StopWatch stopWatch2 = PerformanceTests.performEditorTest(member.getFile(), 100);
        StopWatch stopWatch3 = PerformanceTests.performPaintTest(member.getFile(), 100);

        JOptionPane.showMessageDialog(StrangeEons.getWindow(),
                "Component load: " + stopWatch1.formatTime() + ". Editor creation: " + stopWatch2.formatTime() + ". Paint: " + stopWatch3.formatTime());
        return true;
    }

    @Override
    public boolean appliesTo(Project project, Task task, Member member) {
        if (member == null)
            return false;

        if (member.isFolder())
            return false;

        return true;
    }
}
