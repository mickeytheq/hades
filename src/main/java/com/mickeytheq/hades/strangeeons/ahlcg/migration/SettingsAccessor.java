package com.mickeytheq.hades.strangeeons.ahlcg.migration;

public interface SettingsAccessor {
    // returns a settings value, if missing or set to a 'not set' default then return null
    String getString(String settingsKey);

    // returns an Integer parsed from a settings key, if it is missing or invalid then return null
    Integer getIntegerAllowInvalid(String settingsKey);

    // returns an Enum of the given class from a settings key, if it is missing or invalid then return null
    <T extends Enum<T>> T getEnumAllowInvalid(String settingsKey, Class<T> enumClass);
}
