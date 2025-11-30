package com.mickeytheq.ahlcg4j.core.view.utils;

import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.ahlcg4j.core.view.PaintContext;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class PaintUtils {
    public static void paintLabel(PaintContext paintContext, Rectangle drawRegion, String labelText) {
        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getLargeLabelTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
        markupRenderer.setMarkupText(labelText);
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), drawRegion);
    }

    public static void paintBufferedImage(Graphics2D g, BufferedImage image, Rectangle rectangle) {
        g.drawImage(image, (int)rectangle.getX(), (int)rectangle.getY(), (int)rectangle.getWidth(), (int)rectangle.getHeight(), null);
    }

    public static void drawOutlinedTitle(Graphics2D g, double dpi, String text, Rectangle region, Font font, float maxSize, float outlineSize, Paint textColor, Paint outlineColor, int alignment, boolean outlineUnderneath) {
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
