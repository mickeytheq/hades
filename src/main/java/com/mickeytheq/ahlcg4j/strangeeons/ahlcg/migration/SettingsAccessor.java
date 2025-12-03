package com.mickeytheq.ahlcg4j.strangeeons.ahlcg.migration;

import resources.Settings;

public interface SettingsAccessor {
    // returns a settings value, if missing or set to a 'not set' default then return null
    String getString(String settingsKey);

    // returns an Integer parsed from a settings key, if it is missing or invalid then return null
    Integer getIntegerAllowInvalid(String settingsKey);

    <T extends Enum<T>> T getEnumAllowInvalid(String settingsKey, Class<T> enumClass);
}
