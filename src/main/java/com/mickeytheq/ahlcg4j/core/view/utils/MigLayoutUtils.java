package com.mickeytheq.ahlcg4j.core.view.utils;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;

public class MigLayoutUtils {
    public static void assertMigLayout(Container container) {
        if (!(container.getLayout() instanceof MigLayout))
            throw new RuntimeException("Container '" + container.getName() + "' is required to have MigLayout layout manager but does not");
    }

    public static JPanel createPanel(LC layoutConstraints) {
        return createPanel(layoutConstraints, null);
    }

    public static JPanel createPanel(LC layoutConstraints, @Nullable String title) {
        JPanel panel = new JPanel(new MigLayout(layoutConstraints));

        if (!StringUtils.isEmpty(title))
            panel.setBorder(BorderFactory.createTitledBorder(title));

        return panel;
    }

    public static JPanel createPanel() {
        return createPanel(null, null);
    }

    public static JPanel createPanel(String title) {
        return createPanel(null, title);
    }

    // adds a label followed by a component and wrap to the next row
    public static void addLabelledComponentWrap(JPanel panel, String labelText, Component component) {
        assertMigLayout(panel);

        addLabel(panel, labelText);

        panel.add(component, "wrap, growx, pushx");
    }

    public static void addLabelledComponent(JPanel panel, String labelText, Component component) {
        assertMigLayout(panel);

        addLabel(panel, labelText);

        panel.add(component, "growx, pushx");
    }

    public static void addLabel(JPanel panel, String labelText) {
        addLabel(panel, labelText, null);
    }

    public static void addLabel(JPanel panel, String labelText, String additionalConstraints) {
        assertMigLayout(panel);

        // labels should always end with a ": " to lead into the editor component
        if (!labelText.endsWith(": ")) {
            if (labelText.endsWith(":"))
                labelText = labelText + " ";
            else
                labelText = labelText.trim() + ": ";
        }

        panel.add(new JLabel(labelText), additionalConstraints);
    }

    public static void addComponentGrowXPushX(JPanel panel, Component component) {
        addComponentGrowXPushX(panel, component, null);
    }

    public static void addComponentGrowXPushX(JPanel panel, Component component, String additionalConstraints) {
        assertMigLayout(panel);

        String constraints = "growx, pushx";

        if (!StringUtils.isEmpty(additionalConstraints))
            constraints = constraints + ", " + additionalConstraints;

        panel.add(component, constraints);
    }
}
