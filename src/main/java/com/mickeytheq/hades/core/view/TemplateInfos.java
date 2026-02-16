package com.mickeytheq.hades.core.view;

import com.mickeytheq.hades.core.view.utils.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TemplateInfos {
    private static final int BLEED_600_PIXELS = 72;
    private static final Dimension PORTRAIT_600 = new Dimension(1500 + BLEED_600_PIXELS * 2, 2100 + BLEED_600_PIXELS * 2);
    private static final Dimension LANDSCAPE_600 = new Dimension(2100 + BLEED_600_PIXELS * 2, 1500 + BLEED_600_PIXELS * 2);

    private static final double BLEED_600_POINTS = BLEED_600_PIXELS / 600.0 * 72.0;

    public static TemplateInfo createStandard600(String resourcePath, CardFaceOrientation orientation) {
        if (orientation == CardFaceOrientation.Portrait) {
            return new TemplateInfoImpl(PORTRAIT_600, 600, BLEED_600_PIXELS, BLEED_600_POINTS, resourcePath);
        }
        else {
            return new TemplateInfoImpl(LANDSCAPE_600, 600, BLEED_600_PIXELS, BLEED_600_POINTS, resourcePath);
        }
    }

    private static final Dimension PORTRAIT_300 = new Dimension(750, 1050);
    private static final Dimension LANDSCAPE_300 = new Dimension(1050, 750);

    public static TemplateInfo createStandard300(String resourcePath, CardFaceOrientation orientation) {
        if (orientation == CardFaceOrientation.Portrait) {
            return new TemplateInfoImpl(PORTRAIT_300, 300, 0, 0, resourcePath);
        }
        else {
            return new TemplateInfoImpl(LANDSCAPE_300, 300, 0, 0, resourcePath);
        }
    }

    static class TemplateInfoImpl implements TemplateInfo {
        private final Dimension dimension;
        private final int ppi;
        private final int bleedMarginInPixels;
        private final double bleedMarginInPoints;
        private final String resourcePath;

        public TemplateInfoImpl(Dimension dimension, int ppi, int bleedMarginInPixels, double bleedMarginInPoints, String resourcePath) {
            this.dimension = dimension;
            this.ppi = ppi;
            this.bleedMarginInPixels = bleedMarginInPixels;
            this.bleedMarginInPoints = bleedMarginInPoints;
            this.resourcePath = resourcePath;
        }

        @Override
        public int getWidthInPixels() {
            return (int)dimension.getWidth();
        }

        @Override
        public int getHeightInPixels() {
            return (int)dimension.getHeight();
        }

        @Override
        public int getResolutionInPixelsPerInch() {
            return ppi;
        }

        @Override
        public int getAvailableBleedMarginInPixels() {
            return bleedMarginInPixels;
        }

        @Override
        public double getAvailableBleedMarginInPoints() {
            return bleedMarginInPoints;
        }

        @Override
        public BufferedImage getTemplateImage() {
            return ImageUtils.loadImageReadOnly(resourcePath);
        }
    }
}
