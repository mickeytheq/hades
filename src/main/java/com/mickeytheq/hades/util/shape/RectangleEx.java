package com.mickeytheq.hades.util.shape;

import java.awt.*;

// an immutable Rectangle definition that supports different units
// it's primary use it to support defining draw/painting regions in 'real' units such as millimeters or inches
// real units have the advantage that if the size of the template is changed they are still correct, as opposed to
// defining a draw region in pixels which only works for a specific size of template
//
// conversion to pixels requires the DPI/PPI value to map real -> image. For example converting 10mm to inches at 300 dpi
// would do 10mm / 25.4 inches per mm to get inches and then multiply this by the 300 DPI
//
// if the template size and therefore DPI was changed to 600 - the calculation would result in a pixel output double the amount
public class RectangleEx {
    private final Unit unit;
    private final double x;
    private final XRelativeTo xRelativeTo;
    private final double y;
    private final YRelativeTo yRelativeTo;

    private final double width;
    private final double height;

    private RectangleEx(Unit unit, double x, XRelativeTo xRelativeTo, double y, YRelativeTo yRelativeTo, double width, double height) {
        this.unit = unit;
        this.x = x;
        this.xRelativeTo = xRelativeTo;
        this.y = y;
        this.yRelativeTo = yRelativeTo;
        this.width = width;
        this.height = height;
    }

    public static RectangleEx millimetres(double topLeftX, double topLeftY, double width, double height) {
        return new RectangleEx(Unit.Millimetre, topLeftX, XRelativeTo.Left, topLeftY, YRelativeTo.Top, width, height);
    }

    public static RectangleEx millimetres(double topLeftX, double topLeftY, DimensionEx size) {
        return millimetres(topLeftX, topLeftY,
                UnitConversionUtils.convertUnit(size.getUnit(), Unit.Millimetre, size.getWidth()),
                UnitConversionUtils.convertUnit(size.getUnit(), Unit.Millimetre, size.getHeight()));
    }

    public Rectangle toPixelRectangle(double dpi) {
        double conversionRatio = UnitConversionUtils.getConversionRatio(unit, Unit.Pixel, dpi);

        // add 0.5 as this has the same effect as rounding to the nearest whole number with the int cast
        return new Rectangle((int) (x * conversionRatio + 0.5), (int) (y * conversionRatio + 0.5), (int) (width * conversionRatio + 0.5), (int) (height * conversionRatio + 0.5));
    }

    public enum XRelativeTo {
        Left, Right
    }

    public enum YRelativeTo {
        Top, Bottom
    }
}
