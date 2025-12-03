package com.mickeytheq.ahlcg4j.core.view;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Context used at the 'create editors' stage of the process
 *
 * When a card is hosted within Strange Eons this is used to convey the necessary information/context to {@link CardFaceView}
 * implementations
 */
public interface EditorContext {
    void addDisplayComponent(String title, Component component);

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
