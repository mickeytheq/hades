package com.mickeytheq.hades.strangeeons.tasks;

import ca.cgjennings.apps.arkham.AbstractGameComponentEditor;
import ca.cgjennings.apps.arkham.component.GameComponent;
import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import com.mickeytheq.hades.core.Cards;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.StandardProjectContext;
import com.mickeytheq.hades.core.view.CardView;
import com.mickeytheq.hades.core.view.utils.CardFaceViewUtils;
import com.mickeytheq.hades.serialise.CardIO;
import com.mickeytheq.hades.strangeeons.plugin.Bootstrapper;
import com.mickeytheq.hades.strangeeons.util.GameComponentUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;


public class PerformanceTests {
    public static StopWatch performGameComponentLoadTest(File file, int iterations) {
        StopWatch stopWatch = StopWatch.createStarted();

        for (int i = 0; i < iterations; i++) {
            GameComponent gameComponent = GameComponentUtils.loadGameComponent(file.toPath());
        }

        stopWatch.stop();

        return stopWatch;
    }

    public static StopWatch performEditorTest(File file, int iterations) {
        GameComponent gameComponent = GameComponentUtils.loadGameComponent(file.toPath());
        StopWatch stopWatch = StopWatch.createStarted();

        for (int i = 0; i < iterations; i++) {
            AbstractGameComponentEditor<?> editor = gameComponent.createDefaultEditor();
            editor.dispose();
        }

        stopWatch.stop();

        return stopWatch;
    }

    public static StopWatch performPaintTest(File file, int iterations) {
        GameComponent gameComponent = GameComponentUtils.loadGameComponent(file.toPath());

        StopWatch stopWatch = StopWatch.createStarted();

        for (int i = 0; i < iterations; i++) {
            Sheet[] sheets = gameComponent.createDefaultSheets();

            for (Sheet sheet : sheets) {
                BufferedImage bufferedImage = sheet.paint(RenderTarget.PREVIEW, 600);
            }
        }

        stopWatch.stop();

        return stopWatch;
    }

    public static StopWatch performPaintTest(Path hadesFile, int resolution, int iterations) {

        ProjectContext projectContext = StandardProjectContext.getContextForContentPath(hadesFile);
        Card card = CardIO.readCard(hadesFile, projectContext);
        CardView cardView = Cards.createCardView(card, projectContext);

        // clear out cold start issues
        CardFaceViewUtils.paintCardFace(cardView.getFrontFaceView(), RenderTarget.PREVIEW, resolution, 0);

        StopWatch stopWatch = StopWatch.createStarted();

        for (int i = 0; i < iterations; i++) {
            CardFaceViewUtils.paintCardFace(cardView.getFrontFaceView(), RenderTarget.PREVIEW, resolution, 0);
        }

        stopWatch.stop();

        return stopWatch;
    }

    public static void main(String[] args) {
        Bootstrapper.initaliseOutsideStrangeEons();

//        System.setProperty("sun.java2d.d3d", "true");
//        System.setProperty("sun.java2d.opengl", "True");

        int iterations = 200;
        Path path = Paths.get("D:\\Temp\\Circus Ex Mortis SE-Hades\\01 - One Night Only\\Encounter Set\\01 Disguised Monstrosity.hades");

        StopWatch stopWatch = PerformanceTests.performPaintTest(path, 600, iterations);
        System.out.println("Ran " + iterations + " iterations of test on '" + path + "' in " + stopWatch.formatTime());
    }
}
