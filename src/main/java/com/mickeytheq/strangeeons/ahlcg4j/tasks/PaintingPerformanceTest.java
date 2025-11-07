package com.mickeytheq.strangeeons.ahlcg4j.tasks;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.component.GameComponent;
import ca.cgjennings.apps.arkham.project.Member;
import ca.cgjennings.apps.arkham.project.Project;
import ca.cgjennings.apps.arkham.project.Task;
import ca.cgjennings.apps.arkham.project.TaskAction;
import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import org.apache.commons.lang3.time.StopWatch;
import resources.ResourceKit;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class PaintingPerformanceTest extends TaskAction {
    @Override
    public String getLabel() {
        return "Painting performance test";
    }

    @Override
    public boolean perform(Project project, Task task, Member member) {
        GameComponent gameComponent = ResourceKit.getGameComponentFromFile(member.getFile(), true);

        int iterations = 100;

        StopWatch stopWatch = StopWatch.createStarted();

        for (int i = 0; i < iterations; i++) {
            Sheet[] sheets = gameComponent.createDefaultSheets();

            for (Sheet sheet : sheets) {
                BufferedImage bufferedImage = sheet.paint(RenderTarget.EXPORT, 300);
            }
        }

        stopWatch.stop();

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
