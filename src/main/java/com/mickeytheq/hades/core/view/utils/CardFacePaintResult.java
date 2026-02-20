package com.mickeytheq.hades.core.view.utils;

import java.awt.image.BufferedImage;

public class CardFacePaintResult {
    private final BufferedImage bufferedImage;
    private final int bleedMarginInPixels;

    public CardFacePaintResult(BufferedImage bufferedImage, int bleedMarginInPixels) {
        this.bufferedImage = bufferedImage;
        this.bleedMarginInPixels = bleedMarginInPixels;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public int getBleedMarginInPixels() {
        return bleedMarginInPixels;
    }
}
