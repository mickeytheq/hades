package com.mickeytheq.hades.core.view.utils;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.layout.MarkupRenderer;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.common.CollectionView;
import com.mickeytheq.hades.core.view.common.EncounterSetView;
import com.mickeytheq.hades.core.view.common.StorySectionView;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.core.view.utils.MultiSectionRenderer;
import com.mickeytheq.hades.core.view.utils.PaintUtils;
import com.mickeytheq.hades.core.view.utils.TextStyleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

// some utility methods that cards faces want to share but don't strictly belong to another utility class
public class CardFaceViewUtils {
    public static void createEncounterSetCollectionTab(EditorContext editorContext, EncounterSetView encounterSetView, CollectionView collectionView) {
        if (encounterSetView == null && collectionView == null)
            return;

        JPanel mainPanel = MigLayoutUtils.createOrganiserPanel();

        if (encounterSetView != null) {
            JPanel encounterSetPanel = encounterSetView.createStandardEncounterPanel(editorContext);
            mainPanel.add(encounterSetPanel, "pushx, growx, wrap");
        }

        if (collectionView != null) {
            JPanel collectionPanel = collectionView.createStandardCollectionPanel(editorContext);
            mainPanel.add(collectionPanel, "pushx, growx, wrap");
        }

        editorContext.addDisplayComponent("Collection / encounter", mainPanel);
    }

    // build the header/story/rules sections with appropriate gaps/lines between them
    public static void buildStorySections(PaintContext paintContext, MultiSectionRenderer renderer, StorySectionView... sectionViews) {
        boolean needSeparator = false;

        for (StorySectionView sectionView : sectionViews) {
            if (sectionView.getModel().isEmpty())
                continue;

            if (needSeparator) {
                renderer.getSections().add(new MultiSectionRenderer.VerticalSpacerSection(10));
                renderer.getSections().add(new MultiSectionRenderer.HorizontalLineSection(2));
                renderer.getSections().add(new MultiSectionRenderer.VerticalSpacerSection(10));
            }

            buildSection(paintContext, renderer, sectionView);

            needSeparator = true;
        }
    }

    private static void buildSection(PaintContext paintContext, MultiSectionRenderer renderer, StorySectionView sectionView) {
        if (sectionView.getModel().isEmpty())
            return;

        String header = sectionView.getModel().getHeader();
        String story = sectionView.getModel().getStory();
        String rules = sectionView.getModel().getRules();

        boolean needSeparator = false;

        if (!StringUtils.isEmpty(header)) {
            Supplier<MarkupRenderer> markupRendererSupplier = () -> {
                MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
                markupRenderer.setDefaultStyle(TextStyleUtils.getHeaderTextStyle());
                markupRenderer.setAlignment(MarkupRenderer.LAYOUT_LEFT);
                markupRenderer.setLineTightness(0.6f);
                markupRenderer.setMarkupText(header);
                return markupRenderer;
            };

            renderer.getSections().add(new MultiSectionRenderer.TextSection(markupRendererSupplier));

            needSeparator = true;
        }

        if (!StringUtils.isEmpty(story)) {
            if (needSeparator)
                renderer.getSections().add(new MultiSectionRenderer.VerticalSpacerSection(10));

            Supplier<MarkupRenderer> markupRendererSupplier = () -> {
                MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
                markupRenderer.setDefaultStyle(TextStyleUtils.getStoryTextStyle());
                markupRenderer.setAlignment(MarkupRenderer.LAYOUT_LEFT);
                markupRenderer.setLineTightness(0.6f);
                markupRenderer.setMarkupText(story);
                return markupRenderer;
            };

            renderer.getSections().add(new MultiSectionRenderer.DoubleLineInsetTextSection(markupRendererSupplier, 20));

            needSeparator = true;
        }

        if (!StringUtils.isEmpty(rules)) {
            if (needSeparator)
                renderer.getSections().add(new MultiSectionRenderer.VerticalSpacerSection(10));

            Supplier<MarkupRenderer> markupRendererSupplier = () -> {
                MarkupRenderer markupRenderer = PaintUtils.createBodyTextMarkupRenderer(paintContext);
                markupRenderer.setMarkupText(rules);
                return markupRenderer;
            };

            renderer.getSections().add(new MultiSectionRenderer.TextSection(markupRendererSupplier));
        }
    }

    // paints the card face view returning a BufferedImage
    // throws an exception if there is a problem with the painting
    public static BufferedImage paintCardFace(CardFaceView cardFaceView, RenderTarget renderTarget, int ppi, int requestedBleedMarginInPixels) {
        CardFacePaintResult result = paintCardFaceFullDetails(cardFaceView, renderTarget, ppi, requestedBleedMarginInPixels, false);

        if (result.getStatus() != CardFacePaintResult.Status.Success)
            throw new RuntimeException("Failed to paint card face " + cardFaceView.getBriefDisplayString() + ". Status code was '" + result.getStatus() + "'");

        return result.getBufferedImage();
    }

    // paints the card face view returning a full result container of details
    // if true is passed to generateImageOnError then an image containing the error will be generated instead of throwing an exception
    // this is useful in UI situations where it is a better UX to show the user an error rather than it being in an error dialog somewhere
    public static CardFacePaintResult paintCardFaceFullDetails(CardFaceView cardFaceView, RenderTarget renderTarget, int ppi, int requestedBleedMarginInPixels, boolean generateImageOnError) {
        Optional<TemplateInfo> templateInfoOptional = cardFaceView.getCompatibleTemplateInfo(ppi);

        if (!templateInfoOptional.isPresent()) {
            if (generateImageOnError)
                return new CardFacePaintResult(CardFacePaintResult.Status.NoTemplate, createMissingTemplateImage(cardFaceView, ppi), 0, 0, ppi, ppi);

            String error = "No compatible template found for card face view " + cardFaceView.getBriefDisplayString() + " and requested resolution PPI " + ppi;

            throw new RuntimeException(error);
        }

        TemplateInfo templateInfo = templateInfoOptional.get();

        StopWatch stopWatch = StopWatch.createStarted();

        // this will paint the full template size including all possible bleed margin regardless of how much is requested
        BufferedImage bufferedImage = new BufferedImage(templateInfo.getWidthInPixels(), templateInfo.getHeightInPixels(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        try {
            renderTarget.applyTo(graphics2D);

            cardFaceView.paint(new DefaultPaintContext(graphics2D, renderTarget, cardFaceView, templateInfo));
        }
        finally {
            graphics2D.dispose();
        }

        // after all drawing is complete we need to return a sub-image of the BufferedImage which comprises the bleed margin that was requested
        // this operation is cheap as creating a sub-image doesn't copy anything, it just provides a view on the existing image
        int bleedMarginToKeep = Math.min(templateInfo.getAvailableBleedMarginInPixels(), requestedBleedMarginInPixels);
        int bleedMarginToTrim = templateInfo.getAvailableBleedMarginInPixels() - bleedMarginToKeep;

        BufferedImage bleedTrimmedBufferedImage = bufferedImage.getSubimage(bleedMarginToTrim, bleedMarginToTrim,
                bufferedImage.getWidth() - bleedMarginToTrim * 2, bufferedImage.getHeight() - bleedMarginToTrim * 2);

        long paintTimeInMs = stopWatch.getTime();

        return new CardFacePaintResult(CardFacePaintResult.Status.Success, bleedTrimmedBufferedImage, bleedMarginToKeep,
                paintTimeInMs, templateInfo.getResolutionInPixelsPerInch(), templateInfo.getSourceTemplate().getResolutionInPixelsPerInch());
    }

    public static BufferedImage createMissingTemplateImage(CardFaceView cardFaceView, int ppi) {
        List<String> textStrings = Lists.newArrayList(
                "No template available for",
                "card face view '" + cardFaceView.getTitle() + "'",
                "of type '" + cardFaceView.getClass().getSimpleName() + "'",
                "for desired PPI " + ppi);

        return createErrorImage(textStrings);
    }

    public static BufferedImage createErrorImage(List<String> textStrings) {
        BufferedImage bufferedImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        try {
            graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            graphics2D.setColor(new Color(255, 200, 200));
            graphics2D.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());

            graphics2D.setColor(Color.BLACK);

            graphics2D.setFont(graphics2D.getFont().deriveFont(24.0f));

            int yPos = 100;

            for (String textString : textStrings) {
                int width = graphics2D.getFontMetrics().stringWidth(textString);
                int height = graphics2D.getFontMetrics().getHeight();

                graphics2D.drawString(textString, bufferedImage.getWidth() / 2 - width / 2, yPos);

                yPos = (int)(yPos + height * 1.5f);
            }
        }
        finally {
            graphics2D.dispose();
        }

        return bufferedImage;
    }
}
