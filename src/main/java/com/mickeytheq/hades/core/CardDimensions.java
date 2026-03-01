package com.mickeytheq.hades.core;

import com.mickeytheq.hades.util.shape.DimensionEx;
import com.mickeytheq.hades.util.shape.Unit;
import com.mickeytheq.hades.util.shape.UnitConversionUtils;

import java.awt.*;

public class CardDimensions {
    // the standard bleed size to be added to cards which is 72 pixels at 600 PPI
    public static final double BLEED_POINTS = 8.64;
    public static final double BLEED_INCHES = UnitConversionUtils.convertUnit(Unit.Point, Unit.Inch, BLEED_POINTS);

    //
    // standard card sizes
    //

    // sized in inches as that is the 'standard' unit used for these cards
    public static final DimensionEx STANDARD_PORTRAIT_INCHES = DimensionEx.inches(2.5, 3.5);
    public static final DimensionEx STANDARD_LANDSCAPE_INCHES = DimensionEx.inches(3.5, 2.5);

    // 600 ppi
    public static final int BLEED_PIXELS_AT_600_PPI = (int) Math.round(UnitConversionUtils.convertUnit(Unit.Point, Unit.Pixel, BLEED_POINTS, 600));

    public static final Dimension STANDARD_PORTRAIT_600_PPI_WITH_BLEED_PIXELS = STANDARD_PORTRAIT_INCHES.toUnit(Unit.Pixel, 600)
            .withAdjustment(BLEED_PIXELS_AT_600_PPI * 2, BLEED_PIXELS_AT_600_PPI * 2)
            .toPixelDimension();

    public static final Dimension STANDARD_LANDSCAPE_600_PPI_WITH_BLEED_PIXELS = new Dimension((int) STANDARD_PORTRAIT_600_PPI_WITH_BLEED_PIXELS.getHeight(), (int) STANDARD_PORTRAIT_600_PPI_WITH_BLEED_PIXELS.getWidth());

    // 300 ppi
    public static final int BLEED_PIXELS_AT_300_PPI = (int) Math.round(UnitConversionUtils.convertUnit(Unit.Point, Unit.Pixel, BLEED_POINTS, 300));

    public static final Dimension STANDARD_PORTRAIT_300_PPI = STANDARD_PORTRAIT_INCHES.toUnit(Unit.Pixel, 300)
            .toPixelDimension();
    public static final Dimension STANDARD_LANDSCAPE_300_PPI = new Dimension((int) STANDARD_PORTRAIT_300_PPI.getHeight(), (int) STANDARD_PORTRAIT_300_PPI.getWidth());

    //
    // mini card sizes
    //
    public static final DimensionEx MINI_CARD_PORTRAIT_INCHES = DimensionEx.inches(1.865, 2.4);
    public static final DimensionEx MINI_CARD_LANDSCAPE_INCHES = DimensionEx.inches(2.4, 1.865);
    public static final DimensionEx MINI_CARD_PORTRAIT_INCHES_WITH_BLEED = MINI_CARD_PORTRAIT_INCHES.withAdjustment(BLEED_INCHES * 2, BLEED_INCHES * 2);

    public static final Dimension MINI_CARD_PORTRAIT_600_PPI_WITH_BLEED_PIXELS = MINI_CARD_PORTRAIT_INCHES.toUnit(Unit.Pixel, 600)
            .withAdjustment(BLEED_PIXELS_AT_600_PPI * 2, BLEED_PIXELS_AT_600_PPI * 2)
            .toPixelDimension();

    public static final Dimension MINI_CARD_LANDSCAPE_600_PPI_WITH_BLEED_PIXELS = new Dimension((int) MINI_CARD_PORTRAIT_600_PPI_WITH_BLEED_PIXELS.getHeight(), (int) MINI_CARD_PORTRAIT_600_PPI_WITH_BLEED_PIXELS.getWidth());
}
