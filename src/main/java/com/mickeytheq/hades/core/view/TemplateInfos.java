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
        public void paintTemplate(Graphics2D graphics2D) {
            graphics2D.drawImage(bufferedImageSupplier.get(), 0, 0, getWidthInPixels(), getHeightInPixels(), null);
        }

        @Override
        public TemplateInfo withBleedMarginInPixels(int desiredBleedMarginInPixels) {
            // bound the desired bleed margin with the available bleed margin on the upper end and 0 on the lower end
            int targetBleedInPixels = Math.min(desiredBleedMarginInPixels, bleedMarginInPixels);
            targetBleedInPixels = Math.max(0, targetBleedInPixels);

            // calculate the offset into the raw template image that should be drawn when the template is painted
            // this is just the difference between the available template bleed margin and the desired bleed margin
            int drawingOffset = bleedMarginInPixels - targetBleedInPixels;

            int newTemplateWidth = getWidthInPixelsWithoutBleed() + targetBleedInPixels * 2;
            int newTemplateHeight = getHeightInPixelsWithoutBleed() + targetBleedInPixels * 2;

            // provide a new template info with the new values it will act as if it is a template with the desired bleed
            // margin with the same PPI and the painting logic draw accordingly
            return new TemplateInfoImpl(new Dimension(newTemplateWidth, newTemplateHeight), ppi,
                    targetBleedInPixels, UnitConversionUtils.convertUnit(Unit.Pixel, Unit.Point, targetBleedInPixels, ppi),
                    bufferedImageSupplier) {
                @Override
                public void paintTemplate(Graphics2D graphics2D) {
                    // draw the template image into the top corner of the destination but offsetting the source rectangle
                    // by the amount of bleed margin that has been removed
                    // note that this drawImage() method uses specifies the top left and bottom right corners instead of top left and width/height
                    graphics2D.drawImage(bufferedImageSupplier.get(), 0, 0, newTemplateWidth, newTemplateHeight,
                            drawingOffset, drawingOffset, drawingOffset + newTemplateWidth, drawingOffset + newTemplateHeight, null);
                }
            };
        }
    }
}
