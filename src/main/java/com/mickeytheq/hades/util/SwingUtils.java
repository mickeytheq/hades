package com.mickeytheq.hades.util;

import javax.swing.table.DefaultTableModel;

public class SwingUtils {
    public static DefaultTableModel createNonEditableModel() {
        return new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
}
