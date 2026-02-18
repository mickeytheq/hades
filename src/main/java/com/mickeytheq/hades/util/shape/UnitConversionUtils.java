package com.mickeytheq.hades.util.shape;

public class UnitConversionUtils {
    private static final double MILLIMETERS_PER_INCH = 10 * 2.54;
    private static final double INCHES_PER_MILLIMETER = 1.0 / MILLIMETERS_PER_INCH;
    private static final double POINTS_PER_INCH = 72;
    private static final double MILLIMETERS_PER_POINT = MILLIMETERS_PER_INCH / POINTS_PER_INCH;
    private static final double POINTS_PER_MILLIMETER = 1 / MILLIMETERS_PER_POINT;

    public static double convertUnit(Unit fromUnit, Unit toUnit, double value) {
        if (fromUnit == toUnit)
            return value;

        return getConversionRatio(fromUnit, toUnit, null) * value;
    }

    public static double convertUnit(Unit fromUnit, Unit toUnit, double value, double ppi) {
        if (fromUnit == toUnit)
            return value;

        return getConversionRatio(fromUnit, toUnit, ppi) * value;
    }

    public static double getConversionRatio(Unit fromUnit, Unit toUnit, Double ppi) {
        if (fromUnit == toUnit)
            return 1.0;

        if (ppi == null && (fromUnit == Unit.Pixel || toUnit == Unit.Pixel))
            throw new IllegalArgumentException("Cannot convert to/from Pixel unit without PPI being specified");

        if (fromUnit == Unit.Millimetre && toUnit == Unit.Pixel)
            return INCHES_PER_MILLIMETER * ppi;

        if (fromUnit == Unit.Point && toUnit == Unit.Pixel)
            return ppi / POINTS_PER_INCH;

        if (fromUnit == Unit.Millimetre && toUnit == Unit.Point)
            return POINTS_PER_MILLIMETER;

        if (fromUnit == Unit.Point && toUnit == Unit.Millimetre)
            return 1 / POINTS_PER_MILLIMETER;

        if (fromUnit == Unit.Pixel && toUnit == Unit.Millimetre)
            return 1 / (INCHES_PER_MILLIMETER * ppi);

        if (fromUnit == Unit.Pixel && toUnit == Unit.Point)
            return POINTS_PER_INCH / ppi;

        throw new UnsupportedOperationException("Unsupported unit conversion from " + fromUnit + " to " + toUnit);
    }
}
