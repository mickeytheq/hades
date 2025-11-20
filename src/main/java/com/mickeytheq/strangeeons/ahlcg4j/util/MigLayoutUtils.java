package com.mickeytheq.strangeeons.ahlcg4j.util;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class MigLayoutUtils {
    public static void assertMigLayout(Container container) {
        if (!(container.getLayout() instanceof MigLayout))
            throw new RuntimeException("Container '" + container.getName() + "' is required to have MigLayout layout manager but does not");
    }

    public static JPanel createPanel() {
        return new JPanel(new MigLayout());
    }

    public static JPanel createPanel(String title) {
        JPanel panel = createPanel();
        panel.setBorder(BorderFactory.createTitledBorder(title));
        return panel;
    }

    public static void addLabelledComponentRow(JPanel panel, String label, Component component) {
        assertMigLayout(panel);

        panel.add(new JLabel(label));
        panel.add(component, "wrap, growx, pushx");
    }
}
