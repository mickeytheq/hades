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

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

// some utility methods that cards faces want to share but don't strictly belong to another utility class
public class CardFaceViewUtils {
    // TODO: this needs to go away and be replace by a dynamic value from the CardFaceView's template info
    // TODO: current difficulty is it needs to be known to build the portrait control for UI positioning of the the
    // TODO: image within the available portrait space. Therefore the template and thus DPI needs to selected when/before
    // TODO: the editor is open
    //
    // TODO: we're using this placeholder to easily find where this is being used to fix all instances later
    public final static double HARDCODED_DPI = 300;

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

    public static BufferedImage paintCardFace(CardFaceView cardFaceView, RenderTarget renderTarget, int ppi, int bleedMarginInPixels) {
        Optional<TemplateInfo> templateInfoOptional = cardFaceView.getCompatibleTemplateInfo(ppi);

        if (!templateInfoOptional.isPresent())
            return createMissingTemplateImage(cardFaceView, ppi);

        TemplateInfo sourceTemplateInfo = templateInfoOptional.get();

        TemplateInfo templateInfo = sourceTemplateInfo.withBleedMarginInPixels(bleedMarginInPixels);

        BufferedImage bufferedImage = new BufferedImage(templateInfo.getWidthInPixels(), templateInfo.getHeightInPixels(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        try {
            renderTarget.applyTo(graphics2D);

            cardFaceView.paint(new DefaultPaintContext(graphics2D, renderTarget, cardFaceView, templateInfo));
        }
        finally {
            graphics2D.dispose();
        }

        return bufferedImage;
    }

    public static BufferedImage createMissingTemplateImage(CardFaceView cardFaceView, int ppi) {
        List<String> textStrings = Lists.newArrayList(
                "No template available for",
                "card face view '" + cardFaceView.getTitle() + "'",
                "of type '" + cardFaceView.getClass().getSimpleName() + "'",
                "for desired PPI " + ppi);

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
