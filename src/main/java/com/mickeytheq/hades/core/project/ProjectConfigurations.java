package com.mickeytheq.hades.core.project;

import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProjectConfigurations {
    private static ProjectConfigurationProvider defaultProvider;

    private static ProjectConfiguration INSTANCE;
    private static final Lock LOCK = new ReentrantLock();

    public static void setDefaultProvider(ProjectConfigurationProvider provider) {
        // change the provider for the project configuration
        // clear the cached instance so it will be reloaded next time the project configuration is requested
        LOCK.lock();
        try {
            ProjectConfigurations.defaultProvider = provider;
            INSTANCE = null;
        }
        finally {
            LOCK.unlock();
        }
    }

    // gets the default project configuration
    // note that the result of this method will return the project configuration for the current open project
    // for example if a different Strange Eons project is opened
    // TODO: need to keep an eye on this this is method is used as the result is currently tied to the current open SE project
    // TODO: but SE allows card editors from anywhere to remain open/be opened outside the containing project
    // TODO: if the card painting ever accesses the ProjectConfiguration it will see the current project, not the project the card is from
    // TODO: currently this isn't an issue but needs considering. perhaps individual CardFaceViews should load and keep a reference
    // TODO: to the ProjectConfiguration, although this has a different problem of it becoming stale if the project is closed and opened
    // TODO: and changes are made
    public static ProjectConfiguration get() {
        LOCK.lock();
        try {
            if (INSTANCE != null)
                return INSTANCE;

            if (defaultProvider == null)
                throw new RuntimeException("ProjectConfigurationProvider not specified");

            INSTANCE = defaultProvider.load();

            return INSTANCE;
        }
        finally {
            LOCK.unlock();
        }
    }

}
