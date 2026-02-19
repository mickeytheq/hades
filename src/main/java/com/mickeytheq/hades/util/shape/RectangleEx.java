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
    private static final double UNDEFINED = Double.MIN_VALUE;

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

    // creates a rectangle where the resulting draw region will be centred horizontally in the total painting region which
    // must be supplied externally
    public static RectangleEx millimetresHorizontallyCentred(double topY, double width, double height) {
        return new RectangleEx(Unit.Millimetre, UNDEFINED, XRelativeTo.Centred, topY, YRelativeTo.Top, width, height);
    }

    public static RectangleEx millimetresHorizontallyCentred(double topY, DimensionEx size) {
        double ratio = UnitConversionUtils.getConversionRatio(Unit.Millimetre, size.getUnit(), null);

        return new RectangleEx(Unit.Millimetre, UNDEFINED, XRelativeTo.Centred, topY, YRelativeTo.Top, size.getWidth() * ratio, size.getHeight() * ratio);
    }

    public Rectangle toPixelRectangle(double ppi) {
        return toPixelRectangle(ppi, null);
    }

    public RectangleEx centreOn(DimensionEx size) {
        double ratio = UnitConversionUtils.getConversionRatio(unit, size.getUnit(), null);

        return centreOn(ratio * size.getWidth(), ratio * size.getHeight());
    }

    // creates a new RectangleEx with the same units as this one where the
    // position of the new rectangle is centred on this rectangle
    public RectangleEx centreOn(double newWidth, double newHeight) {
        double centreExistingX;

        if (xRelativeTo == XRelativeTo.Left)
            centreExistingX = x + width / 2;
        else
            centreExistingX = UNDEFINED;

        double centreExistingY = y + height / 2;

        return new RectangleEx(unit, centreExistingX, xRelativeTo, centreExistingY - newHeight / 2, yRelativeTo, newWidth, newHeight);
    }

    public Rectangle toPixelRectangle(double ppi, Rectangle templateRegionInPixels) {
        double conversionRatio = UnitConversionUtils.getConversionRatio(unit, Unit.Pixel, ppi);
        int pixelWidth = (int) (width * conversionRatio + 0.5);

        int pixelX;

        if (xRelativeTo == XRelativeTo.Centred) {
             if (templateRegionInPixels == null)
                 throw new RuntimeException("Cannot resolve Centred rectangle without a draw region");

            pixelX = (int)(templateRegionInPixels.width / 2 - pixelWidth / 2);
        }
        else {
            pixelX = (int)(x * conversionRatio + 0.5);
        }

        return new Rectangle(pixelX, (int) (y * conversionRatio + 0.5), pixelWidth, (int) (height * conversionRatio + 0.5));
    }

    public Dimension toPixelDimension(double ppi) {
        double conversionRatio = UnitConversionUtils.getConversionRatio(unit, Unit.Pixel, ppi);

        // add 0.5 as this has the same effect as rounding to the nearest whole number with the int cast
        return new Dimension((int) (width * conversionRatio + 0.5), (int) (height * conversionRatio + 0.5));
    }

    // describes how to interpret/calculate the x position
    public enum XRelativeTo {
        Left, // absolute distance from the left edge of the draw region
        Centred  // centred in the template rather than an absolute position
    }

    public enum YRelativeTo {
        Top // absolute distance from the top edge of the draw region
    }
}
