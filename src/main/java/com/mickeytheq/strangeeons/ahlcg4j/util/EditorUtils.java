package com.mickeytheq.strangeeons.ahlcg4j.util;

import ca.cgjennings.apps.arkham.StrangeEonsAppWindow;
import ca.cgjennings.ui.DocumentEventAdapter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.function.Consumer;

public class EditorUtils {
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
}
