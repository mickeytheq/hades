package com.mickeytheq.hades.ui.quicksearch;

import ca.cgjennings.ui.DocumentEventAdapter;
import com.mickeytheq.hades.core.CardFaces;
import com.mickeytheq.hades.core.global.BasicCardDatabase;
import com.mickeytheq.hades.core.global.CardDatabase;
import com.mickeytheq.hades.core.global.CardDatabases;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.model.cardfaces.Asset;
import com.mickeytheq.hades.core.model.cardfaces.Event;
import com.mickeytheq.hades.core.model.cardfaces.PlayerCardBack;
import com.mickeytheq.hades.core.model.cardfaces.Skill;
import com.mickeytheq.hades.core.model.image.NothingImagePersister;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.ProjectContexts;
import com.mickeytheq.hades.core.project.StandardProjectContext;
import com.mickeytheq.hades.core.project.configuration.ProjectConfiguration;
import com.mickeytheq.hades.strangeeons.plugin.Bootstrapper;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class QuickSearchDialog extends JDialog {
    public static void main(String[] args) {
        Bootstrapper.initaliseOutsideStrangeEons();

        ProjectConfiguration projectConfiguration = new ProjectConfiguration();

        ProjectContext projectContext = new StandardProjectContext(projectConfiguration, new NothingImagePersister());

        ProjectContexts.withContext(projectContext, () -> {
            List<Card> cards = new ArrayList<>();

            for (int i = 1; i <= 100; i++) {
                Asset asset = new Asset();
                asset.getCommonCardFieldsModel().setTitle("Asset" + i);
                cards.add(CardFaces.createCardModel(asset, new PlayerCardBack()));

                Event event = new Event();
                event.getCommonCardFieldsModel().setTitle("Event" + i);
                cards.add(CardFaces.createCardModel(event, new PlayerCardBack()));

                Skill skill = new Skill();
                skill.getCommonCardFieldsModel().setTitle("Skill" + i);
                cards.add(CardFaces.createCardModel(skill, new PlayerCardBack()));
            }

            CardDatabase cardDatabase = new BasicCardDatabase(cards);
            CardDatabases.set(cardDatabase);

            QuickSearchDialog dialog = new QuickSearchDialog();
            dialog.setVisible(true);
        });
    }

    private final JTextField textField;
    private final JList<Object> resultList;
    private final JScrollPane scrollPane;

    private SearchMode searchMode = new CardTitleSearchMode();

    public QuickSearchDialog() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        setModal(true);
        setUndecorated(true);

        installEscapeCloseOperation(this);
        installKeyStrokes();

        textField = new JTextField(60);
        textField.setFont(textField.getFont().deriveFont(14.0f));
        resultList = new JList<>();
        resultList.setFont(textField.getFont());

        resultList.setCellRenderer(searchMode.getRenderer());

        scrollPane = new JScrollPane(resultList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        textField.getDocument().addDocumentListener(new DocumentEventAdapter() {
            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                textChanged();
            }
        });

        resultList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1)
                    return;

                int index = resultList.locationToIndex(e.getPoint());

                if (index < 0)
                    return;

                if (!resultList.getCellBounds(index, index).contains(e.getPoint()))
                    return;

                performSelect();
            }
        });

        MigLayout layout = new MigLayout();
        getContentPane().setLayout(layout);
        getContentPane().add(textField, "wrap, growx, pushx");

        relayout();

        pack();
    }

    private void relayout() {
        getContentPane().remove(scrollPane);

        if (resultList.getModel().getSize() > 0)
            getContentPane().add(scrollPane, "wrap, growx, pushx");
    }

    private void performSelect() {
        Object selectedValue = resultList.getSelectedValue();

        searchMode.selected(selectedValue);

        dispose();
    }

    private void textChanged() {
        boolean resultListVisibleBeforeUpdate = resultList.getModel().getSize() > 0;

        // it is far cheaper/faster to create a new list model on each refresh
        // updating an existing list model causes lots of internal listeners to fire on each
        // element addition/change which in turn triggers costly layout calculations
        DefaultListModel<Object> listModel = new DefaultListModel<>();

        searchMode.performSearch(textField.getText(), listModel);

        resultList.setModel(listModel);

        if (!listModel.isEmpty()) {
            resultList.setSelectedIndex(0);
            resultList.setVisibleRowCount(8);
        }

        boolean resultListVisibleAfterUpdate = resultList.getModel().getSize() > 0;

        // only perform the relatively expensive layout/pack operations if the dropdown list has changed
        // visibility before/after the search
        if (resultListVisibleBeforeUpdate != resultListVisibleAfterUpdate) {
            relayout();

            pack();
        }
    }

    private static final KeyStroke UP_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
    private static final KeyStroke DOWN_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
    private static final KeyStroke ENTER_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);

    private static final Object PREVIOUS_SELECTION_ACTION = new Object();
    private static final Object NEXT_SELECTION_ACTION = new Object();
    private static final Object SELECT_ACTION = new Object();

    private void installKeyStrokes() {
        JRootPane root = getRootPane();

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(UP_KEY_STROKE, PREVIOUS_SELECTION_ACTION);
        root.getActionMap().put(PREVIOUS_SELECTION_ACTION, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = resultList.getSelectedIndex();

                if (selectedIndex == -1)
                    return;

                if (selectedIndex == 0)
                    return;

                resultList.setSelectedIndex(selectedIndex - 1);
                resultList.ensureIndexIsVisible(resultList.getSelectedIndex());
            }
        });

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(DOWN_KEY_STROKE, NEXT_SELECTION_ACTION);
        root.getActionMap().put(NEXT_SELECTION_ACTION, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = resultList.getSelectedIndex();

                if (selectedIndex == -1)
                    return;

                if (selectedIndex >= resultList.getModel().getSize() - 1)
                    return;

                resultList.setSelectedIndex(selectedIndex + 1);
                resultList.ensureIndexIsVisible(resultList.getSelectedIndex());
            }
        });

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ENTER_KEY_STROKE, SELECT_ACTION);
        root.getActionMap().put(SELECT_ACTION, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSelect();
            }
        });
    }

    private static final KeyStroke ESCAPE_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    public static final Object CLOSE_DIALOG_ACTION = new Object();

    public static void installEscapeCloseOperation(final JDialog dialog) {
        Action dispatchClosing = new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
            }
        };
        JRootPane root = dialog.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ESCAPE_STROKE, CLOSE_DIALOG_ACTION);
        root.getActionMap().put(CLOSE_DIALOG_ACTION, dispatchClosing);
    }
}
