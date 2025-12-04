package com.mickeytheq.ahlcg4j.ui;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class DialogWithButtons extends JDialog {
    private final JPanel contentPanel;
    private final boolean trackDialogSizeToContent;

    private final List<JButton> buttons = new ArrayList<>();

    private int dialogResultCode = -1;

    public DialogWithButtons(JPanel contentPanel, boolean trackDialogSizeToContent) {
        super((Frame) null, true);

        this.contentPanel = contentPanel;
        this.trackDialogSizeToContent = trackDialogSizeToContent;

        if (trackDialogSizeToContent)
            setResizable(false);

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void addDialogClosingButton(String buttonText, int buttonResultCode, Supplier<Boolean> validateSupplier) {
        JButton button = new JButton(buttonText);
        button.addActionListener(e -> {
            Boolean validated = validateSupplier.get();

            if (!validated)
                return;

            dialogResultCode = buttonResultCode;

            setVisible(false);
        });

        addButton(button);
    }

    public void addButton(String buttonText, ActionListener actionListener) {
        JButton button = new JButton(buttonText);
        button.addActionListener(actionListener);
        addButton(button);
    }

    public void addButton(JButton button) {
        buttons.add(button);
    }

    public int showDialog() {
        JPanel mainPanel = new JPanel(new MigLayout());
        mainPanel.add(contentPanel, "wrap");

        JPanel buttonPanel = new JPanel(new MigLayout(new LC().insets("0")));

        for (JButton button : buttons) {
            buttonPanel.add(button, "sizegroup button");
        }

        mainPanel.add(buttonPanel, "gapleft push");

        getContentPane().add(mainPanel);

        if (trackDialogSizeToContent) {
            contentPanel.addComponentListener(new ComponentAdapter() {
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
