package com.mickeytheq.strangeeons.ahlcg4j.cardfaces.common;

// to model statistics such as per investigator numerals or blank/X/- values
// typically on enemies but investigators can also have weird options
public class Statistic {
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
}
