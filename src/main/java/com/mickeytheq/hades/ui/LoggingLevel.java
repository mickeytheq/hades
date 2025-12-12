package com.mickeytheq.hades.ui;

import org.apache.logging.log4j.Level;

public enum LoggingLevel {
    Normal(Level.INFO), Verbose(Level.DEBUG), Debug(Level.TRACE);

    private final Level log4JLevel;

    LoggingLevel(Level log4JLevel) {
        this.log4JLevel = log4JLevel;
    }

    public Level getLog4JLevel() {
        return log4JLevel;
    }

    public static LoggingLevel fromLog4JLevel(Level level) {
        if (level == Level.TRACE)
            return Debug;

        if (level == Level.DEBUG)
            return Verbose;

        return Normal;
    }
}
