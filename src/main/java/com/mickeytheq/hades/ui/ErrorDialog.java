package com.mickeytheq.hades.ui;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.strangeeons.tasks.ViewLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ErrorDialog extends JDialog {
    private static final Logger logger = LogManager.getLogger(ErrorDialog.class);

    private static ErrorDialog CURRENT_INSTANCE;

    private final List<ErrorInfo> errorInfos = new ArrayList<>();
    private final JLabel display;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setDaemon(true).build());
    private volatile Future<?> fadeFuture;

    public static void error(String message, Throwable throwable) {
        logger.error(message, throwable);

        // may or may not be invoked from the UI thread
        SwingUtilities.invokeLater(() -> {
            ErrorDialog currentDialog = getDialog();
            currentDialog.addError(new ErrorInfo(message, throwable));
        });
    }

    private static ErrorDialog getDialog() {
        if (CURRENT_INSTANCE != null && CURRENT_INSTANCE.isVisible()) {
            return CURRENT_INSTANCE;
        }

        CURRENT_INSTANCE = new ErrorDialog();

        return CURRENT_INSTANCE;
    }

    public ErrorDialog() {
        super(Environment.getTopLevelWindow());
        setModal(false);
        setUndecorated(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);

        JPanel panel = MigLayoutUtils.createDialogPanel();
        panel.setBorder(BorderFactory.createLineBorder(new Color(196, 92, 92), 5));

        display = new JLabel();

        Font font = display.getFont().deriveFont(16);

        display.setFont(font);

        JLabel genericIntro = new JLabel("Click here to view log", SwingConstants.CENTER);
        genericIntro.setFont(font);

        panel.add(genericIntro, "wrap, pushx, growx");
        panel.add(display, "width 200!, height 80!");

        getContentPane().add(panel);

        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onMouseClicked(e);
            }
        };

        panel.addMouseListener(mouseListener);

        pack();

        setVisible(true);
    }

    @Override
    public void dispose() {
        super.dispose();

        executorService.shutdown();
    }

    public void addError(ErrorInfo errorInfo) {
        errorInfos.add(errorInfo);

        updateDisplay();
    }

    private void onMouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() != MouseEvent.BUTTON1)
            return;

        ViewLog.viewLog();
    }

    private void updateDisplay() {
        if (errorInfos.isEmpty()) {
            setVisible(false);
            return;
        }

        if (errorInfos.size() == 1) {
            ErrorInfo errorInfo = errorInfos.get(0);
            display.setText(String.format(errorInfo.getMessage()));
        }
        else {
            display.setText(errorInfos.size() + " errors have occurred");
        }

        Window topLevelWindow = Environment.getTopLevelWindow();

        if (topLevelWindow != null) {
            setLocation((int) (topLevelWindow.getBounds().getMaxX() - getWidth() - 15), (int) (topLevelWindow.getBounds().getMaxY() - getHeight() - 15));
        }

        resetFade();
    }

    private static final float FADE_STEP = 0.05f;
    private static final float TOTAL_FADE_TIME_MILLIS = 2000;
    private static final int FADE_STEP_TIME_MILLIS = (int) (TOTAL_FADE_TIME_MILLIS / (1 / FADE_STEP));

    private static final int DELAY_BEFORE_FADE_MILLIS = 5000;

    private void resetFade() {
        if (fadeFuture != null)
            fadeFuture.cancel(false);

        fadeFuture = executorService.scheduleWithFixedDelay(this::fade, DELAY_BEFORE_FADE_MILLIS, FADE_STEP_TIME_MILLIS, TimeUnit.MILLISECONDS);
    }

    private void fade() {
        float currentOpacity = getOpacity();

        // if opacity is at zero cancel the future and hide the dialog
        if (currentOpacity <= 0) {
            fadeFuture.cancel(false);
            setVisible(false);
            return;
        }

        // reduce the opacity guarding against less than zero
        setOpacity(Math.max(0, currentOpacity - FADE_STEP));
    }

    public static class ErrorInfo {
        private final String message;
        private final Throwable throwable;

        public ErrorInfo(String message, Throwable throwable) {
            this.message = message;
            this.throwable = throwable;
        }

        public String getMessage() {
            return message;
        }

        public Throwable getThrowable() {
            return throwable;
        }

        @Override
        public String toString() {
            return message;
        }
    }
}
