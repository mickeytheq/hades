package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.serialise.EmptyEntityDiscriminator;
import org.apache.commons.lang3.StringUtils;

// to model statistics such as per investigator numerals or blank/X/- values
// typically on enemies but investigators can also have weird options
public class Statistic implements EmptyEntityDiscriminator {
    private final String value;
    private final boolean perInvestigator;

    public Statistic(String value, boolean perInvestigator) {
        this.value = value;
        this.perInvestigator = perInvestigator;
    }

    public boolean isPerInvestigator() {
        return perInvestigator;
    }

    public String getValue() {
        return value;
    }

    public static Statistic empty() {
        return new Statistic(null, false);
    }

    @Override
    public boolean isEmpty() {
        return StringUtils.isEmpty(value);
    }
}
