package com.mickeytheq.hades.core.view;

import com.google.common.collect.Lists;
import com.mickeytheq.hades.core.view.utils.ImageUtils;
import com.mickeytheq.hades.util.shape.Unit;
import com.mickeytheq.hades.util.shape.UnitConversionUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.Supplier;

public class TemplateInfos {
    private static final int BLEED_600_PIXELS = 72;
    private static final Dimension PORTRAIT_600 = new Dimension(1500 + BLEED_600_PIXELS * 2, 2100 + BLEED_600_PIXELS * 2);
    private static final Dimension LANDSCAPE_600 = new Dimension(2100 + BLEED_600_PIXELS * 2, 1500 + BLEED_600_PIXELS * 2);

    private static final double BLEED_600_POINTS = BLEED_600_PIXELS / 600.0 * 72.0;

    public static TemplateInfo createStandard600(String resourcePathPrefixWithoutPpiOrExtension, CardFaceOrientation orientation) {
        String resourcePath = getTemplateResourcePath(resourcePathPrefixWithoutPpiOrExtension, 600, "png");

        if (orientation == CardFaceOrientation.Portrait) {
            return new TemplateInfoImpl(PORTRAIT_600, 600, BLEED_600_PIXELS, BLEED_600_POINTS, () -> ImageUtils.loadImageReadOnly(resourcePath));
        }
        else {
            return new TemplateInfoImpl(LANDSCAPE_600, 600, BLEED_600_PIXELS, BLEED_600_POINTS, () -> ImageUtils.loadImageReadOnly(resourcePath));
        }
    }

    private static final Dimension PORTRAIT_300 = new Dimension(750, 1050);
    private static final Dimension LANDSCAPE_300 = new Dimension(1050, 750);

    public static TemplateInfo createStandard300(String resourcePathPrefixWithoutPpiOrExtension, CardFaceOrientation orientation) {
        String templateResourcePath = getTemplateResourcePath(resourcePathPrefixWithoutPpiOrExtension, 300, "png");

        if (orientation == CardFaceOrientation.Portrait) {
            return new TemplateInfoImpl(PORTRAIT_300, 300, 0, 0, () -> ImageUtils.loadImageReadOnly(templateResourcePath));
        }
        else {
            return new TemplateInfoImpl(LANDSCAPE_300, 300, 0, 0, () -> ImageUtils.loadImageReadOnly(templateResourcePath));
        }
    }

    // the convention for resource paths is <prefix>_<ppi>.png
    private static String getTemplateResourcePath(String resourcePathPrefixWithoutPpiOrExtension, int ppi, String extension) {
        StringBuilder sb = new StringBuilder();
        sb.append(resourcePathPrefixWithoutPpiOrExtension);

        // TODO: update when _300 versions are generated to always add the ppi
        if (ppi == 600) {
            sb.append("_");
            sb.append(ppi);
        }

        sb.append(".");
        sb.append(extension);

        return sb.toString();
    }

    // convience method to generate the standard 300 and 600 templates
    public static List<TemplateInfo> createStandard300And600(String resourcePathPrefixWithoutPpiOrExtension, CardFaceOrientation cardFaceOrientation) {
        return Lists.newArrayList(
                createStandard300(resourcePathPrefixWithoutPpiOrExtension, cardFaceOrientation),
                createStandard600(resourcePathPrefixWithoutPpiOrExtension, cardFaceOrientation)
        );
    }

    static class TemplateInfoImpl implements TemplateInfo {
        private final Dimension dimension;
        private final int ppi;
        private final int bleedMarginInPixels;
        private final double bleedMarginInPoints;
        private final Supplier<BufferedImage> bufferedImageSupplier;

        public TemplateInfoImpl(Dimension dimension, int ppi, int bleedMarginInPixels, double bleedMarginInPoints, Supplier<BufferedImage> bufferedImageSupplier) {
            this.dimension = dimension;
            this.ppi = ppi;
            this.bleedMarginInPixels = bleedMarginInPixels;
            this.bleedMarginInPoints = bleedMarginInPoints;
            this.bufferedImageSupplier = bufferedImageSupplier;
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
            return bufferedImageSupplier.get();
        }
    }
}
