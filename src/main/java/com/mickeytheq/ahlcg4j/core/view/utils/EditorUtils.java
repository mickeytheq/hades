package com.mickeytheq.ahlcg4j.core.view.utils;

import ca.cgjennings.apps.arkham.StrangeEonsAppWindow;
import ca.cgjennings.ui.DocumentEventAdapter;
import com.mickeytheq.ahlcg4j.core.model.common.Statistic;
import com.mickeytheq.ahlcg4j.core.view.component.StatisticComponent;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.function.Consumer;

public class EditorUtils {
    public static final String DEFAULT_NULL_COMBO_BOX_DISPLAY = "(None)"; // TODO: i18n

    public static JTextField createTextField(int columns) {
        JTextField textFieldEditor = new JTextField(columns);
        applyNoEditorOverride(textFieldEditor);
        return textFieldEditor;
    }

    public static JTextArea createTextArea(int rows, int columns) {
        JTextArea textAreaEditor = new JTextArea(rows, columns);
        applyNoEditorOverride(textAreaEditor);
        return textAreaEditor;
    }

    public static <E extends Enum> JComboBox<E> createEnumComboBoxNullable(Class<E> clazz) {
        return createEnumComboBoxNullable(clazz, DEFAULT_NULL_COMBO_BOX_DISPLAY);
    }

    public static <E extends Enum> JComboBox<E> createEnumComboBoxNullable(Class<E> clazz, String nullDisplay) {
        JComboBox<E> comboBox = createNullableComboBox(nullDisplay);

        for (E enumConstant : clazz.getEnumConstants()) {
            comboBox.addItem(enumConstant);
        }

        return comboBox;
    }

    public static <E extends Enum> JComboBox<E> createEnumComboBox(Class<E> clazz) {
        JComboBox<E> comboBox = new JComboBox<>();

        for (E enumConstant : clazz.getEnumConstants()) {
            comboBox.addItem(enumConstant);
        }

        return comboBox;
    }

    public static void applyNoEditorOverride(JComponent component) {
        // the font gets changed to an SE 'default' by AppFrame.installTextEditorFont which affects all JTextComponent derived classes
        // which is called shortly after GameComponent.createDefaultEditor()
        // can be disabled by setting StrangeEonsAppWindow.NO_EDITOR_FONT property on the JTextField by using JComponent.putClientProperty
        component.putClientProperty(StrangeEonsAppWindow.NO_EDITOR_FONT, true);
    }

    // binding utils
    public static void bindTextComponent(JTextComponent textComponent, Consumer<String> consumer) {
        textComponent.getDocument().addDocumentListener(new DocumentEventAdapter() {
            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                consumer.accept(textComponent.getText());
            }
        });
    }

    public static <T> void bindComboBox(JComboBox<T> comboBox, Consumer<T> consumer) {
        comboBox.addItemListener(e -> {
            consumer.accept((T)comboBox.getSelectedItem());
        });
    }

    public static void bindStatisticComponent(StatisticComponent statisticComponent, Consumer<Statistic> consumer) {
        statisticComponent.addActionListener(e -> {
            consumer.accept(statisticComponent.getStatistic());
        });
    }

    public static void bindToggleButton(JToggleButton button, Consumer<Boolean> consumer) {
        button.addActionListener(e -> {
            consumer.accept(button.isSelected());
        });
    }

    public static <E> JComboBox<E> createNullableComboBox() {
        return createNullableComboBox(DEFAULT_NULL_COMBO_BOX_DISPLAY);
    }

    // creates a combo box that inserts a default 'null' value as the first item and
    // displays the given string when that item is selected
    public static <E> JComboBox<E> createNullableComboBox(String nullDisplay) {
        JComboBox<E> comboBox = new JComboBox<>();
        comboBox.addItem(null);

        // it is better to wrap the existing combobox renderer that has all the look and feel elements completed
        // and just tinker with the display text
        comboBox.setRenderer(new NullDisplayRenderer<E>(comboBox.getRenderer(), nullDisplay));

        return comboBox;
    }

    static class NullDisplayRenderer<E> implements ListCellRenderer<E> {
        private final ListCellRenderer<? super E> renderer;
        private final String nullDisplay;

        public NullDisplayRenderer(ListCellRenderer<? super E> renderer, String nullDisplay) {
            this.renderer = renderer;
            this.nullDisplay = nullDisplay;
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
            Component component = renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value == null && component instanceof JLabel) {
                JLabel label = (JLabel)component;
                label.setText(nullDisplay);
            }

            return component;
        }
    }
}
