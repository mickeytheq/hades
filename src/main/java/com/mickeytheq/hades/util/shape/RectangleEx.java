package com.mickeytheq.hades.util.shape;

import java.awt.*;

public class RectangleEx {
    private final Unit unit;
    private final double topLeftX;
    private final double topLeftY;

    private final double width;
    private final double height;

    private RectangleEx(Unit unit, double topLeftX, double topLeftY, double width, double height) {
        this.unit = unit;
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.width = width;
        this.height = height;
    }

    public static RectangleEx millimeters(double topLeftX, double topLeftY, double width, double height) {
        return new RectangleEx(Unit.Millimetres, topLeftX, topLeftY, width, height);
    }

    public Rectangle toPixelRectangle(double dpi) {
        double conversionRatio = getConversionRatio(unit, Unit.Pixel, dpi);
        return toRectangle(conversionRatio);
    }

    private Rectangle toRectangle(double conversionRatio) {
        // add 0.5 as this has the same effect as rounding to the nearest whole number
        return new Rectangle((int) (topLeftX * conversionRatio + 0.5), (int) (topLeftY * conversionRatio + 0.5), (int) (width * conversionRatio + 0.5), (int) (height * conversionRatio + 0.5));
    }

    private static final double MILLIMETERS_PER_INCH = 10 * 2.54;
    private static final double INCHES_PER_MILLIMETER = 1.0 / MILLIMETERS_PER_INCH;

    private double getConversionRatio(Unit fromUnit, Unit toUnit, double dpi) {
        if (fromUnit == toUnit)
            return 1.0;

        if (fromUnit == Unit.Millimetres && toUnit == Unit.Pixel)
            return INCHES_PER_MILLIMETER * dpi;

        if (fromUnit == Unit.Millimetres && toUnit == Unit.Inches)
            return INCHES_PER_MILLIMETER;

        if (fromUnit == Unit.Inches && toUnit == Unit.Pixel)
            return dpi;

        if (fromUnit == Unit.Inches && toUnit == Unit.Millimetres)
            return MILLIMETERS_PER_INCH;

        if (fromUnit == Unit.Pixel && toUnit == Unit.Millimetres)
            return 1 / (INCHES_PER_MILLIMETER * dpi);

        if (fromUnit == Unit.Pixel && toUnit == Unit.Inches)
            return 1 / dpi;

        throw new UnsupportedOperationException("Unsupported unit conversion from " + fromUnit + " to " + toUnit);
    }
}
