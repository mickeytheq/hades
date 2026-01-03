package com.mickeytheq.hades.core.view.utils;

import ca.cgjennings.layout.MarkupRenderer;
import ca.cgjennings.layout.PageShape;
import ca.cgjennings.layout.TextStyle;
import com.mickeytheq.hades.core.model.common.Statistic;
import com.mickeytheq.hades.core.view.PaintContext;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class PaintUtils {
    public static void paintLabel(PaintContext paintContext, Rectangle drawRegion, String labelText) {
        if (StringUtils.isEmpty(labelText))
            return;

        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getLargeLabelTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
        markupRenderer.setMarkupText(labelText);
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), drawRegion);
    }

    public static void paintTitleLeftAlign(PaintContext paintContext, Rectangle drawRegion, String titleText, boolean unique) {
        paintTitle(paintContext, drawRegion, titleText, unique, MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_LEFT, false);
    }

    public static void paintTitle(PaintContext paintContext, Rectangle drawRegion, String titleText, boolean unique) {
        paintTitle(paintContext, drawRegion, titleText, unique, MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER, false);
    }

    public static void paintTitle(PaintContext paintContext, Rectangle drawRegion, String titleText, boolean unique, int alignment, boolean multiline) {
        if (StringUtils.isEmpty(titleText))
            return;

        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getTitleTextStyle());
        markupRenderer.setAlignment(alignment);

        MarkupUtils.applyTagMarkupConfiguration(markupRenderer);

        if (unique)
            titleText = "<uni>" + titleText;

        markupRenderer.setMarkupText(titleText);

        if (multiline) {
            markupRenderer.setTextFitting(MarkupRenderer.FIT_SCALE_TEXT);
            markupRenderer.draw(paintContext.getGraphics(), drawRegion);
        }
        else {
            markupRenderer.drawAsSingleLine(paintContext.getGraphics(), drawRegion);
        }
    }

    public static void paintSubtitle(PaintContext paintContext, Rectangle drawRegion, String subtitleText) {
        if (StringUtils.isEmpty(subtitleText))
            return;

        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getSubtitleTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);

        MarkupUtils.applyTagMarkupConfiguration(markupRenderer);

        markupRenderer.setMarkupText(subtitleText);
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), drawRegion);
    }

    public static void paintBodyText(PaintContext paintContext, String bodyText, Rectangle bodyDrawRegion, PageShape pageShape) {
        if (StringUtils.isEmpty(bodyText))
            return;

        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getBodyTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_LEFT);
        markupRenderer.setLineTightness(0.6f * 0.9f);
        markupRenderer.setTextFitting(MarkupRenderer.FIT_SCALE_TEXT);
        markupRenderer.setPageShape(pageShape);

        MarkupUtils.applyTagMarkupConfiguration(markupRenderer);

        markupRenderer.setMarkupText(bodyText);
        markupRenderer.draw(paintContext.getGraphics(), bodyDrawRegion);
    }

    private static final Font STAT_FONT = new Font("Bolton", Font.PLAIN, 24);
    private static final Font PER_INVESTIGATOR_FONT = new Font(TextStyleUtils.AHLCG_SYMBOL_FONT, Font.PLAIN, 6).deriveFont(6.5f);

    private static final Color HEALTH_TEXT_COLOUR = new Color(0.996f, 0.945f, 0.859f);
    private static final Color HEALTH_TEXT_OUTLINE_COLOUR = new Color(0.68f, 0.12f, 0.22f);
    private static final Color SANITY_TEXT_COLOUR = HEALTH_TEXT_COLOUR;
    private static final Color SANITY_TEXT_OUTLINE_COLOUR = new Color(0.25f, 0.33f, 0.44f);

    public static void paintHealth(PaintContext paintContext, Rectangle drawRegion, boolean paintSymbol, String value, boolean perInvestigator) {
        if (StringUtils.isEmpty(value))
            return;

        if (paintSymbol)
            PaintUtils.paintBufferedImage(paintContext.getGraphics(), ImageUtils.loadImage("/overlays/health_base.png"), drawRegion);

        if (perInvestigator) {
            Rectangle healthStatDrawRegion = new Rectangle(drawRegion);
            healthStatDrawRegion.translate(-15, -5);

            PaintUtils.drawOutlinedTitle(paintContext.getGraphics(), paintContext.getRenderingDpi(),
                    value,
                    healthStatDrawRegion,
                    STAT_FONT, STAT_FONT.getSize(), 3.0f,
                    HEALTH_TEXT_COLOUR,
                    HEALTH_TEXT_OUTLINE_COLOUR,
                    0, true);

            Rectangle perInvestigatorDrawRegion = new Rectangle(healthStatDrawRegion);
            perInvestigatorDrawRegion.translate(30, 0);

            PaintUtils.drawOutlinedTitle(paintContext.getGraphics(), paintContext.getRenderingDpi(),
                    "p",
                    perInvestigatorDrawRegion,
                    PER_INVESTIGATOR_FONT, PER_INVESTIGATOR_FONT.getSize(), 3.0f,
                    HEALTH_TEXT_COLOUR,
                    HEALTH_TEXT_OUTLINE_COLOUR,
                    0, true);
        }
        else {
            Rectangle healthStatDrawRegion = new Rectangle(drawRegion);
            healthStatDrawRegion.translate(-2, -5);

            PaintUtils.drawOutlinedTitle(paintContext.getGraphics(), paintContext.getRenderingDpi(),
                    value,
                    healthStatDrawRegion,
                    STAT_FONT, STAT_FONT.getSize(), 3.0f,
                    HEALTH_TEXT_COLOUR,
                    HEALTH_TEXT_OUTLINE_COLOUR,
                    0, true);
        }
    }

    public static void paintSanity(PaintContext paintContext, Rectangle drawRegion, boolean paintSymbol, String value, boolean perInvestigator) {
        if (StringUtils.isEmpty(value))
            return;

        if (paintSymbol)
            PaintUtils.paintBufferedImage(paintContext.getGraphics(), ImageUtils.loadImage("/overlays/sanity_base.png"), drawRegion);

        if (perInvestigator) {
            Rectangle statDrawRegion = new Rectangle(drawRegion);
            statDrawRegion.translate(-8, -5);

            PaintUtils.drawOutlinedTitle(paintContext.getGraphics(), paintContext.getRenderingDpi(),
                    value,
                    statDrawRegion,
                    STAT_FONT, STAT_FONT.getSize(), 3.0f,
                    SANITY_TEXT_COLOUR,
                    SANITY_TEXT_OUTLINE_COLOUR,
                    0, true);

            Rectangle perInvestigatorDrawRegion = new Rectangle(statDrawRegion);
            perInvestigatorDrawRegion.translate(30, 0);

            PaintUtils.drawOutlinedTitle(paintContext.getGraphics(), paintContext.getRenderingDpi(),
                    "p",
                    perInvestigatorDrawRegion,
                    PER_INVESTIGATOR_FONT, PER_INVESTIGATOR_FONT.getSize(), 3.0f,
                    SANITY_TEXT_COLOUR,
                    SANITY_TEXT_OUTLINE_COLOUR,
                    0, true);
        }
        else {
            Rectangle statDrawRegion = new Rectangle(drawRegion);
            statDrawRegion.translate(-2, -5);

            PaintUtils.drawOutlinedTitle(paintContext.getGraphics(), paintContext.getRenderingDpi(),
                    value,
                    statDrawRegion,
                    STAT_FONT, STAT_FONT.getSize(), 3.0f,
                    SANITY_TEXT_COLOUR,
                    SANITY_TEXT_OUTLINE_COLOUR,
                    0, true);
        }
    }

    // light colour used for drawing statistic values or outlines
    public static final Color STATISTIC_LIGHT_TEXT_COLOUR = new Color(0.996f, 0.945f, 0.859f);

    public static void paintStatistic(PaintContext paintContext, Rectangle drawRegion, Statistic statistic, Color outlineColour, Color textColour) {
        if (statistic.isNull())
            return;

        new StatisticPainter(paintContext, statistic, drawRegion, outlineColour, textColour).paint();
    }

    // for things like act/agenda headers, e.g. Agenda 1a would be typeText = 'Agenda' and indexText = '1a'
    public static void paintScenarioIndex(PaintContext paintContext, Rectangle drawRegion, String typeText, String number, String deckId) {
        paintScenarioIndex(paintContext, drawRegion, typeText, number, deckId, TextStyleUtils.getScenarioIndexTextStyle());
    }

    public static void paintScenarioIndexBack(PaintContext paintContext, Rectangle drawRegion, String typeText, String number, String deckId) {
        paintScenarioIndex(paintContext, drawRegion, typeText, number, deckId, TextStyleUtils.getScenarioIndexBackTextStyle());
    }

    public static void paintScenarioIndex(PaintContext paintContext, Rectangle drawRegion, String typeText, String number, String deckId, TextStyle textStyle) {
        if (StringUtils.isEmpty(number) && StringUtils.isEmpty(deckId))
            return;

        // change to empty strings instead of null for concatenation
        number = StringUtils.defaultIfEmpty(number, "");
        deckId = StringUtils.defaultIfEmpty(deckId, "");

        String text = typeText + " " + number + deckId;

        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(textStyle);
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
        markupRenderer.setMarkupText(text);
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), drawRegion);
    }

    public static void paintBufferedImage(Graphics2D g, BufferedImage image, Rectangle rectangle) {
        g.drawImage(image, (int)rectangle.getX(), (int)rectangle.getY(), (int)rectangle.getWidth(), (int)rectangle.getHeight(), null);
    }

    public static void drawOutlinedTitle(Graphics2D g, double dpi, String text, Rectangle region, Font font, float maxSize, float outlineSize, Paint textColor, Paint outlineColor, int alignment, boolean outlineUnderneath) {
        if (StringUtils.isEmpty(text))
            return;

        Font f = font.deriveFont(maxSize * (float) dpi / 72f);
        GlyphVector gv = f.createGlyphVector(g.getFontRenderContext(), text);
        Rectangle2D bounds = gv.getLogicalBounds();

        if (bounds.getWidth() > region.getWidth()) {
            f = f.deriveFont((float) (region.getWidth() / bounds.getWidth()) * maxSize * (float) dpi / 72f);
            gv = f.createGlyphVector(g.getFontRenderContext(), text);
            bounds = gv.getLogicalBounds();
        }

        float y = region.y + (float) (region.height - bounds.getHeight()) / 2f;
        float x;
        if (alignment > 0) {
            x = (float) (region.x + region.width - bounds.getWidth());
        } else if (alignment < 0) {
            x = region.x;
        } else {
            x = (float) (region.x + (region.width - bounds.getWidth()) / 2f);
        }

        Shape s = gv.getOutline(x, y + g.getFontMetrics(f).getAscent());
        Object oldAA = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (!outlineUnderneath) {
            g.setPaint(textColor);
            g.fill(s);
        }

        Stroke oldStroke = g.getStroke();
        g.setStroke(new BasicStroke(outlineSize * (float) dpi / 72f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setPaint(outlineColor);
        g.draw(s);
        g.setStroke(oldStroke);

        if (outlineUnderneath) {
            g.setPaint(textColor);
            g.fill(s);
        }

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAA);
    }

    // paints statistics
    // draws an outline around the statistic character
    // handles positioning of number and optional per investigator icon
    //
    static class StatisticPainter {
        private final PaintContext paintContext;
        private final Statistic statistic;
        private final Rectangle drawRegion;
        private final Color outlineColour;
        private final Color textColour;
        private Font statisticValueFont = STAT_FONT;
        private Font perInvestigatorFont = PER_INVESTIGATOR_FONT;

        private GlyphVector statisticValueGlyphVector;
        private Rectangle2D valueTextBounds;
        private GlyphVector perInvestigatorGlyphVector;
        private Rectangle2D perInvestigatorBounds;

        // the draw region should have a 0 width (it is ignored) as positioning occurs as a centring around the X-position
        // the width is irrelevant as only the height is used to control the sizing/scale of the rendered text
        // TODO: this does means a longer statistic (such as 100) might be too large
        public StatisticPainter(PaintContext paintContext, Statistic statistic, Rectangle drawRegion, Color outlineColour, Color textColour) {
            this.paintContext = paintContext;
            this.statistic = statistic;
            this.drawRegion = drawRegion;
            this.outlineColour = outlineColour;
            this.textColour = textColour;
        }

        public void paint() {
            if (statistic.isNull())
                return;

            calculateStatisticValue();

            if (statistic.isPerInvestigator()) {
                calculatePerInvestigator();
            }

            drawStatistic();
        }

        private void calculateStatisticValue() {
            Graphics2D g = paintContext.getGraphics();

            // figure out the bounds of the statistic value text when drawn when the initial/default font/size
            statisticValueGlyphVector = statisticValueFont.createGlyphVector(g.getFontRenderContext(), statistic.getValue());
            valueTextBounds = statisticValueGlyphVector.getLogicalBounds();

            // calculate a new font size based on the desired draw region - use the height as the defining size
            double scaleAdjust = drawRegion.getHeight() / valueTextBounds.getHeight();

            // recalculate glyph vector and text bounds with the new font size
            statisticValueFont = statisticValueFont.deriveFont(statisticValueFont.getSize() * (float)scaleAdjust);
            statisticValueGlyphVector = statisticValueFont.createGlyphVector(g.getFontRenderContext(), statistic.getValue());
            valueTextBounds = statisticValueGlyphVector.getLogicalBounds();
        }

        private void calculatePerInvestigator() {
            Graphics2D g = paintContext.getGraphics();

            // calculate per investigator font/glyph/bounds
            // scale the per investigator font off the text font so they stay in proportion as the text font changes
            perInvestigatorFont = perInvestigatorFont.deriveFont(statisticValueFont.getSize() * 0.4f);
            perInvestigatorGlyphVector = perInvestigatorFont.createGlyphVector(g.getFontRenderContext(), "p");
            perInvestigatorBounds = perInvestigatorGlyphVector.getLogicalBounds();
        }

        private void drawStatistic() {
            double totalWidth = valueTextBounds.getWidth();

            // calculate a total width appropriate for the text and per investigator symbol if present
            if (statistic.isPerInvestigator()) {
                totalWidth = totalWidth + perInvestigatorBounds.getWidth();

                // add a small gap for visual separation between the value and the per-investigator icon
                // make this gap proportional to the per-investigator icon size so it scales if the overall
                // font sizes are changed
//                totalWidth = totalWidth + perInvestigatorBounds.getWidth() * 0.2f;
            }

            // calculate X and Y coordinates to draw that are centred in the draw region
            // the Y should be ~ the draw region as the font heights are fitted to it earlier in the process
            float y = drawRegion.y + (float) (drawRegion.height - valueTextBounds.getHeight()) / 2f;
            float x = (float) (drawRegion.x + (drawRegion.width - totalWidth) / 2f);

            drawStroke(statisticValueFont, statisticValueGlyphVector, x, y);

            if (statistic.isPerInvestigator()) {
                x = x + (float) valueTextBounds.getWidth();

                // adjust the y position of the per investigator by a factor of the text height to position it
                // approximately in the vertical-middle of the text
                y = y + (float)valueTextBounds.getHeight() * 0.5f;

                drawStroke(perInvestigatorFont, perInvestigatorGlyphVector, x, y);
            }
        }

        private void drawStroke(Font font, GlyphVector glyphVector, float x, float y) {
            Graphics2D g = paintContext.getGraphics();

            Shape shape = glyphVector.getOutline(x, y + g.getFontMetrics(font).getAscent());
            Object oldAA = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Stroke oldStroke = g.getStroke();

            // TODO: configurable stroke width?
            g.setStroke(new BasicStroke(1.0f * (float) paintContext.getRenderingDpi() / 72f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setPaint(outlineColour);
            g.draw(shape);
            g.setStroke(oldStroke);
            g.setPaint(textColour);
            g.fill(shape);

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAA);
        }
    }
}
