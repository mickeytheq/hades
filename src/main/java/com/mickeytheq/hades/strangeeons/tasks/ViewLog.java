package com.mickeytheq.hades.strangeeons.tasks;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.project.Member;
import com.mickeytheq.hades.core.view.utils.EditorUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.ui.DialogWithButtons;
import com.mickeytheq.hades.ui.LoggingLevel;
import com.mickeytheq.hades.util.log4j.GlobalMemoryAppender;
import com.mickeytheq.hades.util.log4j.Log4JUtils;

import javax.swing.*;
import java.awt.*;

public class ViewLog extends BaseTaskAction {
    @Override
    public String getLabel() {
        return "View Hades log";
    }

    @Override
    public boolean appliesToSelection(Member[] members) {
        return true;
    }

    @Override
    public boolean performOnSelection(Member[] members) {
        viewLog();

        return true;
    }

    public static void viewLog() {
        new LogView().showDialog();
    }

    static class LogView extends DialogWithButtons {
        public LogView() {
            super(StrangeEons.getWindow(), false);

            setTitle("Hades log");
            setModal(false);

            JTextArea logArea = new JTextArea();
            logArea.setLineWrap(true);
            logArea.setWrapStyleWord(true);
            logArea.setEditable(false);

            String logText = GlobalMemoryAppender.getGlobalLog();
            logArea.setText(logText);

            JScrollPane scrollPane = new JScrollPane(logArea);

            JComboBox<LoggingLevel> loggingEditor = EditorUtils.createEnumComboBox(LoggingLevel.class);
            loggingEditor.setSelectedItem(LoggingLevel.fromLog4JLevel(Log4JUtils.getHadesGlobalLoggerLevel()));
            loggingEditor.addItemListener(e -> {
                LoggingLevel loggingLevel = (LoggingLevel) loggingEditor.getSelectedItem();
                Log4JUtils.setHadesGlobalLoggerLevel(loggingLevel.getLog4JLevel());
            });

            JPanel optionsPanel = MigLayoutUtils.createTitledPanel("Options");
            MigLayoutUtils.addLabel(optionsPanel, "Global log level");
            optionsPanel.add(loggingEditor, "gap right push, wrap");

            JPanel logTextPanel = MigLayoutUtils.createTitledPanel("Log");
            logTextPanel.add(scrollPane, "grow, push");

            scrollPane.setPreferredSize(new Dimension(1600, 1100));

            JPanel mainPanel = MigLayoutUtils.createOrganiserPanel();
            mainPanel.add(optionsPanel, "growx, pushx, wrap");
            mainPanel.add(logTextPanel, "grow, push");

            setContentComponent(mainPanel);

            addDialogClosingButton("Close", 0, () -> true);
        }
    }
}
