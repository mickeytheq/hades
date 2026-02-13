package com.mickeytheq.hades.strangeeons.tasks;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.component.GameComponent;
import ca.cgjennings.apps.arkham.project.Member;
import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.core.CardFaces;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.StandardProjectContext;
import com.mickeytheq.hades.core.view.CardView;
import com.mickeytheq.hades.core.view.common.CardFaceViewUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.serialise.CardIO;
import com.mickeytheq.hades.strangeeons.util.MemberUtils;
import com.mickeytheq.hades.ui.DialogEx;
import com.mickeytheq.hades.ui.FileChooser;
import com.mickeytheq.hades.ui.LoggingLevel;
import com.mickeytheq.hades.ui.ProgressDialog;
import org.apache.commons.lang3.Strings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import resources.ResourceKit;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateComparisonImagesTaskAction extends BaseTaskAction {
    private static final Logger logger = LogManager.getLogger(GenerateComparisonImagesTaskAction.class);

    @Override
    public String getLabel() {
        return "Generate AHLCG and Hades comparison images";
    }

    @Override
    public boolean appliesToSelection(Member[] members) {
        return true;
    }

    @Override
    public boolean performOnSelection(Member[] members) {
        CompareOptions compareOptions = new CompareOptions();
        if (!compareOptions.showDialog())
            return true;

        List<Path> eonsPathsToCompare = MemberUtils.getAllMemberDescendants(Arrays.asList(members)).stream()
                .filter(MemberUtils::isMemberEonFile)
                .map(o -> o.getFile().toPath())
                .collect(Collectors.toList());

        new ProgressDialog(LoggingLevel.Normal).runWithinProgressDialog(() -> {
                buildComparison(eonsPathsToCompare,
                        compareOptions.getAhlcgDirectoryChooser().getSelectedPath(),
                        compareOptions.getHadesDirectoryChooser().getSelectedPath(),
                        compareOptions.getOutputDirectoryChooser().getSelectedPath());
                return null;
        });

        return true;
    }

    private void buildComparison(List<Path> eonsFiles, Path ahlcgProjectRoot, Path hadesProjectRoot, Path outputDirectory) {
        try {
            Files.createDirectories(outputDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create output directory", e);
        }

        ProjectContext projectContext = StandardProjectContext.getContextForContentPath(hadesProjectRoot);

        int dpi = 300;
        RenderTarget renderTarget = RenderTarget.PREVIEW;

        for (Path eonsFile : eonsFiles) {
            try {
                Path relativePath = ahlcgProjectRoot.relativize(eonsFile);

                // AHLCG
                GameComponent gameComponent = ResourceKit.getGameComponentFromFile(eonsFile.toFile());
                Sheet[] sheets = gameComponent.createDefaultSheets();
                BufferedImage ahlcgFrontImage = sheets[0].paint(renderTarget, dpi);
                BufferedImage ahlcgBackImage = sheets.length > 1 ? sheets[1].paint(renderTarget, dpi) : null;

                // hades
                Path hadesPath = Paths.get(Strings.CS.replace(hadesProjectRoot.resolve(relativePath).toString(), ".eon", ".hades"));
                List<BufferedImage> hadesImages = createHadesImages(hadesPath, projectContext, renderTarget, dpi);

                writeImage(composeImage(ahlcgFrontImage, hadesImages.get(0)), outputDirectory.resolve(cleanPath(relativePath.toString()) + "-1-front.png"));
                writeImage(composeImage(ahlcgBackImage, hadesImages.get(1)), outputDirectory.resolve(cleanPath(relativePath.toString()) + "-2-back.png"));
            }
            catch (Exception e) {
                logger.error("Error generating comparison image for eons file '" + eonsFile + "'", e);
            }
        }
    }

    private List<BufferedImage> createHadesImages(Path cardPath, ProjectContext projectContext, RenderTarget renderTarget, double dpi) {
        if (!Files.exists(cardPath))
            return Lists.newArrayList(null, null);

        Card card = CardIO.readCard(cardPath, projectContext);
        CardView cardView = CardFaces.createCardView(card, projectContext);

        BufferedImage hadesFrontImage = CardFaceViewUtils.paintCardFace(cardView.getFrontFaceView(), renderTarget, dpi);

        BufferedImage hadesBackImage = null;
        if (cardView.hasBack())
            hadesBackImage = CardFaceViewUtils.paintCardFace(cardView.getBackFaceView(), renderTarget, dpi);

        return Lists.newArrayList(hadesFrontImage, hadesBackImage);
    }

    private void writeImage(BufferedImage bufferedImage, Path outputPath) {
        if (bufferedImage == null)
            return;

        try {
            ImageIO.write(bufferedImage, "png", outputPath.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        logger.info("Created composed image at " + outputPath);
    }

    private String cleanPath(String path) {
        path = Strings.CS.replace(path,"\\", "-");
        path = Strings.CS.replace(path,"/", "-");
        return path;
    }

    private BufferedImage composeImage(BufferedImage ahlcg, BufferedImage hades) {
        if (ahlcg == null && hades == null)
            return null;

        ahlcg = substituteIfMissing(ahlcg);
        hades = substituteIfMissing(hades);

        int spacingBetween = 100;
        BufferedImage composed = new BufferedImage(ahlcg.getWidth() + hades.getWidth() + spacingBetween, Math.max(ahlcg.getHeight(), hades.getHeight()), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = composed.createGraphics();
        try {
            g.setPaint(Color.BLACK);
            g.fillRect(0, 0, composed.getWidth(), composed.getHeight());
            g.drawImage(ahlcg, 0, 0, null);
            g.drawImage(hades, ahlcg.getWidth() + spacingBetween, 0, null);
        } finally {
            g.dispose();
        }

        return composed;
    }

    private BufferedImage substituteIfMissing(BufferedImage bufferedImage) {
        if (bufferedImage != null)
            return bufferedImage;

        BufferedImage missing = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = missing.createGraphics();
        try {
            g.setPaint(Color.RED);
            g.fillRect(0, 0, missing.getWidth(), missing.getHeight());
        } finally {
            g.dispose();
        }

        return missing;
    }

    static class CompareOptions {
        private FileChooser ahlcgDirectoryChooser;
        private FileChooser hadesDirectoryChooser;
        private FileChooser outputDirectoryChooser;

        public boolean showDialog() {
            ahlcgDirectoryChooser = new FileChooser();
            ahlcgDirectoryChooser.setSelectedFile(StrangeEons.getOpenProject().getFile());
            ahlcgDirectoryChooser.getFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            ahlcgDirectoryChooser.getTextField().setEnabled(false);

            hadesDirectoryChooser = new FileChooser();
            hadesDirectoryChooser.getFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            hadesDirectoryChooser.getTextField().setEnabled(false);

            outputDirectoryChooser = new FileChooser();
            outputDirectoryChooser.getFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            outputDirectoryChooser.getTextField().setEnabled(false);

            ahlcgDirectoryChooser.addActionListener(e -> {
                hadesDirectoryChooser.setSelectedFile(new File(ahlcgDirectoryChooser.getSelectedFile() + "-hades"));
                outputDirectoryChooser.setSelectedFile(new File(ahlcgDirectoryChooser.getSelectedFile() + "-compare-ahlcg-hades"));
            });

            JPanel panel = MigLayoutUtils.createTitledPanel("Options");
            MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, "AHLCG project root directory: ", ahlcgDirectoryChooser);
            MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, "Hades project root directory: ", hadesDirectoryChooser);
            MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, "Output directory: ", outputDirectoryChooser);

            DialogEx dialogEx = new DialogEx(StrangeEons.getWindow(), true);
            dialogEx.setContentComponent(panel);
            dialogEx.setTitle("Migration options");
            dialogEx.addOkCancelButtons(() -> Boolean.TRUE);

            return dialogEx.showDialog() == DialogEx.OK_OPTION;
        }

        public FileChooser getAhlcgDirectoryChooser() {
            return ahlcgDirectoryChooser;
        }

        public FileChooser getHadesDirectoryChooser() {
            return hadesDirectoryChooser;
        }

        public FileChooser getOutputDirectoryChooser() {
            return outputDirectoryChooser;
        }
    }
}
