package com.mickeytheq.hades.ui;

import javax.swing.*;

public class ListItem implements DynamicListCellRenderer.HasDisplay, DynamicListCellRenderer.HasIcon {
    private final String display;
    private final Object value;
    private final Icon icon;

    public ListItem(String display, Object value, Icon icon) {
        this.display = display;
        this.value = value;
        this.icon = icon;
    }

    public ListItem(String display, Icon icon) {
        this(display, display, icon);
    }

    public String getDisplay() {
        return display;
    }

    public Object getValue() {
        return value;
    }

    public Icon getIcon() {
        return icon;
    }
}
