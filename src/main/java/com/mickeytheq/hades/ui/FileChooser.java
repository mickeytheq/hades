package com.mickeytheq.hades.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileChooser extends JPanel {
    private final JTextField textField;

    private final JFileChooser fileChooser;

    public FileChooser() {
        fileChooser = new JFileChooser();
        fileChooser.setPreferredSize(new Dimension(800,600));

        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        // creates the GUI
        textField = new JTextField(30);
        JButton button = new JButton("...");
        button.setPreferredSize(new Dimension(30, 20));

        button.addActionListener(this::buttonActionPerformed);

        add(textField);
        add(button);

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                File textFieldFile = new File(textField.getText());

                if (Objects.equals(fileChooser.getSelectedFile(), textFieldFile))
                    return;

                fileChooser.setSelectedFile(new File(textField.getText()));

                fireActionListeners("selected-file-changed");
            }
        });
    }

    private void buttonActionPerformed(ActionEvent evt) {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            textField.setText(fileChooser.getSelectedFile().getAbsolutePath());

            fireActionListeners("selected-file-changed");
        }
    }

    public File getSelectedFile() {
        return fileChooser.getSelectedFile();
    }

    public Path getSelectedPath() {
        return getSelectedFile().toPath();
    }

    public void setSelectedFile(File file) {
        if (file == null) {
            textField.setText("");
        } else {
            textField.setText(file.getAbsolutePath());
        }

        fileChooser.setSelectedFile(file);
    }

    public String getSelectedFilePath() {
        return textField.getText();
    }

    public JTextField getTextField() {
        return textField;
    }

    public JFileChooser getFileChooser() {
        return fileChooser;
    }

    private final List<ActionListener> actionListeners = new ArrayList<>();

    public void addActionListener(ActionListener actionListener) {
        actionListeners.add(actionListener);
    }

    private void fireActionListeners(String command) {
        ActionEvent actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);

        for (ActionListener actionListener : actionListeners) {
            actionListener.actionPerformed(actionEvent);
        }
    }
}
