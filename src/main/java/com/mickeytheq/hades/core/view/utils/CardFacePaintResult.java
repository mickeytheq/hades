package com.mickeytheq.hades.core.view.utils;

import java.awt.image.BufferedImage;

public class CardFacePaintResult {
    public enum Status {
        Success, NoTemplate
    }

    private final Status status;
    private final BufferedImage bufferedImage;
    private final int bleedMarginInPixels;
    private final long paintTimeInMilliseconds;

    public CardFacePaintResult(Status status, BufferedImage bufferedImage, int bleedMarginInPixels, long paintTimeInMilliseconds) {
        this.status = status;
        this.bufferedImage = bufferedImage;
        this.bleedMarginInPixels = bleedMarginInPixels;
        this.paintTimeInMilliseconds = paintTimeInMilliseconds;
    }

    public Status getStatus() {
        return status;
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
