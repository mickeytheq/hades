package com.mickeytheq.hades.ui;

import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

// a component that provides a selection option using a JSelector, records what is selected and what is unselected of the
// provided values
// selected values are presented as JTags next to the selector
public class JTagSelector<E> extends JPanel {
    private final JSelector<E> selector;
    private final List<JTag> tags = new ArrayList<>();
    private final Map<E, JTag> tagMap = new HashMap<>();

    private final List<E> allValues;
    private final List<E> selectedValues = new ArrayList<>();

    private BufferedImage tagCloseIcon;
    private BufferedImage tagCloseHoverIcon;

    public JTagSelector(List<E> allValues, BiPredicate<E, String> textMatcher) {
        this.allValues = new ArrayList<>(allValues);

        setLayout(MigLayoutUtils.createOrganiserLayout());

        selector = new JSelector<>(s -> getUnselectedValues().stream().filter(o -> textMatcher.test(o, s)).collect(Collectors.toList()));
        selector.addSelectionListener(this::select);

        relayout();
    }

    public BufferedImage getTagCloseIcon() {
        return tagCloseIcon;
    }

    public void setTagCloseIcon(BufferedImage tagCloseIcon) {
        this.tagCloseIcon = tagCloseIcon;
    }

    public BufferedImage getTagCloseHoverIcon() {
        return tagCloseHoverIcon;
    }

    public void setTagCloseHoverIcon(BufferedImage tagCloseHoverIcon) {
        this.tagCloseHoverIcon = tagCloseHoverIcon;
    }

    public List<E> getSelectedValues() {
        return new ArrayList<>(selectedValues);
    }

    public void setSelectedValues(List<E> selectedValues) {
        this.selectedValues.clear();
        this.selectedValues.addAll(new ArrayList<>(selectedValues));

        relayout();
    }

    private List<E> getUnselectedValues() {
        return allValues.stream().filter(o -> !selectedValues.contains(o)).collect(Collectors.toList());
    }

    private void select(E item) {
        if (selectedValues.contains(item))
            return;

        selectedValues.add(item);

        JTag tag = new JTag(item.toString(), tagCloseIcon, tagCloseHoverIcon);
        tag.addActionListener(e -> {
            unselect(item);
        });
        tags.add(tag);
        tagMap.put(item, tag);

        for (JTagSelectorListener<E> listener : listeners) {
            listener.tagSelected(item);
        }

        relayout();
    }

    private void unselect(E item) {
        selectedValues.remove(item);

        JTag tag = tagMap.remove(item);

        if (tag != null) {
            tags.remove(tag);
        }

        for (JTagSelectorListener<E> listener : listeners) {
            listener.tagUnselected(item);
        }

        relayout();
    }

    private void relayout() {
        removeAll();

        for (JTag tag : tags) {
            add(tag, "width pref!");
        }

        add(selector, "pushx, growx");

        revalidate();
    }

    private final List<JTagSelectorListener<E>> listeners = new ArrayList<>();

    public void addListener(JTagSelectorListener<E> listener) {
        listeners.add(listener);
    }

    public void removeListener(JTagSelectorListener<E> listener) {
        listeners.remove(listener);
    }

    public interface JTagSelectorListener<E> {
        void tagSelected(E value);

        void tagUnselected(E value);
    }
}
