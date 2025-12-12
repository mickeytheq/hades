package com.mickeytheq.hades.util.log4j;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.StringLayout;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;

import java.io.Serializable;
import java.util.function.Consumer;

public class MemoryAppender extends AbstractAppender {
    private final Consumer<String> logMessageConsumer;

    public MemoryAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties, Consumer<String> logMessageConsumer) {
        super(name, filter, layout, ignoreExceptions, properties);

        this.logMessageConsumer = logMessageConsumer;
    }

    @Override
    public void append(LogEvent event) {
        String message = ((StringLayout)getLayout()).toSerializable(event);

        logMessageConsumer.accept(message);
    }

    @Override
    public boolean isStarted() {
        return true;
    }
}
