package com.mickeytheq.hades.core.view.utils;

import com.mickeytheq.hades.core.view.component.DimensionExComponent;
import com.mickeytheq.hades.util.shape.DimensionEx;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * utility class for creating bindings between swing components and java objects/fields/methods
  */
public class Binder {
    private final List<Runnable> callOnAnyChange = new ArrayList<>();

    private static final Consumer<JComponent> MARK_CHANGED = EditorUtils::markChanged;

    public static Binder create() {
        return new Binder();
    }

    public Binder onAnyChange(Runnable callOnAnyChange) {
        this.callOnAnyChange.add(callOnAnyChange);
        return this;
    }

    public Binder textComponent(JTextComponent editor, Consumer<String> consumer) {
        EditorUtils.bindTextComponent(editor, wrapEditorChanged(editor, consumer));
        return this;
    }

    public <T> Binder comboBox(JComboBox<T> editor, Consumer<T> consumer) {
        EditorUtils.bindComboBox(editor, wrapEditorChanged(editor, consumer));
        return this;
    }

    public Binder spinner(JSpinner editor, Consumer<Integer> consumer) {
        EditorUtils.bindSpinner(editor, wrapEditorChanged(editor, consumer));
        return this;
    }

    public Binder spinnerDouble(JSpinner editor, Consumer<Double> consumer) {
        EditorUtils.bindSpinnerDouble(editor, wrapEditorChanged(editor, consumer));
        return this;
    }

    public Binder toggleButton(JToggleButton editor, Consumer<Boolean> consumer) {
        EditorUtils.bindToggleButton(editor, wrapEditorChanged(editor, consumer));
        return this;
    }

    public Binder dimension(DimensionExComponent editor, Consumer<DimensionEx> consumer) {
        EditorUtils.bindDimensionExComponent(editor, wrapEditorChanged(editor, consumer));
        return this;
    }

    private <T> Consumer<T> wrapEditorChanged(JComponent component, Consumer<T> consumer) {
        Consumer<T> wrapped = wrapWithMarkChanged(component, consumer);

        if (callOnAnyChange.isEmpty())
            return wrapped;

        return t -> {
            wrapped.accept(t);

            for (Runnable runnable : callOnAnyChange) {
                runnable.run();
            }
        };
    }

    private <T> Consumer<T> wrapWithMarkChanged(JComponent component, Consumer<T> consumer) {
        return t -> {
            consumer.accept(t);
            MARK_CHANGED.accept(component);
        };
    }
}
