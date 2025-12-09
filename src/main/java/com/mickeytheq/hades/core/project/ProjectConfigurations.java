package com.mickeytheq.hades.core.project;

import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;

public class ProjectConfigurations {
    private static ProjectConfigurationProvider defaultProvider;

    private static final LazyInitializer<ProjectConfiguration> INSTANCE = new LazyInitializer<ProjectConfiguration>() {
        @Override
        protected ProjectConfiguration initialize() {
            if (defaultProvider == null)
                throw new RuntimeException("ProjectConfigurationProvider not specified");

            return defaultProvider.load();
        }
    };

    public static void setDefaultProvider(ProjectConfigurationProvider provider) {
        ProjectConfigurations.defaultProvider = provider;
    }

    // gets the default project configuration
    public static ProjectConfiguration get() {
        try {
            return INSTANCE.get();
        } catch (ConcurrentException e) {
            throw new RuntimeException(e);
        }
    }

}
