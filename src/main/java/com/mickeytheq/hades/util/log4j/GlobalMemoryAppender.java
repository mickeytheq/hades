package com.mickeytheq.hades.util.log4j;

import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

// a log4j2 appender that captures all log message in memory and makes them available globally
// only one instance of this should be used
@Plugin(
        name = GlobalMemoryAppender.NAME,
        category = Core.CATEGORY_NAME,
        elementType = Appender.ELEMENT_TYPE)
public class GlobalMemoryAppender extends AbstractAppender {
    public static final String NAME = "GlobalMemoryAppender";

    private final static Deque<String> logMessages = new ConcurrentLinkedDeque<>();

    public static String getGlobalLog() {
        return String.join("", logMessages);
    }

    public GlobalMemoryAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties) {
        super(name, filter, layout, ignoreExceptions, properties);
    }

    @PluginFactory
    public static GlobalMemoryAppender createAppender(@PluginAttribute("name") String name,
                                                      @PluginElement("Layout") Layout<? extends Serializable> layout,
                                                      @PluginElement("Filter") final Filter filter) {
        if (name == null) {
            name = NAME;
        }

        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }

        return new GlobalMemoryAppender(name, filter, layout, false, null);
    }

    @Override
    public void append(LogEvent event) {
        String message = ((StringLayout) getLayout()).toSerializable(event);

        logMessages.addLast(message);

        if (logMessages.size() > 1000) {
            logMessages.removeFirst();
        }
    }
}
