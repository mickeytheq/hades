package com.mickeytheq.ahlcg4j.core.view.utils;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class MigLayoutUtils {
    public static void assertMigLayout(Container container) {
        if (!(container.getLayout() instanceof MigLayout))
            throw new RuntimeException("Container '" + container.getName() + "' is required to have MigLayout layout manager but does not");
    }

    public static LC createDefaultLayoutContraints() {
        LC lc = new LC();
        return lc;
    }

    public static MigLayout createDefaultMigLayout() {
        return new MigLayout(createDefaultLayoutContraints());
    }

    // creates a panel that has a titled border and standard insets/gaps within the panel
    public static JPanel createTitledPanel(String title) {
        Objects.requireNonNull(title);

        JPanel panel = new JPanel(createDefaultMigLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));

        return panel;
    }

    // creates a panel that has no visual spacing/border to be used as a container/organiser for other elements
    public static JPanel createEmbeddedPanel() {
        JPanel panel = new JPanel(new MigLayout(createDefaultLayoutContraints().insets("0")));
        return panel;
    }

    // adds a label followed by a component and wrap to the next row
    public static void addLabelledComponentWrapGrowPush(JPanel panel, String labelText, Component component) {
        assertMigLayout(panel);

        addLabel(panel, labelText);

        panel.add(component, "wrap, growx, pushx");
    }

    public static void addLabelledComponent(JPanel panel, String labelText, Component component, String constraints) {
        assertMigLayout(panel);

        addLabel(panel, labelText);

        panel.add(component, constraints);
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
