package com.mickeytheq.hades.scratchpad;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import com.mickeytheq.hades.core.CardFaces;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.model.CardFaceModel;
import com.mickeytheq.hades.core.model.cardfaces.Enemy;
import com.mickeytheq.hades.core.model.image.NothingImagePersister;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.ProjectContexts;
import com.mickeytheq.hades.core.project.StandardProjectContext;
import com.mickeytheq.hades.core.project.configuration.ProjectConfiguration;
import com.mickeytheq.hades.core.view.CardView;
import com.mickeytheq.hades.core.view.utils.CardFaceViewUtils;
import com.mickeytheq.hades.serialise.CardIO;
import com.mickeytheq.hades.strangeeons.plugin.Bootstrapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QuickExport {
    public static void main(String[] args) throws Exception {
        Bootstrapper.initaliseOutsideStrangeEons();

        ProjectContext projectContext = new StandardProjectContext(new ProjectConfiguration(), new NothingImagePersister());
        ProjectContexts.withContext(projectContext, () -> {
            doInContext(projectContext);
        });

    }

    private static void doInContext(ProjectContext projectContext) {
        Enemy enemy = new Enemy();
        enemy.getCommonCardFieldsModel().setTitle("Enemy");

        writeCardFace(enemy, projectContext, Paths.get("D:\\Temp\\Trash\\Enemy.png"));

        enemy.getCommonCardFieldsModel().setSubtitle("Enemy");

        writeCardFace(enemy, projectContext, Paths.get("D:\\Temp\\Trash\\Enemy-Subtitle.png"));
    }

    private static void writeCardFace(CardFaceModel cardFaceModel, ProjectContext projectContext, Path destination) {
        Card card = CardFaces.createCardModel(cardFaceModel, null);

        CardView cardView = CardFaces.createCardView(card, projectContext);

        BufferedImage bufferedImage = CardFaceViewUtils.paintCardFace(cardView.getFrontFaceView(), RenderTarget.PREVIEW, 600, 72);

        try {
            ImageIO.write(bufferedImage, "png", destination.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
