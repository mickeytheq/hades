package com.mickeytheq.hades.ui;

import javax.swing.*;
import java.awt.*;

// a list cell renderer for that inspects the value for various options to handle rendering
public class DynamicListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        String label;
        Icon icon = null;

        if (value instanceof HasIcon)
            icon = ((HasIcon)value).getIcon();

        if (value instanceof HasDisplay)
            label = ((HasDisplay)value).getDisplay();
        else
            label = String.valueOf(value);

        super.getListCellRendererComponent(list, label, index, isSelected, cellHasFocus);

        setIcon(icon);

        return this;
    }

    public interface HasIcon {
        Icon getIcon();
    }

    public interface HasDisplay {
        String getDisplay();
    }
}