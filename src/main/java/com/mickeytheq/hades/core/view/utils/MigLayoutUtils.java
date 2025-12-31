package com.mickeytheq.hades.core.view.utils;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class MigLayoutUtils {
    private static final boolean DEBUG = false;

    // fix the width of spacing editors
    public static final String SPACING_EDITOR_CONSTRAINTS = "wrap, width 50:50:50";

    public static void assertMigLayout(Container container) {
        if (!(container.getLayout() instanceof MigLayout))
            throw new RuntimeException("Container '" + container.getName() + "' is required to have MigLayout layout manager but does not");
    }

    public static LC createDefaultLayoutConstraints() {
        LC lc = new LC();

        if (DEBUG)
            lc.debug(500);

        return lc;
    }

    // create a layout suitable for use with a top level panel/container inside a dialog
    public static MigLayout createTopLevelLayout() {
        return new MigLayout(createDefaultLayoutConstraints().insets("dialog"));
    }

    public static JPanel createDialogPanel() {
        return new JPanel(createTopLevelLayout());
    }

    // creates a panel that has a titled border and standard insets/gaps within the panel
    public static JPanel createTitledPanel(String title) {
        Objects.requireNonNull(title);

        JPanel panel = new JPanel(new MigLayout(createDefaultLayoutConstraints()));
        panel.setBorder(BorderFactory.createTitledBorder(title));

        return panel;
    }

    public static MigLayout createOrganiserLayout() {
        return new MigLayout(createDefaultLayoutConstraints().insets("0"));
    }

    // creates a panel that has no visual spacing border/insets to be used as a container/organiser for other elements
    public static JPanel createOrganiserPanel() {
        return new JPanel(createOrganiserLayout());
    }

    // creates a populated panel from the list of components where each components are placed in a
    // vertical flow layout (top to bottom) with growing/pushing on the X axis
    // typically used for a 'main panel' to layout multiple sub-panels
    public static JPanel createVerticalFlowOrganiserPanel(Component ... components) {
        JPanel panel = createOrganiserPanel();

        for (Component component : components) {
            panel.add(component, "pushx, growx, wrap");
        }

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
