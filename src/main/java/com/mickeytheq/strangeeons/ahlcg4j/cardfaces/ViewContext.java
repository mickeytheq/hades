package com.mickeytheq.strangeeons.ahlcg4j.cardfaces;

import ca.cgjennings.layout.MarkupRenderer;

import java.util.function.Consumer;

public interface ViewContext {
    //
    // mark changed methods
    //

    // mark the relevant Strange Eons sheet/game component as changed
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
