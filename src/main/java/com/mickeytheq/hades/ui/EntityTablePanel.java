package com.mickeytheq.hades.ui;

import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.util.SwingUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

// a base panel that provides management of table of entities with add/edit/delete options
public abstract class EntityTablePanel<T> extends JPanel {
    private final JTable table;

    public EntityTablePanel() {
        DefaultTableModel tableModel = SwingUtils.createNonEditableModel();
        tableModel.addColumn("Entity");

        configureTableModel(tableModel);

        table = new JTable(tableModel);

        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(0));

        configureTable(table);

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

        // refresh when the component is shown
        // as we are in the constructor any sub-class that is assigning to a member variable in the constructor
        // won't have done that yet so information may be missing. solve by just refreshing the table when this becomes visible
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshTable();
            }
        });
    }

    private T getSelectedEntity() {
        int selectedRow = table.getSelectedRow();
        return (T)table.getModel().getValueAt(selectedRow, 0);
    }

    private void add() {
        boolean entityAdded = performAdd();

        if (!entityAdded)
            return;

        refreshTable();
    }

    private void edit() {
        T entity = getSelectedEntity();

        boolean entityEdited = performEdit(entity);

        if (!entityEdited)
            return;

        refreshTable();
    }

    private void delete() {
        T entity = getSelectedEntity();

        boolean entityDeleted = performDelete(entity);

        if (!entityDeleted)
            return;

        refreshTable();
    }

    public void refreshTable() {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0);

        populateTable(tableModel);
    }

    protected abstract void configureTableModel(DefaultTableModel tableModel);

    protected abstract void configureTable(JTable table);

    protected abstract boolean performAdd();

    protected abstract boolean performEdit(T entity);

    protected abstract boolean performDelete(T entity);

    protected abstract void populateTable(DefaultTableModel tableModel);
}
