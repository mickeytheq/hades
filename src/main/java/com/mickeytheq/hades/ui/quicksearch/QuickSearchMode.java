package com.mickeytheq.hades.ui.quicksearch;

import javax.swing.*;

public interface QuickSearchMode {
    // display string for this mode to be used to draw labels for mode selection
    String getDisplay();

    // populate the given item list with matched items using the given input text
    void performSearch(String searchText, DefaultListModel<Object> listModel);

    // get the renderer used to present individual items in the item list
    ListCellRenderer<Object> getRenderer();

    // perform an action on this item, e.g. navigating to the relevant item/card
    void selected(Object selectedItem);
}
