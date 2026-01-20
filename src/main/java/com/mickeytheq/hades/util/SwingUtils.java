package com.mickeytheq.hades.util;

import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SwingUtils {
    public static DefaultTableModel createNonEditableModel() {
        return new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public static Color copyColor(Color color) {
        return new Color(color.getRGB(), true);
    }
}
