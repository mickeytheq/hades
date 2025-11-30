package com.mickeytheq.ahlcg4j.strangeeons.tasks;

import ca.cgjennings.apps.arkham.component.GameComponent;
import ca.cgjennings.apps.arkham.project.Member;
import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import org.apache.commons.lang3.time.StopWatch;
import resources.ResourceKit;

import java.awt.image.BufferedImage;
import java.io.File;


public class PerformanceTests {
    public static StopWatch performGameComponentLoadTest(File file, int iterations) {
        StopWatch stopWatch = StopWatch.createStarted();

        for (int i = 0; i < iterations; i++) {
            GameComponent gameComponent = ResourceKit.getGameComponentFromFile(file, true);
        }

        stopWatch.stop();

        return stopWatch;
    }

    public static StopWatch performEditorTest(File file, int iterations) {
        GameComponent gameComponent = ResourceKit.getGameComponentFromFile(file, true);

        StopWatch stopWatch = StopWatch.createStarted();

        for (int i = 0; i < iterations; i++) {
            gameComponent.createDefaultEditor();
        }

        stopWatch.stop();

        return stopWatch;
    }

    public static StopWatch performPaintTest(File file, int iterations) {
        GameComponent gameComponent = ResourceKit.getGameComponentFromFile(file, true);

        StopWatch stopWatch = StopWatch.createStarted();

        for (int i = 0; i < iterations; i++) {
            Sheet[] sheets = gameComponent.createDefaultSheets();

            for (Sheet sheet : sheets) {
                BufferedImage bufferedImage = sheet.paint(RenderTarget.EXPORT, 300);
            }
        }

        stopWatch.stop();

        return stopWatch;
    }
}
