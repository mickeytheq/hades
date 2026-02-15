package com.mickeytheq.hades.core.global.configuration;

public interface GlobalConfigurationProvider {
    GlobalConfiguration load();

    void save(GlobalConfiguration globalConfiguration);
}
