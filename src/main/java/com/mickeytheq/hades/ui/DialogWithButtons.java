package com.mickeytheq.hades.ui;

import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class DialogWithButtons extends JDialog {
    public static final int OK_OPTION = 0;
    public static final int CANCEL_OPTION = 1;

    private final boolean trackDialogSizeToContent;
    private Component content;

    private final List<JButton> buttons = new ArrayList<>();

    private int dialogResultCode = -1;

    public DialogWithButtons(Dialog parent, boolean trackDialogSizeToContent) {
        super(parent, true);

        this.trackDialogSizeToContent = trackDialogSizeToContent;

        initialise();
    }

    public DialogWithButtons(Frame parent, boolean trackDialogSizeToContent) {
        super(parent, true);

        this.trackDialogSizeToContent = trackDialogSizeToContent;

        initialise();
    }

    private void initialise() {
        if (trackDialogSizeToContent)
            setResizable(false);

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void setContent(Component content) {
        this.content = content;
    }

    public void addOkCancelButtons() {
        addDialogClosingButton("OK", OK_OPTION, () -> Boolean.TRUE);
        addDialogClosingButton("Cancel", CANCEL_OPTION, () -> Boolean.TRUE);
    }

    public JButton addDialogClosingButton(String buttonText, int buttonResultCode, Supplier<Boolean> validateSupplier) {
        JButton button = new JButton(buttonText);
        button.addActionListener(e -> {
            Boolean validated = validateSupplier.get();

            if (!validated)
                return;

            dialogResultCode = buttonResultCode;

            setVisible(false);
        });

        addButton(button);
        return button;
    }

    public JButton addButton(String buttonText, ActionListener actionListener) {
        JButton button = new JButton(buttonText);
        button.addActionListener(actionListener);
        addButton(button);

        return button;
    }

    public void addButton(JButton button) {
        buttons.add(button);
    }

    public int showDialog() {
        JPanel mainPanel = new JPanel(MigLayoutUtils.createDialogMigLayout());
        mainPanel.add(content, "wrap, growx, growy, pushx, pushy");

        JPanel buttonPanel = MigLayoutUtils.createOrganiserPanel();

        for (JButton button : buttons) {
            buttonPanel.add(button, "sizegroup button");
        }

        // add a gap on the left to take up all the room the buttons don't want
        mainPanel.add(buttonPanel, "gapleft push");

        getContentPane().add(mainPanel);

        if (trackDialogSizeToContent) {
            content.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    pack();
                }
            });
        }

        pack();

        setVisible(true);
        dispose();

        return dialogResultCode;
    }
}
