package com.mickeytheq.hades.core.view.utils;

import ca.cgjennings.apps.arkham.AbstractViewer;
import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import com.mickeytheq.hades.core.view.CardFaceView;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
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
    private final int desiredBleedMarginInPixels;
    private int resolutionPpi;
    private boolean includeBleed = true;
    private boolean drawBleedBorder = false;

    private boolean needRefresh = true;

    // the result of the last paint that is cached and re-painted if markChanged is called
    private CardFacePaintResult cardFacePaintResult;

    public CardFaceViewViewer(CardFaceView cardFaceView, int resolutionPpi, int desiredBleedMarginInPixels) {
        this.cardFaceView = cardFaceView;
        this.resolutionPpi = resolutionPpi;
        this.desiredBleedMarginInPixels = desiredBleedMarginInPixels;

        JMenu resolutionMenu = new JMenu("Resolution");

        JMenuItem resolution300 = new JRadioButtonMenuItem("300", resolutionPpi == 300);
        resolution300.addActionListener(e -> setResolutionPpi(300));
        JMenuItem resolution600 = new JRadioButtonMenuItem("600", resolutionPpi == 600);
        resolution600.addActionListener(e -> setResolutionPpi(600));

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(resolution300);
        buttonGroup.add(resolution600);

        resolutionMenu.add(resolution300);
        resolutionMenu.add(resolution600);

        JMenuItem showBleedMarginMenuItem = new JCheckBoxMenuItem("Include bleed", desiredBleedMarginInPixels > 0);
        showBleedMarginMenuItem.addActionListener(e -> setIncludeBleed(showBleedMarginMenuItem.isSelected()));

        JMenuItem drawBleedBorderMenuItem = new JCheckBoxMenuItem("Draw bleed border", drawBleedBorder);
        drawBleedBorderMenuItem.addActionListener(e -> setDrawBleedBorder(drawBleedBorderMenuItem.isSelected()));

        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(resolutionMenu);
        popupMenu.add(showBleedMarginMenuItem);
        popupMenu.add(drawBleedBorderMenuItem);

        setComponentPopupMenu(popupMenu);
    }

    private void setResolutionPpi(int newResolution) {
        this.resolutionPpi = newResolution;
        markChanged();
    }

    private void setIncludeBleed(boolean includeBleed) {
        this.includeBleed = includeBleed;
        markChanged();
    }

    public void setDrawBleedBorder(boolean drawBleedBorder) {
        this.drawBleedBorder = drawBleedBorder;
        markChanged();
    }

    private int getActualBleedMarginInPixels() {
        if (cardFacePaintResult == null)
            return 0;

        return cardFacePaintResult.getBleedMarginInPixels();
    }

    // override just to adding timing for debugging
    @Override
    protected void paintComponent(Graphics g1) {
        StopWatch stopWatch = StopWatch.createStarted();
        super.paintComponent(g1);

        drawLabel((Graphics2D) g1, resolutionPpi + " PPI", 2);
        drawLabel((Graphics2D) g1, getActualBleedMarginInPixels() + " bleed pixels", 3);
        drawLabel((Graphics2D) g1, cardFacePaintResult.getPaintTimeInMilliseconds() + "ms render time", 4);

        logger.trace("paintComponent of " + CardFaceViewViewer.class.getSimpleName() + " completed in " + stopWatch.getTime() + "ms");
    }

    @Override
    protected BufferedImage getCurrentImage() {
        if (!needRefresh)
            return cardFacePaintResult.getBufferedImage();

        cardFacePaintResult = CardFaceViewUtils.paintCardFaceFullDetails(cardFaceView, RenderTarget.PREVIEW, resolutionPpi, includeBleed ? desiredBleedMarginInPixels : 0);

        paintBleedBorder();

        needRefresh = false;

        return cardFacePaintResult.getBufferedImage();
    }

    private void paintBleedBorder() {
        if (!drawBleedBorder)
            return;

        // draw a 2 pixel width border on the innermost pixels of the bleed margin. these borders are just outside the
        // core card area so will not be visible when the card is painted with no bleed margin
        BufferedImage image = cardFacePaintResult.getBufferedImage();
        Graphics2D graphics2D = image.createGraphics();
        try {
            graphics2D.setColor(Color.RED);

            int x = cardFacePaintResult.getBleedMarginInPixels() - 1;
            int y = cardFacePaintResult.getBleedMarginInPixels() - 1;
            int width = cardFacePaintResult.getBufferedImage().getWidth() - cardFacePaintResult.getBleedMarginInPixels() * 2 + 1;
            int height = cardFacePaintResult.getBufferedImage().getHeight() - cardFacePaintResult.getBleedMarginInPixels() * 2 + 1;

            graphics2D.drawRect(x, y, width, height);

            x = x - 1;
            y = y - 1;
            width = width + 2;
            height = height + 2;

            graphics2D.drawRect(x, y, width, height);
        } finally {
            graphics2D.dispose();
        }
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

