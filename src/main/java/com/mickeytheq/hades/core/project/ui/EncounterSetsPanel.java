package com.mickeytheq.hades.core.project.ui;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.dialog.ErrorDialog;
import com.mickeytheq.hades.core.project.EncounterSetInfo;
import com.mickeytheq.hades.core.project.ProjectConfiguration;
import com.mickeytheq.hades.core.view.utils.EditorUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.ui.DialogWithButtons;
import com.mickeytheq.hades.ui.FileChooser;
import com.mickeytheq.hades.util.SwingUtils;
import org.apache.commons.lang3.StringUtils;
import org.mozilla.javascript.tools.debugger.Dim;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

public class EncounterSetsPanel extends JPanel {
    private final JTable table;

    private final ProjectConfiguration projectConfiguration;

    public EncounterSetsPanel(ProjectConfiguration projectConfiguration) {
        this.projectConfiguration = projectConfiguration;

        DefaultTableModel tableModel = SwingUtils.createNonEditableModel();
        tableModel.addColumn("EncounterSetInfo");
        tableModel.addColumn("Key");
        tableModel.addColumn("Name");

        table = new JTable(tableModel);

        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(0));

        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> add());

        JButton editButton = new JButton("Edit");
        editButton.setEnabled(false);
        editButton.addActionListener(e -> edit());

        JButton deleteButton = new JButton("Delete");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(e -> delete());

        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow < 0)
                return;

            editButton.setEnabled(true);
            deleteButton.setEnabled(true);
        });

        setLayout(MigLayoutUtils.createTopLevelLayout());

        add(scrollPane, "wrap, growx, pushx");
        add(addButton, "gapleft push");
        add(editButton);
        add(deleteButton);

        refreshTable();
    }

    private void add() {
        EncounterSetInfo encounterSetInfo = new EncounterSetInfo();

        if (!openEditorDialog(encounterSetInfo))
            return;

        projectConfiguration.getEncounterSetConfiguration().getEncounterSetInfos().add(encounterSetInfo);

        refreshTable();

        projectConfiguration.save();
    }

    private void edit() {
        int selectedRow = table.getSelectedRow();
        EncounterSetInfo encounterSetInfo = (EncounterSetInfo)table.getModel().getValueAt(selectedRow, 0);

        if (!openEditorDialog(encounterSetInfo))
            return;

        refreshTable();

        projectConfiguration.save();
    }

    private void delete() {
        int selectedRow = table.getSelectedRow();
        EncounterSetInfo encounterSetInfo = (EncounterSetInfo)table.getModel().getValueAt(selectedRow, 0);

        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this encounter set", "Delete encounter set", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
            return;

        projectConfiguration.getEncounterSetConfiguration().getEncounterSetInfos().remove(encounterSetInfo);

        refreshTable();

        projectConfiguration.save();
    }

    private boolean openEditorDialog(EncounterSetInfo encounterSetInfo) {
        EditEncounterSetInfoPanel panel = new EditEncounterSetInfoPanel(encounterSetInfo);

        DialogWithButtons dialogWithButtons = new DialogWithButtons(StrangeEons.getWindow(), true);
        dialogWithButtons.setTitle("Edit encounter set");
        dialogWithButtons.setContentComponent(panel);
        dialogWithButtons.addOkCancelButtons(() -> {
            if (StringUtils.isEmpty(encounterSetInfo.getKey())) {
                JOptionPane.showMessageDialog(this, "Please specify a key", "Missing key", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (StringUtils.isEmpty(encounterSetInfo.getDisplayName())) {
                JOptionPane.showMessageDialog(this, "Please specify a display name", "Missing display name", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            Optional<EncounterSetInfo> duplicateKeyEncounterSetInfo = projectConfiguration.getEncounterSetConfiguration().getEncounterSetInfos().stream()
                    .filter(o -> o != encounterSetInfo)
                    .filter(o -> o.getKey().equals(encounterSetInfo.getKey()))
                    .findAny();

            if (duplicateKeyEncounterSetInfo.isPresent()) {
                JOptionPane.showMessageDialog(this, "The key specified is already in use by another encounter set", "Duplicate key", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
        });

        int result = dialogWithButtons.showDialog();

        return result == DialogWithButtons.OK_OPTION;
    }

    public void refreshTable() {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0);

        for (EncounterSetInfo encounterSetInfo : projectConfiguration.getEncounterSetConfiguration().getEncounterSetInfos()) {
            tableModel.addRow(new Object[]{encounterSetInfo, encounterSetInfo.getKey(), encounterSetInfo.getDisplayName()});
        }
    }

    static class EditEncounterSetInfoPanel extends JPanel {
        private EncounterSetInfo encounterSetInfo;

        private JTextField keyEditor;
        private JTextField displayNameEditor;
        private JButton selectImageButton;
        private JLabel imageEditor;

        public EditEncounterSetInfoPanel(EncounterSetInfo encounterSetInfo) {
            this.encounterSetInfo = encounterSetInfo;

            keyEditor = new JTextField(10);
            displayNameEditor = new JTextField(10);
            selectImageButton = new JButton("Select image");

            imageEditor = new JLabel();

            EditorUtils.bindTextComponent(keyEditor, encounterSetInfo::setKey);
            EditorUtils.bindTextComponent(displayNameEditor, encounterSetInfo::setDisplayName);

            selectImageButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser(StrangeEons.getOpenProject().getFile());
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    try {
                        BufferedImage image = ImageIO.read(fileChooser.getSelectedFile());
                        imageEditor.setIcon(new ImageIcon(image));
                        encounterSetInfo.setImage(image);
                    } catch (IOException ex) {
                        ErrorDialog.displayError("Not a valid image file", ex);
                        imageEditor.setIcon(new ImageIcon());
                    }
                }
            });

            keyEditor.setText(encounterSetInfo.getKey());
            displayNameEditor.setText(encounterSetInfo.getDisplayName());

            if (encounterSetInfo.getImage() != null)
                imageEditor.setIcon(new ImageIcon(encounterSetInfo.getImage()));

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
