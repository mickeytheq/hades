package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.serialise.EmptyEntityDiscriminator;
import org.apache.commons.lang3.StringUtils;

// to model statistics such as per investigator numerals or blank/X/- values
// typically on enemies but investigators can also have weird options
public class Statistic implements EmptyEntityDiscriminator {
    private String value;
    private boolean perInvestigator;

    public Statistic() {
    }

    public Statistic(String value, boolean perInvestigator) {
        this.value = value;
        this.perInvestigator = perInvestigator;
    }

    @Property("PerInvestigator")
    public boolean isPerInvestigator() {
        return perInvestigator;
    }

    public void setPerInvestigator(boolean perInvestigator) {
        this.perInvestigator = perInvestigator;
    }

    @Property("Value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static Statistic empty() {
        return new Statistic(null, false);
    }

    @Override
    public boolean isEmpty() {
        return StringUtils.isEmpty(value);
    }
}
