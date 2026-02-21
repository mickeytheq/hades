package com.mickeytheq.hades.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileChooser extends JPanel {
    private final JTextField textField;

    private final JFileChooser fileChooser;

    public static FileChooser create(int fileSelectionMode) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getFileChooser().setFileSelectionMode(fileSelectionMode);
        return fileChooser;
    }

    public FileChooser() {
        fileChooser = new JFileChooser();
        fileChooser.setFileHidingEnabled(false);
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

    public Path getSelectedPath() {
        File file = fileChooser.getSelectedFile();

        if (file == null)
            return null;

        return file.toPath();
    }

    public void setSelectedPathAndSensibleCurrentDirectory(Path path) {
        setSelectedPath(path);
        setCurrentDirectory(getSensibleDirectory(path));
    }

    public void setSelectedPath(Path path) {
        // if the path is relative, possibly an empty string treat that as an unknown path
        if (path == null || !path.isAbsolute()) {
            textField.setText("");
        } else {
            textField.setText(path.toAbsolutePath().toString());
            fileChooser.setSelectedFile(path.toFile());
        }
    }

    public void setCurrentDirectory(Path path) {
        if (path == null || !path.isAbsolute())
            fileChooser.setCurrentDirectory(null);
        else
            fileChooser.setCurrentDirectory(path.toFile());
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

    private static Path getSensibleDirectory(Path currentPath) {
        // first if the path exists and is a diectory
        if (isDirectoryPathValid(currentPath))
            return currentPath;

        // try the immediate parent
        if (currentPath != null) {
            currentPath = currentPath.getParent();

            if (isDirectoryPathValid(currentPath))
                return currentPath;
        }

        // otherwise null
        return null;
    }

    private static boolean isDirectoryPathValid(Path path) {
        if (path == null)
            return false;

        if (!Files.isDirectory(path))
            return false;

        return true;
    }
}
