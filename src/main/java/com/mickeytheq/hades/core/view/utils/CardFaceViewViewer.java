package com.mickeytheq.hades.core.view.utils;

import ca.cgjennings.apps.arkham.AbstractViewer;
import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import com.mickeytheq.hades.core.view.CardFaceView;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * a viewer (really a previewer) that renders a CardFaceView used in the preview panel when editing a card
 * most of the work is done by AbstractViewer with this implementation providing a BufferedImage to render
 * the image is cached and only rendered when markChanged() is called, typically in response to a user activity
 * such as changing an editor the maps to rendered card content, e.g. the card title
 */
public class CardFaceViewViewer extends AbstractViewer {
    private static final Logger logger = LogManager.getLogger(CardFaceViewViewer.class);

    private final CardFaceView cardFaceView;
    private final int resolutionPpi;
    private final int desiredBleedMarginInPixels;

    private boolean needRefresh = true;

    // the rendered image that is cached and re-painted if markChanged is called
    private BufferedImage cachedImage;

    public CardFaceViewViewer(CardFaceView cardFaceView, int resolutionPpi, int desiredBleedMarginInPixels) {
        this.cardFaceView = cardFaceView;
        this.resolutionPpi = resolutionPpi;
        this.desiredBleedMarginInPixels = desiredBleedMarginInPixels;
    }

    // override just to adding timing for debugging
    @Override
    protected void paintComponent(Graphics g1) {
        StopWatch stopWatch = StopWatch.createStarted();
        super.paintComponent(g1);

        drawLabel((Graphics2D) g1, resolutionPpi + " PPI", 2);
        drawLabel((Graphics2D) g1, desiredBleedMarginInPixels + " target bleed pixels", 3);

        logger.trace("paintComponent of " + CardFaceViewViewer.class.getSimpleName() + " completed in " + stopWatch.getTime() + "ms");
    }

    @Override
    protected BufferedImage getCurrentImage() {
        if (!needRefresh)
            return cachedImage;

        StopWatch stopWatch = StopWatch.createStarted();
        cachedImage = CardFaceViewUtils.paintCardFace(cardFaceView, RenderTarget.PREVIEW, resolutionPpi, desiredBleedMarginInPixels);
        logger.trace("Painted card face view to local buffer in " + stopWatch.getTime() + "ms");

        needRefresh = false;

        return cachedImage;
    }

    public void markChanged() {
        needRefresh = true;
        repaint();
    }

    // the y-offset is indexed at 1 with the zoom label being provided by AbstractViewer being index 1
    // each increment moves the label up the text height + a fixed gap
    // this code copied from AbstractView and modified slightly
    private void drawLabel(Graphics2D g, String label, int labelYOffset) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setFont(labelFont);
        FontMetrics fm = g.getFontMetrics();
        int width = fm.stringWidth(label);
        int height = fm.getAscent() + fm.getDescent();
        int xp = getWidth() - width - LABEL_GAP_X - LABEL_MARGIN * 2;
        int yp = getHeight() - height * labelYOffset - LABEL_GAP_Y * labelYOffset;
        g.setColor(new Color(0x77ff_ffff, true));
        g.fillRoundRect(xp, yp + 1, width + 4 + LABEL_MARGIN * 2, height + 2, LABEL_ARC, LABEL_ARC);
        g.setColor(Color.BLACK);
        g.drawRoundRect(xp, yp + 1, width + 4 + LABEL_MARGIN * 2, height + 2, LABEL_ARC, LABEL_ARC);
        yp += fm.getAscent();
        g.drawString(label, xp + 2 + LABEL_MARGIN, yp + 2);
    }

    private static final int LABEL_GAP_X = 8;
    private static final int LABEL_GAP_Y = 8;
    private static final int LABEL_MARGIN = 6;
    private static final int LABEL_ARC = 6;
}

