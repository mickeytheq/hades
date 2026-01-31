package com.mickeytheq.hades.util.shape;

import com.mickeytheq.hades.core.model.common.Distance;

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

    public static DimensionEx millimetres(double width, double height) {
        return new DimensionEx(Unit.Millimetre, width, height);
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
}
