package com.mickeytheq.hades.ui.cardreviewer;

import java.util.List;

public class ListItemSource<T> implements ItemSource<T> {
    private final List<T> items;

    private int currentIndex = -1;

    public ListItemSource(List<T> items) {
        this.items = items;
    }

    @Override
    public T getCurrent() {
        if (currentIndex < 0)
            throw new RuntimeException("Index not initialised. Call next() first");

        return items.get(currentIndex);
    }

    @Override
    public boolean next() {
        int newIndex = currentIndex + 1;

        if (newIndex >= items.size())
            return false;

        currentIndex = newIndex;
        return true;
    }

    @Override
    public boolean previous() {
        int newIndex = currentIndex - 1;

        if (newIndex < 0)
            return false;

        currentIndex = newIndex;
        return true;
    }

    @Override
    public int getCurrentIndex() {
        return currentIndex;
    }

    @Override
    public Integer getTotal() {
        return items.size();
    }
}
