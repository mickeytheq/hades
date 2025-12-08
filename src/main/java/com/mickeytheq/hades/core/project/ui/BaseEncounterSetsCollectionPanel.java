package com.mickeytheq.hades.core.project.ui;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.dialog.ErrorDialog;
import com.mickeytheq.hades.core.project.KeyedImageInfo;
import com.mickeytheq.hades.core.view.utils.EditorUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.ui.DialogWithButtons;
import com.mickeytheq.hades.ui.EntityTablePanel;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public abstract class BaseEncounterSetsCollectionPanel<T extends KeyedImageInfo> extends EntityTablePanel<T> {
    @Override
    protected void configureTableModel(DefaultTableModel tableModel) {
        tableModel.addColumn("Key");
        tableModel.addColumn("Name");
    }

    @Override
    protected void configureTable(JTable table) {
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
    }

    protected abstract List<T> getEntityList();

    @Override
    protected void populateTable(DefaultTableModel tableModel) {
        for (KeyedImageInfo keyedImageInfo : getEntityList()) {
            tableModel.addRow(new Object[]{keyedImageInfo, keyedImageInfo.getKey(), keyedImageInfo.getDisplayName()});
        }
    }

    protected boolean openEditorDialog(KeyedImageInfo keyedImageInfo) {
        EditKeyedImageInfoPanel panel = new EditKeyedImageInfoPanel(keyedImageInfo);

        DialogWithButtons dialogWithButtons = new DialogWithButtons(StrangeEons.getWindow(), true);
        dialogWithButtons.setTitle("Edit item");
        dialogWithButtons.setContentComponent(panel);
        dialogWithButtons.addOkCancelButtons(() -> {
            if (StringUtils.isEmpty(keyedImageInfo.getKey())) {
                JOptionPane.showMessageDialog(this, "Please specify a key", "Missing key", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (StringUtils.isEmpty(keyedImageInfo.getDisplayName())) {
                JOptionPane.showMessageDialog(this, "Please specify a display name", "Missing display name", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            Optional<T> duplicate = getEntityList().stream()
                    .filter(o -> o != keyedImageInfo)
                    .filter(o -> o.getKey().equals(keyedImageInfo.getKey()))
                    .findAny();

            if (duplicate.isPresent()) {
                JOptionPane.showMessageDialog(this, "The key specified is already in use by another item in this list", "Duplicate key", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
        });

        int result = dialogWithButtons.showDialog();

        return result == DialogWithButtons.OK_OPTION;
    }

    static class EditKeyedImageInfoPanel extends JPanel {
        public EditKeyedImageInfoPanel(KeyedImageInfo keyedImageInfo) {

            JTextField keyEditor = new JTextField(10);
            JTextField displayNameEditor = new JTextField(10);
            JButton selectImageButton = new JButton("Select image");

            JLabel imageEditor = new JLabel();

            EditorUtils.bindTextComponent(keyEditor, keyedImageInfo::setKey);
            EditorUtils.bindTextComponent(displayNameEditor, keyedImageInfo::setDisplayName);

            selectImageButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser(StrangeEons.getOpenProject().getFile());
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    try {
                        BufferedImage image = ImageIO.read(fileChooser.getSelectedFile());
                        imageEditor.setIcon(new ImageIcon(image));
                        keyedImageInfo.setImage(image);
                    } catch (IOException ex) {
                        ErrorDialog.displayError("Not a valid image file", ex);
                        imageEditor.setIcon(new ImageIcon());
                    }
                }
            });

            keyEditor.setText(keyedImageInfo.getKey());
            displayNameEditor.setText(keyedImageInfo.getDisplayName());

            if (keyedImageInfo.getImage() != null)
                imageEditor.setIcon(new ImageIcon(keyedImageInfo.getImage()));

            // layout
            setLayout(MigLayoutUtils.createOrganiserLayout());

            JPanel optionsPanel = MigLayoutUtils.createTitledPanel("Options");

            MigLayoutUtils.addLabelledComponentWrapGrowPush(optionsPanel, "Key", keyEditor);
            MigLayoutUtils.addLabelledComponentWrapGrowPush(optionsPanel, "Display name", displayNameEditor);
            MigLayoutUtils.addLabelledComponentWrapGrowPush(optionsPanel, "Icon", selectImageButton);

            JPanel imagePanel = MigLayoutUtils.createTitledPanel("Image/icon");
            imagePanel.add(imageEditor, "grow, push");

            add(optionsPanel);
            add(imagePanel);
        }
    }
}
