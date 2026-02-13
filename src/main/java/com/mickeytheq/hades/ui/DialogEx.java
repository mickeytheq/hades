package com.mickeytheq.hades.ui;

import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.ui.validation.Validators;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * an enhanced dialog that supports the following:
 *
 * validation of individual controls in conjunction with {@link com.mickeytheq.hades.ui.validation.Validators} which will display
 * error icons against fields that fail validation
 *
 * TODO: persistence
 *
 * control of the buttons that are present on the dialog, some of which may close the dialog and trigger additional dialog-wide validation
 * standardisation of button positioning and sizing
 */
public class DialogEx extends JDialog {
    // some standard codes to attach to common buttons
    public static final int OK_OPTION = 0;
    public static final int CANCEL_OPTION = 1;
    public static final int CLOSE_OPTION = 2;

    private final boolean trackDialogSizeToContent;
    private Component content;

    private final List<JButton> dialogLifecycleButtons = new ArrayList<>();
    private JButton helpButton = null;
    private final List<JButton> otherButtons = new ArrayList<>();

    private int dialogResultCode = -1;

    public DialogEx(Frame frame, boolean trackDialogSizeToContent) {
        super(frame, true);

        this.trackDialogSizeToContent = trackDialogSizeToContent;

        initialise();
    }

    private void initialise() {
        if (trackDialogSizeToContent)
            setResizable(false);

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void setContentComponent(Component content) {
        this.content = content;
    }

    public void addOkCancelButtons() {
        addOkCancelButtons(() -> Boolean.TRUE);
    }

    public void addOkCancelButtons(Supplier<Boolean> okValidator) {
        addOkCancelButtons("OK", "Cancel", okValidator);
    }

    public void addOkCancelButtons(String okText, String cancelText, Supplier<Boolean> okValidator) {
        addDialogClosingButtonWithValidation(okText, OK_OPTION, okValidator);
        addDialogClosingButton(cancelText, CANCEL_OPTION);
    }

    public JButton addCloseButton() {
        return addCloseButton("Close");
    }

    public JButton addCloseButton(String closeText) {
        return addDialogClosingButton(closeText, CLOSE_OPTION);
    }

    /**
     * adds an ok/confirm button that will perform no validation and simply close the dialog.
     * {@link DialogEx#showDialog()} method will return the given result code
     */
    public JButton addDialogClosingButton(String buttonText, int buttonResultCode) {
        JButton button = new JButton(buttonText);
        button.addActionListener(e -> {
            dialogResultCode = buttonResultCode;
            setVisible(false);
            dispose();
        });

        dialogLifecycleButtons.add(button);
        return button;
    }

    /**
     * adds an ok/confirm button that will perform field validation created by {@link com.mickeytheq.hades.ui.validation.Validators},
     * then perform validation as specified by the parameter
     * if all validation is passed the dialog will be close and the {@link DialogEx#showDialog()} method will return the given result code
     */
    public JButton addDialogClosingButtonWithValidation(String buttonText, int buttonResultCode, Supplier<Boolean> validateSupplier) {
        JButton button = new JButton(buttonText);
        button.addActionListener(e -> {
            // execute the validation on any components
            // if any failures do not close the dialog
            if (!Validators.validateComponentHierarchy(this))
                return;

            Boolean validated = validateSupplier.get();

            if (!validated)
                return;

            dialogResultCode = buttonResultCode;

            setVisible(false);
            dispose();
        });

        dialogLifecycleButtons.add(button);
        return button;
    }

    public JButton addHelpButton(String buttonText, ActionListener actionListener) {
        helpButton = new JButton(buttonText);
        helpButton.addActionListener(actionListener);
        return helpButton;
    }

    public JButton addOtherButton(String buttonText, ActionListener actionListener) {
        JButton button = new JButton(buttonText);
        button.addActionListener(actionListener);

        otherButtons.add(button);

        return button;
    }

    public int showDialog() {
        JPanel mainPanel = new JPanel(MigLayoutUtils.createTopLevelLayout());
        mainPanel.add(content, "wrap, growx, growy, pushx, pushy");

        JPanel buttonPanel = MigLayoutUtils.createOrganiserPanel();

        // first the help button
        if (helpButton != null)
            buttonPanel.add(helpButton, "sizegroup button");

        // then the 'other' buttons
        for (JButton button : otherButtons) {
            buttonPanel.add(button, "sizegroup button");
        }

        // then any lifecycle buttons like ok/cancel/close/continue
        for (JButton button : dialogLifecycleButtons) {
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

        Container parent = getParent();

        if (parent != null)
            setLocationRelativeTo(parent);

        setVisible(true);

        return dialogResultCode;
    }
}
