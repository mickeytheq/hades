package com.mickeytheq.hades.strangeeons.tasks;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.component.GameComponent;
import ca.cgjennings.apps.arkham.project.Member;
import ca.cgjennings.apps.arkham.project.Project;
import ca.cgjennings.apps.arkham.project.Task;
import ca.cgjennings.apps.arkham.project.TaskAction;
import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.time.StopWatch;
import resources.ResourceKit;

import javax.swing.*;

public class LoadGameComponentPerformanceTest extends TaskAction {
    @Override
    public String getLabel() {
        return "Load game component from disk performance test";
    }

    @Override
    public boolean perform(Project project, Task task, Member member) {
        int iterations = 100;
        StopWatch stopWatch = PerformanceTests.performGameComponentLoadTest(member.getFile(), iterations);

        JOptionPane.showMessageDialog(StrangeEons.getWindow(), "Ran " + iterations + " iterations of test '" + member.getFile().getAbsolutePath() + "' in " + stopWatch.formatTime());

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
