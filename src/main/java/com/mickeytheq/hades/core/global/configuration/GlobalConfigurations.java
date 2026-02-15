package com.mickeytheq.hades.core.global.configuration;

public class GlobalConfigurations {
    private static GlobalConfigurationProvider provider;

    public static GlobalConfigurationProvider getProvider() {
        return provider;
    }

    public static void setProvider(GlobalConfigurationProvider provider) {
        GlobalConfigurations.provider = provider;
    }

    public static GlobalConfiguration get() {
        return getProvider().load();
    }

    public static void save(GlobalConfiguration globalConfiguration) {
        getProvider().save(globalConfiguration);
    }
}
