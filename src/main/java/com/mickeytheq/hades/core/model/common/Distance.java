package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.serialise.EmptyEntityDiscriminator;
import com.mickeytheq.hades.util.shape.Unit;

// a distance comprising a unit and an amount of that unit
public class Distance implements EmptyEntityDiscriminator {
    private double amount = 0;
    private Unit unit = Unit.Point;

    public Distance() {
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

    @Property("Amount")
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Property("Unit")
    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
