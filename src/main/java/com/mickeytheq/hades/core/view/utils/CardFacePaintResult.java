package com.mickeytheq.hades.core.view.utils;

import java.awt.image.BufferedImage;

public class CardFacePaintResult {
    private final BufferedImage bufferedImage;
    private final int bleedMarginInPixels;
    private final long paintTimeInMilliseconds;

    public CardFacePaintResult(BufferedImage bufferedImage, int bleedMarginInPixels, long paintTimeInMilliseconds) {
        this.bufferedImage = bufferedImage;
        this.bleedMarginInPixels = bleedMarginInPixels;
        this.paintTimeInMilliseconds = paintTimeInMilliseconds;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public int getBleedMarginInPixels() {
        return bleedMarginInPixels;
    }

    public long getPaintTimeInMilliseconds() {
        return paintTimeInMilliseconds;
    }
}
