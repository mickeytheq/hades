package com.mickeytheq.hades.core;

import com.mickeytheq.hades.util.shape.Unit;
import com.mickeytheq.hades.util.shape.UnitConversionUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestUnitConversion {
    @Test
    public void testInches() {
        Assert.assertEquals(UnitConversionUtils.convertUnit(Unit.Inch, Unit.Inch, 10), 10);
        Assert.assertEquals(UnitConversionUtils.convertUnit(Unit.Inch, Unit.Pixel, 3.5, 600), 2100);
        Assert.assertEquals(Math.round(UnitConversionUtils.convertUnit(Unit.Inch, Unit.Pixel, 2.5, 600)), 1500);
        Assert.assertEquals(UnitConversionUtils.convertUnit(Unit.Inch, Unit.Millimetre, 2), 2 * 25.4);
        Assert.assertEquals(UnitConversionUtils.convertUnit(Unit.Inch, Unit.Point, 1), 72);
    }

    @Test
    public void testPoints() {
        Assert.assertEquals(UnitConversionUtils.convertUnit(Unit.Point, Unit.Inch, 72), 1);
        Assert.assertEquals(UnitConversionUtils.convertUnit(Unit.Point, Unit.Millimetre, 36), 0.5 * 25.4);
        Assert.assertEquals(UnitConversionUtils.convertUnit(Unit.Point, Unit.Point, 2), 2);
        Assert.assertEquals(Math.round(UnitConversionUtils.convertUnit(Unit.Point, Unit.Pixel, 36, 600)), 300);
    }

    @Test
    public void testMillimeters() {
        Assert.assertEquals(UnitConversionUtils.convertUnit(Unit.Millimetre, Unit.Millimetre, 36), 36);
        Assert.assertEquals(UnitConversionUtils.convertUnit(Unit.Millimetre, Unit.Inch, 25.4), 1);
        Assert.assertEquals(UnitConversionUtils.convertUnit(Unit.Millimetre, Unit.Point, 25.4), 72);
        Assert.assertEquals(Math.round(UnitConversionUtils.convertUnit(Unit.Millimetre, Unit.Pixel, 88.9, 600)), 2100);
        Assert.assertEquals(Math.round(UnitConversionUtils.convertUnit(Unit.Millimetre, Unit.Pixel, 63.5, 600)), 1500);
    }
}
