package com.mickeytheq.hades.ui;

import ca.cgjennings.ui.DocumentEventAdapter;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.scratchpad.Scratch;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

// a selection component that presents as a text box and when the text changes
// a popup will show possible matching values
// selecting a value either by mouse click or pressing enter will invoke listeners
// this component has no 'state' as such as it is used as a picker of values
public class JSelector<E> extends JPanel {
    private final Function<String, List<E>> searchFunction;

    private JTextField searchEditor;
    private JScrollPane scrollPane;
    private JList<E> list;
    private JPopupMenu popupMenu;

    public JSelector(Function<String, List<E>> searchFunction) {
        this.searchFunction = searchFunction;

        list = new JList<>();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane = new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        searchEditor = new JTextField(30);
        searchEditor.getDocument().addDocumentListener(new DocumentEventAdapter() {
            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                // on the text box changing re-populate the selection box
                performSearch();
            }
        });

        searchEditor.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                performSearch();
            }

            @Override
            public void focusLost(FocusEvent e) {
                closePopupMenu();
            }
        });

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1)
                    return;

                int index = list.locationToIndex(e.getPoint());

                if (index < 0)
                    return;

                if (!list.getCellBounds(index, index).contains(e.getPoint()))
                    return;

                performSelect();
            }
        });

        setLayout(MigLayoutUtils.createOrganiserLayout());
        add(searchEditor, "pushx, growx");
    }

    private void performSearch() {
        String text = searchEditor.getText();

        // it is far cheaper/faster to create a new list model on each refresh
        // updating an existing list model causes lots of internal listeners to fire on each
        // element addition/change which in turn triggers costly layout calculations
        DefaultListModel<E> listModel = new DefaultListModel<>();

        searchFunction.apply(text).forEach(listModel::addElement);

        list.setModel(listModel);

        if (!listModel.isEmpty()) {
            list.setSelectedIndex(0);
            list.setVisibleRowCount(Math.min(8, listModel.getSize()));

            if (popupMenu == null) {
                popupMenu = createPopupMenu();
                popupMenu.show(this, 0, getHeight());
            }

            // re-pack/validate the popup menu so it resizes if necessary
            popupMenu.pack();
            popupMenu.validate();
        } else {
            closePopupMenu();
        }
    }

    private JPopupMenu createPopupMenu() {
        JPopupMenu newPopupMenu = new JPopupMenu();
        newPopupMenu.setBorder(BorderFactory.createEmptyBorder());
        newPopupMenu.add(scrollPane);
        newPopupMenu.setFocusable(false);
        newPopupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // remove the local popup menu instance when the popup is about to close so a new one will be created
                // if further changes are made
                popupMenu = null;
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });

        installKeyboardShortcuts();

        return newPopupMenu;
    }

    private void closePopupMenu() {
        if (popupMenu == null)
            return;

        popupMenu.setVisible(false);
    }

    private static final KeyStroke UP_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
    private static final KeyStroke DOWN_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
    private static final KeyStroke ENTER_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
    private static final KeyStroke ESCAPE_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);

    private static final Object PREVIOUS_SELECTION_ACTION = new Object();
    private static final Object NEXT_SELECTION_ACTION = new Object();
    private static final Object SELECT_ACTION = new Object();
    private static final Object CLOSE_ACTION = new Object();

    private void installKeyboardShortcuts() {
        JComponent keybindingComponent = searchEditor;

        keybindingComponent.getInputMap(JComponent.WHEN_FOCUSED).put(UP_KEY_STROKE, PREVIOUS_SELECTION_ACTION);
        keybindingComponent.getActionMap().put(PREVIOUS_SELECTION_ACTION, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (popupMenu == null)
                    return;

                int selectedIndex = list.getSelectedIndex();

                if (selectedIndex == -1)
                    return;

                if (selectedIndex == 0)
                    return;

                list.setSelectedIndex(selectedIndex - 1);
                list.ensureIndexIsVisible(list.getSelectedIndex());
            }
        });

        keybindingComponent.getInputMap(JComponent.WHEN_FOCUSED).put(DOWN_KEY_STROKE, NEXT_SELECTION_ACTION);
        keybindingComponent.getActionMap().put(NEXT_SELECTION_ACTION, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (popupMenu == null)
                    return;

                int selectedIndex = list.getSelectedIndex();

                if (selectedIndex == -1)
                    return;

                if (selectedIndex >= list.getModel().getSize() - 1)
                    return;

                list.setSelectedIndex(selectedIndex + 1);
                list.ensureIndexIsVisible(list.getSelectedIndex());
            }
        });

        keybindingComponent.getInputMap(JComponent.WHEN_FOCUSED).put(ENTER_KEY_STROKE, SELECT_ACTION);
        keybindingComponent.getActionMap().put(SELECT_ACTION, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSelect();
            }
        });

        keybindingComponent.getInputMap(JComponent.WHEN_FOCUSED).put(ESCAPE_KEY_STROKE, CLOSE_ACTION);
        keybindingComponent.getActionMap().put(CLOSE_ACTION, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closePopupMenu();
            }
        });
    }

    private void performSelect() {
        closePopupMenu();

        for (SelectionListener<E> selectionListener : selectionListeners) {
            selectionListener.selected(list.getSelectedValue());
        }

        // after a selection is made clear the search text in anticipation of the next search
        searchEditor.setText("");

        searchEditor.requestFocusInWindow();
    }

    public interface SelectionListener<E> {
        void selected(E selectedItem);
    }

    private final List<SelectionListener<E>> selectionListeners = new ArrayList<>();

    public void addSelectionListener(SelectionListener<E> listener) {
        selectionListeners.add(listener);
    }

    public void removeSelectionListener(SelectionListener<E> listener) {
        selectionListeners.add(listener);
    }
}
