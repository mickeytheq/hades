package com.mickeytheq.strangeeons.ahlcg4j;

public enum CardFaceSide {
    Front("Front"), Back("Back");

    CardFaceSide(String settingsPrefix) {
        this.settingsPrefix = settingsPrefix;
    }

    private final String settingsPrefix;

    public String getSettingsPrefix() {
        return settingsPrefix;
    }
}
