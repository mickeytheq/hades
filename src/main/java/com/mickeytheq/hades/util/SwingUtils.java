package com.mickeytheq.hades.util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

public class SwingUtils {
    public static DefaultTableModel createNonEditableModel() {
        return new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public static Color copyColor(Color color) {
        return new Color(color.getRGB(), true);
    }

    // standard way of closing a Window (like a dialog or frame) by firing the closing event
    // this ensures any listeners can intercept and results in a standard close operations
    // note this may not automatically dispose the window so can be used with WindowConstants.DISPOSE_ON_CLOSE
    // to build an WINDOW_CLOSING event -> hide -> dispose chain of events
    public static void closeWindow(Window window) {
        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
    }

    private static final KeyStroke ESCAPE_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    public static final Object CLOSE_DIALOG_ACTION = new Object();

    // installs a keybinding on the Escape key that closes the dialog
    public static void installEscapeCloseKeyboardShortcut(JDialog dialog) {
        Action dispatchClosing = new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                closeWindow(dialog);
            }
        };
        JRootPane root = dialog.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ESCAPE_STROKE, CLOSE_DIALOG_ACTION);
        root.getActionMap().put(CLOSE_DIALOG_ACTION, dispatchClosing);
    }

    // walks a swing component hierarchy, depth first
    public static void walkComponentHierarchy(Component component, Consumer<Component> consumer) {
        consumer.accept(component);

        if (component instanceof Container) {
            Container container = (Container)component;

            for (Component containerComponent : container.getComponents()) {
                walkComponentHierarchy(containerComponent, consumer);
            }
        }
    }
}
