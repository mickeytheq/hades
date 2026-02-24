package com.mickeytheq.hades.core.view;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface TemplateInfo {
    // returns the total width including any bleed
    int getWidthInPixels();

    // returns the total height including any bleed
    int getHeightInPixels();

    // returns the dimensions including bleed
    default Dimension getDimension() {
        return new Dimension(getWidthInPixels(), getHeightInPixels());
    }

    int getResolutionInPixelsPerInch();

    int getAvailableBleedMarginInPixels();

    default int getWidthInPixelsWithoutBleed() {
        return getWidthInPixels() - getAvailableBleedMarginInPixels() * 2;
    }

    default int getHeightInPixelsWithoutBleed() {
        return getHeightInPixels() - getAvailableBleedMarginInPixels() * 2;
    }

    double getAvailableBleedMarginInPoints();

    BufferedImage getTemplateImage();

    // returns a new TemplateInfo derived from this one with the resolution up or downscaled appropriately to the new resolution
    TemplateInfo scaleToResolution(int newPpi);
}
