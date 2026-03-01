package com.mickeytheq.hades.core.view;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.core.CardDimensions;
import com.mickeytheq.hades.core.view.utils.ImageUtils;
import com.mickeytheq.hades.util.shape.DimensionEx;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.Supplier;

public class TemplateInfos {
    private static final int BLEED_STANDARD_600_PIXELS = CardDimensions.BLEED_PIXELS_AT_600_PPI;
    private static final Dimension PORTRAIT_600 = CardDimensions.STANDARD_PORTRAIT_600_PPI_WITH_BLEED_PIXELS;
    private static final Dimension LANDSCAPE_600 = CardDimensions.STANDARD_LANDSCAPE_600_PPI_WITH_BLEED_PIXELS;

    private static final double BLEED_POINTS = CardDimensions.BLEED_POINTS;

    public static TemplateInfo createStandard600(String resourcePathPrefixWithoutPpiOrExtension, CardFaceOrientation orientation) {
        String resourcePath = getTemplateResourcePath(resourcePathPrefixWithoutPpiOrExtension, 600, "png");

        if (orientation == CardFaceOrientation.Portrait) {
            return new TemplateInfoImpl(PORTRAIT_600, 600, BLEED_STANDARD_600_PIXELS, () -> ImageUtils.loadImageReadOnly(resourcePath));
        } else {
            return new TemplateInfoImpl(LANDSCAPE_600, 600, BLEED_STANDARD_600_PIXELS, () -> ImageUtils.loadImageReadOnly(resourcePath));
        }
    }

    // TODO: version with bleed pixels where available
    private static final Dimension PORTRAIT_300 = CardDimensions.STANDARD_PORTRAIT_300_PPI;
    private static final Dimension LANDSCAPE_300 = CardDimensions.STANDARD_LANDSCAPE_300_PPI;

    public static TemplateInfo createStandard300(String resourcePathPrefixWithoutPpiOrExtension, CardFaceOrientation orientation) {
        String templateResourcePath = getTemplateResourcePath(resourcePathPrefixWithoutPpiOrExtension, 300, "png");

        if (orientation == CardFaceOrientation.Portrait) {
            return new TemplateInfoImpl(PORTRAIT_300, 300, 0, () -> ImageUtils.loadImageReadOnly(templateResourcePath));
        } else {
            return new TemplateInfoImpl(LANDSCAPE_300, 300, 0, () -> ImageUtils.loadImageReadOnly(templateResourcePath));
        }
    }

    // convience method to generate the standard 300 and 600 templates
    public static List<TemplateInfo> createStandard300And600(String resourcePathPrefixWithoutPpiOrExtension, CardFaceOrientation cardFaceOrientation) {
        return Lists.newArrayList(
                createStandard300(resourcePathPrefixWithoutPpiOrExtension, cardFaceOrientation),
                createStandard600(resourcePathPrefixWithoutPpiOrExtension, cardFaceOrientation)
        );
    }

    private static final Dimension MINI_CARD_600_PORTRAIT = new Dimension(975 + BLEED_STANDARD_600_PIXELS * 2, 1440 + BLEED_STANDARD_600_PIXELS * 2);
    private static final Dimension MINI_CARD_600_LANDSCAPE = new Dimension((int) MINI_CARD_600_PORTRAIT.getHeight(), (int) MINI_CARD_600_PORTRAIT.getWidth());

    public static TemplateInfo createBlankMiniCard600(CardFaceOrientation cardFaceOrientation) {
        Dimension dimension = cardFaceOrientation == CardFaceOrientation.Portrait ? MINI_CARD_600_PORTRAIT : MINI_CARD_600_LANDSCAPE;

        return new TemplateInfoImpl(dimension, 600, BLEED_STANDARD_600_PIXELS, () -> null);
    }

    public static TemplateInfo createBlankCustom600(DimensionEx dimension) {
        Dimension dimensionInPixels = dimension.toPixelDimension(600);
        return new TemplateInfoImpl(dimensionInPixels, 600, BLEED_STANDARD_600_PIXELS, () -> null);
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

    static class TemplateInfoImpl implements TemplateInfo {
        private final Dimension dimensionInPixels;
        private final int ppi;
        private final int bleedMarginInPixels;
        private final Supplier<BufferedImage> bufferedImageSupplier;

        public TemplateInfoImpl(Dimension dimensionInPixels, int ppi, int bleedMarginInPixels, Supplier<BufferedImage> bufferedImageSupplier) {
            this.dimensionInPixels = dimensionInPixels;
            this.ppi = ppi;
            this.bleedMarginInPixels = bleedMarginInPixels;
            this.bufferedImageSupplier = bufferedImageSupplier;
        }

        @Override
        public int getWidthInPixels() {
            return (int) dimensionInPixels.getWidth();
        }

        @Override
        public int getHeightInPixels() {
            return (int) dimensionInPixels.getHeight();
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
        public BufferedImage getTemplateImage() {
            return bufferedImageSupplier.get();
        }

        @Override
        public TemplateInfo scaleToResolution(int newPpi) {
            double ratio = (double) newPpi / ppi;

            int newWidth = (int) (dimensionInPixels.getWidth() * ratio);
            int newHeight = (int) (dimensionInPixels.getHeight() * ratio);
            int newBleedPixels = (int) (bleedMarginInPixels * ratio);

            BufferedImage unscaledImage = bufferedImageSupplier.get();

            BufferedImage scaledImage;

            if (unscaledImage != null) {
                scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics2D = scaledImage.createGraphics();
                try {
                    // TODO: assumes export quality - pass in the RenderTarget?
                    RenderTarget.EXPORT.applyTo(graphics2D);

                    graphics2D.drawImage(unscaledImage, 0, 0, newWidth, newHeight, null);
                } finally {
                    graphics2D.dispose();
                }
            }
            else {
                scaledImage = null;
            }

            return new DerivedTemplateInfoImpl(new Dimension(newWidth, newHeight), newPpi, newBleedPixels, () -> scaledImage, this);
        }
    }

    static class DerivedTemplateInfoImpl extends TemplateInfoImpl {
        private final TemplateInfo sourceTemplate;

        public DerivedTemplateInfoImpl(Dimension dimension, int ppi, int bleedMarginInPixels, Supplier<BufferedImage> bufferedImageSupplier, TemplateInfo sourceTemplate) {
            super(dimension, ppi, bleedMarginInPixels, bufferedImageSupplier);
            this.sourceTemplate = sourceTemplate;
        }

        @Override
        public TemplateInfo getSourceTemplate() {
            return sourceTemplate;
        }
    }
}
