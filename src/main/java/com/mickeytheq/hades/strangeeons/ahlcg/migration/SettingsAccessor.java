package com.mickeytheq.hades.strangeeons.ahlcg.migration;

// this will automatically add any required suffix such as 'Back' to the key when required so this
// can be used regardless of whether handling front or back faces
public interface SettingsAccessor {
    // returns a settings value, if missing, empty string or set to a 'not set' default then return null
    String getString(String settingsKey);

    // returns an Integer parsed from a settings key, if it is missing or invalid then return null
    Integer getIntegerAllowInvalid(String settingsKey);

    // returns an Enum of the given class from a settings key, if it is missing or invalid then return null
    <T extends Enum<T>> T getEnumAllowInvalid(String settingsKey, Class<T> enumClass);

    // returns the settings key this accessor will resolve the given key to, i.e. adding any suffix
    String getFullSettingsKey(String settingsKey);

    // ignores any settings key adjustment by the accessor and use exactly the value passed in
    // should be used sparingly to access settings that do not confirm to the typical 'Back' prefix pattern
    String getRawSettingsValue(String fullSettingsKey);

    // special method to handle getting spacing values which has a suffix at the end (after the 'Back' suffix)
    // for example passing in 'Rules' on a card back face the resulting key will be 'RulesBackSpacing'
    // any errors will just return 0
    int getSpacingValue(String spacingPrefixKey);
}
