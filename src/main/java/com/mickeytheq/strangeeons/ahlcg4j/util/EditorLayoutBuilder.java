package com.mickeytheq.strangeeons.ahlcg4j.util;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class EditorLayoutBuilder {
    public static EditorLayoutBuilder create() {
        return new EditorLayoutBuilder();
    }

    private final JPanel mainPanel;

    EditorLayoutBuilder() {
        mainPanel = new JPanel(createLayoutManager());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private LayoutManager createLayoutManager() {
        return new MigLayout();
    }

    public GroupLayoutBuilder createGroupLayoutBuilder(String title) {
        JPanel groupPanel = new JPanel(createLayoutManager());
        groupPanel.setBorder(BorderFactory.createTitledBorder(title));

        return new GroupLayoutBuilder(groupPanel);
    }

    public class GroupLayoutBuilder {
        private final JPanel groupPanel;

        public GroupLayoutBuilder(JPanel groupPanel) {
            this.groupPanel = groupPanel;
        }

        public JPanel getGroupPanel() {
            return groupPanel;
        }

        public GroupLayoutBuilder addLabelledEditor(String labelText, Component editor) {
            if (!labelText.endsWith(": "))
                labelText = labelText + ": ";

            JLabel label = new JLabel(labelText);
            groupPanel.add(label, "aligny center");

            groupPanel.add(editor, "wrap, pushx, growx");

            return this;
        }
    }
}
