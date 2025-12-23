package com.mickeytheq.hades.core.project.ui;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.dialog.ErrorDialog;
import com.mickeytheq.hades.core.project.configuration.TaggedImageInfo;
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

public abstract class BaseEncounterSetsCollectionPanel<T extends TaggedImageInfo> extends EntityTablePanel<T> {
    @Override
    protected void configureTableModel(DefaultTableModel tableModel) {
        tableModel.addColumn("Tag");
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
        for (TaggedImageInfo taggedImageInfo : getEntityList()) {
            tableModel.addRow(new Object[]{taggedImageInfo, taggedImageInfo.getTag(), taggedImageInfo.getDisplayName()});
        }
    }

    protected boolean openEditorDialog(TaggedImageInfo taggedImageInfo) {
        EditTaggedImageInfoPanel panel = new EditTaggedImageInfoPanel(taggedImageInfo);

        DialogWithButtons dialogWithButtons = new DialogWithButtons(StrangeEons.getWindow(), true);
        dialogWithButtons.setTitle("Edit item");
        dialogWithButtons.setContentComponent(panel);
        dialogWithButtons.addOkCancelButtons(() -> {
            if (StringUtils.isEmpty(taggedImageInfo.getTag())) {
                JOptionPane.showMessageDialog(this, "Please specify a tag", "Missing tag", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (StringUtils.isEmpty(taggedImageInfo.getDisplayName())) {
                JOptionPane.showMessageDialog(this, "Please specify a display name", "Missing display name", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            Optional<T> duplicate = getEntityList().stream()
                    .filter(o -> o != taggedImageInfo)
                    .filter(o -> o.getTag().equals(taggedImageInfo.getTag()))
                    .findAny();

            if (duplicate.isPresent()) {
                JOptionPane.showMessageDialog(this, "The tag specified is already in use by another item in this list", "Duplicate tag", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
        });

        int result = dialogWithButtons.showDialog();

        return result == DialogWithButtons.OK_OPTION;
    }

    static class EditTaggedImageInfoPanel extends JPanel {
        public EditTaggedImageInfoPanel(TaggedImageInfo taggedImageInfo) {

            // TODO: should tag be read only on an existing item? at least inform user that links will break if changed
            JTextField tagEditor = new JTextField(10);
            JTextField displayNameEditor = new JTextField(10);
            JButton selectImageButton = new JButton("Select image");

            JLabel imageEditor = new JLabel();

            EditorUtils.bindTextComponent(tagEditor, taggedImageInfo::setTag);
            EditorUtils.bindTextComponent(displayNameEditor, taggedImageInfo::setDisplayName);

            selectImageButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser(StrangeEons.getOpenProject().getFile());
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    try {
                        BufferedImage bufferedImage = ImageIO.read(fileChooser.getSelectedFile());
                        imageEditor.setIcon(new ImageIcon(bufferedImage));
                        taggedImageInfo.getImage().set(bufferedImage);
                    } catch (IOException ex) {
                        ErrorDialog.displayError("Not a valid image file", ex);
                        imageEditor.setIcon(new ImageIcon());
                    }
                }
            });

            tagEditor.setText(taggedImageInfo.getTag());
            displayNameEditor.setText(taggedImageInfo.getDisplayName());

            if (!taggedImageInfo.getImage().isEmpty())
                imageEditor.setIcon(new ImageIcon(taggedImageInfo.getImage().get()));

            // layout
            setLayout(MigLayoutUtils.createOrganiserLayout());

            JPanel optionsPanel = MigLayoutUtils.createTitledPanel("Options");

            MigLayoutUtils.addLabelledComponentWrapGrowPush(optionsPanel, "Tag", tagEditor);
            MigLayoutUtils.addLabelledComponentWrapGrowPush(optionsPanel, "Display name", displayNameEditor);
            MigLayoutUtils.addLabelledComponentWrapGrowPush(optionsPanel, "Icon", selectImageButton);

            JPanel imagePanel = MigLayoutUtils.createTitledPanel("Image/icon");
            imagePanel.add(imageEditor, "grow, push");

            add(optionsPanel);
            add(imagePanel);
        }
    }
}
