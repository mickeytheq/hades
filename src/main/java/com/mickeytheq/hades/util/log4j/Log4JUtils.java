package com.mickeytheq.hades.util.log4j;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.util.function.Supplier;

public class Log4JUtils {
    private static final String HADES_GLOBAL_LOGGER_NAME = "com.mickeytheq.hades";

    public static Level getHadesGlobalLoggerLevel() {
        return LogManager.getLogger(HADES_GLOBAL_LOGGER_NAME).getLevel();
    }

    public static void setHadesGlobalLoggerLevel(Level level) {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(HADES_GLOBAL_LOGGER_NAME);
        loggerConfig.setLevel(level);
        ctx.updateLoggers();
    }

    // attaches the new appender to the main hades logger with the given level and filter
    // then invokes the given callback
    // on return from the callback the appender will be removed
    public static <T> T withAppender(Appender appender, Level level, Filter filter, Supplier<T> callback) {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(HADES_GLOBAL_LOGGER_NAME);

        loggerConfig.addAppender(appender, level, filter);
        ctx.updateLoggers();
        try {
            return callback.get();
        } finally {
            loggerConfig.removeAppender(appender.getName());
            ctx.updateLoggers();
        }
    }

    public static void withAppender(Appender appender, Level level, Filter filter, Runnable runnable) {
        withAppender(appender, level, filter, () -> {
            runnable.run();
            return null;
        });
    }
}
