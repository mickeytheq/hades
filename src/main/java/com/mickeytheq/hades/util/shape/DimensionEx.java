package com.mickeytheq.hades.util.shape;

import com.mickeytheq.hades.core.model.common.Distance;

import java.awt.*;

// an immutable Dimension that captures width and height in a Unit
public class DimensionEx {
    private final Unit unit;
    private final double width;
    private final double height;

    private DimensionEx(Unit unit, double width, double height) {
        this.unit = unit;
        this.width = width;
        this.height = height;
    }

    public static DimensionEx create(Unit unit, double width, double height) {
        return new DimensionEx(unit, width, height);
    }

    public static DimensionEx millimetres(double width, double height) {
        return new DimensionEx(Unit.Millimetre, width, height);
    }

    public static DimensionEx inches(double width, double height) {
        return new DimensionEx(Unit.Inch, width, height);
    }

    public Unit getUnit() {
        return unit;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Dimension toPixelDimension(int ppi) {
        double conversionRatio = UnitConversionUtils.getConversionRatio(unit, Unit.Pixel, ppi);
        return new Dimension((int) (conversionRatio * width + 0.5), (int) (conversionRatio * height + 0.5));
    }

    public DimensionEx toUnit(Unit toUnit, int ppi) {
        double conversionRatio = UnitConversionUtils.getConversionRatio(unit, toUnit, ppi);

        return new DimensionEx(toUnit, conversionRatio * width, conversionRatio * height);
    }

    public DimensionEx withAdjustment(double widthAdjust, double heightAdjust) {
        return new DimensionEx(unit, width + widthAdjust, height + heightAdjust);
    }

    public Dimension toPixelDimension() {
        if (unit != Unit.Pixel)
            throw new IllegalStateException("Unable to convert to pixel dimension without a PPI as this dimension not already in pixel unit, unit is " + unit);

        return new Dimension((int) (width + 0.5), (int) (height + 0.5));
    }
}
