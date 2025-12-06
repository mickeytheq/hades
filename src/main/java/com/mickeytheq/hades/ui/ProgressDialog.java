package com.mickeytheq.hades.ui;

import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.util.LoggerUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ProgressDialog {
    private DialogWithButtons dialog;

    private LoggingLevel loggingLevel;

    private JTextArea logTextArea;
    private JButton closeButton;
    private DialogWithButtons dialogWithButtons;

    public ProgressDialog(LoggingLevel loggingLevel) {
        this.loggingLevel = loggingLevel;

        logTextArea = new JTextArea();

        JScrollPane scrollPane = new JScrollPane(logTextArea);
        scrollPane.setPreferredSize(new Dimension(1200, 800));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel panel = MigLayoutUtils.createTitledPanel("Log");
        panel.add(scrollPane, "wrap, pushx, growx, growy, pushy");

        dialogWithButtons = new DialogWithButtons((Frame)null, false);
        dialogWithButtons.setContent(panel);
        dialogWithButtons.setTitle("Progress");

        closeButton = dialogWithButtons.addDialogClosingButton("Close", 0, () -> Boolean.TRUE);
        closeButton.setEnabled(false);
    }

    public <T> T runWithinProgressDialog(Callable<T> callable) {
        AtomicReference<T> reference = new AtomicReference<>();

        // when a progress dialog is shown begin execution
        dialogWithButtons.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                T result = execute(callable);
                reference.set(result);
            }
        });

        // install a new logging handler
        Handler logHandler = new Handler() {
            @Override
            public void publish(LogRecord record) {
                // only log things coming from our classes
                if (!record.getLoggerName().startsWith("com.mickeytheq"))
                    return;

                boolean shouldLog;
                switch (loggingLevel) {
                    case Debug:
                        shouldLog = true;
                        break;
                    case Verbose:
                        shouldLog = record.getLevel().intValue() >= Level.FINE.intValue();
                        break;
                    case Normal:
                        shouldLog = record.getLevel().intValue() >= Level.INFO.intValue();
                        break;
                    default:
                        shouldLog = true;
                        break;
                }

                // log everything to the progress dialog if verbose has been selected, otherwise only log info level or higher
                if (shouldLog)
                    addLine(record.getMessage());
            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {

            }
        };

        // route logging messages to the progress dialog
        Logger.getLogger("").addHandler(logHandler);
        try {
            // show the dialog which will make the componentShown event above trigger and start the work
            // we want done within the dialog
            dialogWithButtons.showDialog();
        } finally {
            Logger.getLogger("").removeHandler(logHandler);
        }

        // return the result
        return reference.get();
    }

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private <T> T execute(Callable<T> callable) {
        // invoke the work in a separate thread
        // we may be on the Swing UI thread. we want to fork off from that to allow
        // UI processing to continue
        Future<T> future = executorService.submit(callable);
        try {
            return future.get();
        } catch (InterruptedException e) {
            addLine(LoggerUtils.toLoggable("Interrupted exception", e.getCause()));
            return null;
        } catch (ExecutionException e) {
            addLine(LoggerUtils.toLoggable("Unexpected/uncaught exception", e.getCause()));
            return null;
        }
        finally {
            // always call complete to enable the progress dialog to be closed
            completed();
        }
    }

    private void completed() {
        addLine("Process completed. Click close button when ready");

        closeButton.setEnabled(true);
    }

    public void addLine(String line) {
        SwingUtilities.invokeLater(() -> logTextArea.append(line + System.lineSeparator()));
    }
}
