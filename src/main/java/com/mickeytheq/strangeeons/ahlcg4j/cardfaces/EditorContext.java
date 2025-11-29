package com.mickeytheq.strangeeons.ahlcg4j.cardfaces;

import javax.swing.*;
import java.util.function.Consumer;

public interface EditorContext {
    JTabbedPane getTabbedPane();

    //
    // mark changed methods
    //

    // call to signal a control has changed value so that any associated preview/displays
    // can be repainted
    void markChanged();

    // convenience method that wraps a Consumer (for example when binding an editor component to a member variable) with another
    // that invokes markChanged() after the initial consumer has been called
    default <T> Consumer<T> wrapConsumerWithMarkedChanged(Consumer<T> consumer) {
        return t -> {
            consumer.accept(t);
            markChanged();
        };
    }
}
