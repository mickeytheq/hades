package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.serialise.EmptyEntityDiscriminator;
import com.mickeytheq.hades.util.shape.Unit;

// a distance comprising a unit and an amount of that unit
public class Distance implements EmptyEntityDiscriminator {
    private final double amount;
    private final Unit unit;

    public static Distance createZeroPoint() {
        return new Distance(0, Unit.Point);
    }

    public Distance(double amount, Unit unit) {
        this.amount = amount;
        this.unit = unit;
    }

    @Override
    public boolean isEmpty() {
        // 0 is the default so consider it empty when set to zero
        return amount == 0;
    }

    public double getAmount() {
        return amount;
    }

    public Unit getUnit() {
        return unit;
    }
}
