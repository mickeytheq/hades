package com.mickeytheq.hades.ui;

import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.util.LoggerUtils;
import com.mickeytheq.hades.util.log4j.Log4JUtils;
import com.mickeytheq.hades.util.log4j.MemoryAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class ProgressDialog {
    private LoggingLevel loggingLevel;

    private JTextArea logTextArea;
    private JButton closeButton;
    private DialogEx dialogEx;

    public ProgressDialog(LoggingLevel loggingLevel) {
        this.loggingLevel = loggingLevel;

        logTextArea = new JTextArea();

        JScrollPane scrollPane = new JScrollPane(logTextArea);
        scrollPane.setPreferredSize(new Dimension(1200, 800));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel panel = MigLayoutUtils.createTitledPanel("Log");
        panel.add(scrollPane, "wrap, pushx, growx, growy, pushy");

        dialogEx = new DialogEx((Frame)null, false);
        dialogEx.setContentComponent(panel);
        dialogEx.setTitle("Progress");

        closeButton = dialogEx.addCloseButton();
        closeButton.setEnabled(false);
    }

    public <T> T runWithinProgressDialog(Callable<T> callable) {
        AtomicReference<T> reference = new AtomicReference<>();

        // when a progress dialog is shown begin execution
        dialogEx.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                execute(callable, reference::set);
            }
        });

        // route logging messages to the progress dialog
        Log4JUtils.withAppender(
                new MemoryAppender("ProgressDialog", null, PatternLayout.createDefaultLayout(), true, null, this::addText),
                loggingLevel.getLog4JLevel(),
                null,
                () -> {
                    // show the dialog which will make the componentShown event above trigger and start the work
                    // we want done within the dialog - this will block here until the dialog is closed
                    dialogEx.showDialog();
                });

        // return the result
        return reference.get();
    }

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private <T> void execute(Callable<T> callable, Consumer<T> resultConsumer) {
        // invoke the work in a separate thread
        // we may be on the Swing UI thread. we want to fork off from that to allow
        // UI processing to continue
        executorService.submit(() -> {
            try {
                T result = callable.call();
                resultConsumer.accept(result);
            } catch (Exception e) {
                addLine(LoggerUtils.toLoggable("Unexpected/uncaught exception", e.getCause()));
            }
            finally {
                completed();
            }
        });
        executorService.shutdown();
    }

    private void completed() {
        addLine("Process completed. Click close button when ready");

        SwingUtilities.invokeLater(() -> {
            closeButton.setEnabled(true);
        });
    }

    public void addText(String text) {
        SwingUtilities.invokeLater(() -> logTextArea.append(text));
    }

    public void addLine(String line) {
        addText(line + System.lineSeparator());
    }
}
