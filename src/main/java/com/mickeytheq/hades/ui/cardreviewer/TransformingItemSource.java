package com.mickeytheq.hades.ui.cardreviewer;

import java.util.function.Function;

public class TransformingItemSource<S, T> implements ItemSource<T> {
    private final ItemSource<S> source;
    private final Function<S, T> transformer;

    public TransformingItemSource(ItemSource<S> source, Function<S, T> transformer) {
        this.source = source;
        this.transformer = transformer;
    }

    @Override
    public T getCurrent() {
        return transformer.apply(source.getCurrent());
    }

    @Override
    public boolean next() {
        return source.next();
    }

    @Override
    public boolean previous() {
        return source.previous();
    }

    @Override
    public int getCurrentIndex() {
        return source.getCurrentIndex();
    }

    @Override
    public Integer getTotal() {
        return source.getTotal();
    }
}
