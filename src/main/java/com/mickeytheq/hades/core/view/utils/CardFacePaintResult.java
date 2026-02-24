package com.mickeytheq.hades.core.view.utils;

import java.awt.image.BufferedImage;

public class CardFacePaintResult {
    public enum Status {
        Success, NoTemplate
    }

    private final Status status;
    private final BufferedImage bufferedImage;
    private final int paintedBleedMarginInPixels;
    private final long paintTimeInMilliseconds;
    private final int paintedPpi;
    private final int sourceTemplatePpi;

    public CardFacePaintResult(Status status, BufferedImage bufferedImage, int paintedBleedMarginInPixels, long paintTimeInMilliseconds, int paintedPpi, int sourceTemplatePpi) {
        this.status = status;
        this.bufferedImage = bufferedImage;
        this.paintedBleedMarginInPixels = paintedBleedMarginInPixels;
        this.paintTimeInMilliseconds = paintTimeInMilliseconds;
        this.paintedPpi = paintedPpi;
        this.sourceTemplatePpi = sourceTemplatePpi;
    }

    public Status getStatus() {
        return status;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public int getPaintedBleedMarginInPixels() {
        return paintedBleedMarginInPixels;
    }

    public long getPaintTimeInMilliseconds() {
        return paintTimeInMilliseconds;
    }

    public int getPaintedPpi() {
        return paintedPpi;
    }

    public int getSourceTemplatePpi() {
        return sourceTemplatePpi;
    }
}
