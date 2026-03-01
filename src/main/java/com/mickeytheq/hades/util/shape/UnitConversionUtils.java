package com.mickeytheq.hades.util.shape;

import org.apache.commons.math3.util.Precision;

public class UnitConversionUtils {
    private static final double MILLIMETERS_PER_INCH = 10 * 2.54;
    private static final double INCHES_PER_MILLIMETER = 1.0 / MILLIMETERS_PER_INCH;
    private static final double POINTS_PER_INCH = 72;
    private static final double MILLIMETERS_PER_POINT = MILLIMETERS_PER_INCH / POINTS_PER_INCH;
    private static final double POINTS_PER_MILLIMETER = 1 / MILLIMETERS_PER_POINT;

    public static double convertUnit(Unit fromUnit, Unit toUnit, double value) {
        if (fromUnit == toUnit)
            return value;

        // round to avoid minor variation that can occur with floating point numbers
        // 8 dp is more than enough for what we need.
        // to ensure that 0.99999999999 etc end up as what we
        // actually expect 1.0. this isn't perfectly precise but it is good enough for our purposes
        return Precision.round(getConversionRatio(fromUnit, toUnit, null) * value, 8);
    }

    public static double convertUnit(Unit fromUnit, Unit toUnit, double value, int ppi) {
        if (fromUnit == toUnit)
            return value;

        return Precision.round(getConversionRatio(fromUnit, toUnit, ppi) * value, 8);
    }

    public static double getConversionRatio(Unit fromUnit, Unit toUnit, Integer ppi) {
        if (fromUnit == toUnit)
            return 1.0;

        if (ppi == null && (fromUnit == Unit.Pixel || toUnit == Unit.Pixel))
            throw new IllegalArgumentException("Cannot convert to/from Pixel unit without PPI being specified");

        // normalise everything to millimeter and then recurse
        if (fromUnit == Unit.Point)
            return MILLIMETERS_PER_POINT * getConversionRatio(Unit.Millimetre, toUnit, ppi);

        if (toUnit == Unit.Point)
            return POINTS_PER_MILLIMETER * getConversionRatio(fromUnit, Unit.Millimetre, ppi);

        if (fromUnit == Unit.Inch)
            return MILLIMETERS_PER_INCH * getConversionRatio(Unit.Millimetre, toUnit, ppi);

        if (toUnit == Unit.Inch)
            return INCHES_PER_MILLIMETER * getConversionRatio(fromUnit, Unit.Millimetre, ppi);

        // resolve PPI
        if (fromUnit == Unit.Millimetre && toUnit == Unit.Pixel)
            return INCHES_PER_MILLIMETER * ppi;

        if (fromUnit == Unit.Pixel && toUnit == Unit.Millimetre)
            return 1 / (INCHES_PER_MILLIMETER * ppi);

        throw new UnsupportedOperationException("Unsupported unit conversion from " + fromUnit + " to " + toUnit);
    }
}
