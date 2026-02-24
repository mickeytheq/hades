package com.mickeytheq.hades.ui.quicksearch;

import ca.cgjennings.ui.DocumentEventAdapter;
import com.mickeytheq.hades.core.Cards;
import com.mickeytheq.hades.core.global.carddatabase.BasicCardDatabase;
import com.mickeytheq.hades.core.global.carddatabase.CardDatabase;
import com.mickeytheq.hades.core.global.carddatabase.CardDatabases;
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
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.strangeeons.plugin.Bootstrapper;
import com.mickeytheq.hades.util.SwingUtils;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                asset.getCommonCardFieldsModel().setCopyright("MickeyTheQ");
                asset.getCommonCardFieldsModel().setRules("Asset" + i);
                cards.add(Cards.createCardModel(asset, new PlayerCardBack()));

                Event event = new Event();
                event.getCommonCardFieldsModel().setTitle("Event" + i);
                cards.add(Cards.createCardModel(event, new PlayerCardBack()));

                Skill skill = new Skill();
                skill.getCommonCardFieldsModel().setTitle("Skill" + i);
                cards.add(Cards.createCardModel(skill, new PlayerCardBack()));
            }

            CardDatabase cardDatabase = new BasicCardDatabase(cards);
            CardDatabases.set(cardDatabase);

            QuickSearchDialog dialog = new QuickSearchDialog();
            dialog.setVisible(true);
        });
    }

    private final JTextField searchTextField;
    private final JList<Object> resultList;
    private final JScrollPane scrollPane;

    private final List<QuickSearchMode> searchModes = new ArrayList<>();

    private QuickSearchMode currentSearchMode;

    public QuickSearchDialog() {
        searchModes.add(new CardTitleQuickSearchMode());
        searchModes.add(new TextCardContentSearchMode());

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        setModal(true);
        setUndecorated(true);

        installKeyboardShortcuts();

        searchTextField = new JTextField(60);
        searchTextField.setFont(searchTextField.getFont().deriveFont(14.0f));
        resultList = new JList<>();
        resultList.setFont(searchTextField.getFont());

        scrollPane = new JScrollPane(resultList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        searchTextField.getDocument().addDocumentListener(new DocumentEventAdapter() {
            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                performSearch();
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

        MigLayout layout = new MigLayout(MigLayoutUtils.createDefaultLayoutConstraints().insets("2"));
        getContentPane().setLayout(layout);

        JPanel modeSelectionPanel = createModeSelectionPanel();

        getContentPane().add(modeSelectionPanel, "wrap, growx, pushx");

        getContentPane().add(searchTextField, "wrap, growx, pushx");

        selectMode(searchModes.get(0));

        relayout();

        pack();

        // start the focus on the search field
        searchTextField.requestFocusInWindow();
    }

    private void relayout() {
        getContentPane().remove(scrollPane);

        if (resultList.getModel().getSize() > 0)
            getContentPane().add(scrollPane, "wrap, growx, pushx");
    }

    private void selectMode(QuickSearchMode searchMode) {
        currentSearchMode = searchMode;

        setButtonColours();

        resultList.setCellRenderer(currentSearchMode.getRenderer());

        performSearch();
    }

    private void performSelect() {
        Object selectedValue = resultList.getSelectedValue();

        currentSearchMode.selected(selectedValue);

        // always close the dialog after a selection is made
        SwingUtils.closeWindow(this);
    }

    private void performSearch() {
        boolean resultListVisibleBeforeUpdate = resultList.getModel().getSize() > 0;

        // it is far cheaper/faster to create a new list model on each refresh
        // updating an existing list model causes lots of internal listeners to fire on each
        // element addition/change which in turn triggers costly layout calculations
        DefaultListModel<Object> listModel = new DefaultListModel<>();

        currentSearchMode.performSearch(searchTextField.getText(), listModel);

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

    private void installKeyboardShortcuts() {
        SwingUtils.installEscapeCloseKeyboardShortcut(this);

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

    private final Map<QuickSearchMode, JButton> buttonMap = new HashMap<>();

    private JPanel createModeSelectionPanel() {
        JPanel panel = new JPanel(new MigLayout(MigLayoutUtils.createDefaultLayoutConstraints().insets("2").gridGap("0", "0")));
        for (QuickSearchMode searchMode : searchModes) {
            JButton button = new JButton(searchMode.getDisplay());
            button.setFont(resultList.getFont());
            button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            button.setContentAreaFilled(false);
            button.setOpaque(true);

            buttonMap.put(searchMode, button);

            button.addActionListener(e -> {
                selectMode(searchMode);
            });

            panel.add(button);
        }

        return panel;
    }

    private void setButtonColours() {
        for (Map.Entry<QuickSearchMode, JButton> entry : buttonMap.entrySet()) {
            QuickSearchMode searchMode = entry.getKey();
            JButton button = entry.getValue();

            Color foreground = resultList.getForeground();
            Color background = resultList.getBackground();

            if (searchMode == currentSearchMode) {
                foreground = SwingUtils.copyColor(resultList.getSelectionForeground());
                background = SwingUtils.copyColor(resultList.getSelectionBackground());
            }

            button.setForeground(foreground);
            button.setBackground(background);
        }
    }
}
