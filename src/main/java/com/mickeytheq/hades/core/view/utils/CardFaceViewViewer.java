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
 * a viewer (really a previewer) that renders a CardFaceView.
 * most of the work is done by AbstractViewer with this implementation providing a BufferedImage to render
 * the image is cached and only rendered when markChanged() is called, typically in response to a user activity
 * such as changing an editor the maps to rendered card content, e.g. the card title
 */
public class CardFaceViewViewer extends AbstractViewer {
    private static final Logger logger = LogManager.getLogger(CardFaceViewViewer.class);

    private final CardFaceView cardFaceView;
    private boolean needRefresh = true;

    // the rendered image that is cached and re-painted if markChanged is called
    private BufferedImage cachedImage;

    public CardFaceViewViewer(CardFaceView cardFaceView) {
        this.cardFaceView = cardFaceView;
    }

    // override just to adding timing for debugging
    @Override
    protected void paintComponent(Graphics g1) {
        StopWatch stopWatch = StopWatch.createStarted();
        super.paintComponent(g1);
        logger.trace("paintComponent of " + CardFaceViewViewer.class.getSimpleName() + " completed in " + stopWatch.getTime() + "ms");
    }

    @Override
    protected BufferedImage getCurrentImage() {
        if (!needRefresh)
            return cachedImage;

        StopWatch stopWatch = StopWatch.createStarted();
        cachedImage = CardFaceViewUtils.paintCardFace(cardFaceView, RenderTarget.PREVIEW, 600);
        logger.trace("Painted card face view to local buffer in " + stopWatch.getTime() + "ms");

        needRefresh = false;

        return cachedImage;
    }

    public void markChanged() {
        needRefresh = true;
        repaint();
    }
}

