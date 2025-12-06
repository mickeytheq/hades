package com.mickeytheq.ahlcg4j.core.view.utils;

import ca.cgjennings.layout.MarkupRenderer;
import ca.cgjennings.layout.PageShape;
import com.mickeytheq.ahlcg4j.core.view.PaintContext;
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
        paintTitle(paintContext, drawRegion, titleText, unique, MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_LEFT);
    }

    public static void paintTitle(PaintContext paintContext, Rectangle drawRegion, String titleText, boolean unique) {
        paintTitle(paintContext, drawRegion, titleText, unique, MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
    }

    private static void paintTitle(PaintContext paintContext, Rectangle drawRegion, String titleText, boolean unique, int alignment) {
        if (StringUtils.isEmpty(titleText))
            return;

        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getTitleTextStyle());
        markupRenderer.setAlignment(alignment);

        MarkupUtils.applyTagMarkupConfiguration(markupRenderer);

        if (unique)
            titleText = "<uni>" + titleText;

        markupRenderer.setMarkupText(titleText);
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), drawRegion);
    }

    public static void paintSubtitle(PaintContext paintContext, Rectangle drawRegion, String subtitleText) {
        if (StringUtils.isEmpty(subtitleText))
            return;

        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getSubtitleTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
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
}
