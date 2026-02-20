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

    void paintTemplate(Graphics2D graphics2D);
}
