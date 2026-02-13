package com.mickeytheq.hades.ui.cardreviewer;

import ca.cgjennings.apps.arkham.StrangeEons;
import com.mickeytheq.hades.core.view.CardView;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.strangeeons.plugin.Bootstrapper;
import com.mickeytheq.hades.strangeeons.util.MemberUtils;
import com.mickeytheq.hades.ui.CardFaceViewComponent;
import com.mickeytheq.hades.ui.DialogEx;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class CardReviewDialog extends DialogEx {
    private final ItemSource<CardView> cardViewIterator;
    private JLabel totalDisplay;
    private JPanel imagePanel;

    public CardReviewDialog(Frame frame, ItemSource<CardView> cardViewIterator) {
        super(frame, false);
        this.cardViewIterator = cardViewIterator;

        initialise();
    }

    private void initialise() {
        JButton nextButton = new JButton(">");
        nextButton.setFont(nextButton.getFont().deriveFont(20f));
        nextButton.addActionListener(e -> {
            next();
        });

        JButton previousButton = new JButton("<");
        previousButton.setFont(nextButton.getFont().deriveFont(20f));
        previousButton.addActionListener(e -> {
            if (!cardViewIterator.previous())
                return;

            showCurrentCard();
        });

        totalDisplay = new JLabel();
        totalDisplay.setFont(totalDisplay.getFont().deriveFont(20f));
        totalDisplay.setHorizontalAlignment(SwingConstants.CENTER);

        imagePanel = MigLayoutUtils.createOrganiserPanel();

        JPanel controlPanel = MigLayoutUtils.createDialogPanel();
        controlPanel.add(previousButton);
        controlPanel.add(totalDisplay, "width 80!, align center");
        controlPanel.add(nextButton);

        JPanel mainPanel = MigLayoutUtils.createOrganiserPanel();
        mainPanel.add(imagePanel, "wrap, push, grow, align center");
        mainPanel.add(controlPanel, "align center");

        installKeyboardShortcuts(mainPanel);

        setContentComponent(mainPanel);

        if (!cardViewIterator.next()) {
            return;
        }

        showCurrentCard();

        setPreferredSize(new Dimension(1200, 1200));

        showDialog();
    }

    private final static Object PREVIOUS_ACTION = new Object();
    private final static Object NEXT_ACTION = new Object();

    private void installKeyboardShortcuts(JPanel panel) {
        KeyStroke left = KeyStroke.getKeyStroke(KeyEvent.VK_KP_RIGHT, 0);

        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(left, PREVIOUS_ACTION);
        panel.getActionMap().put(PREVIOUS_ACTION, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previous();
            }
        });

        KeyStroke right = KeyStroke.getKeyStroke(KeyEvent.VK_KP_LEFT, 0);

        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(right, NEXT_ACTION);
        panel.getActionMap().put(NEXT_ACTION, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                next();
            }
        });
    }

    private void previous() {
        if (!cardViewIterator.previous())
            return;

        showCurrentCard();
    }

    private void next() {
        if (!cardViewIterator.next())
            return;

        showCurrentCard();
    }

    private void showCurrentCard() {
        totalDisplay.setText((cardViewIterator.getCurrentIndex() + 1) + "/" + cardViewIterator.getTotal());

        imagePanel.removeAll();
        imagePanel.add(new CardFaceViewComponent(cardViewIterator.getCurrent().getFrontFaceView()), "grow, push");
    }

    public static void main(String[] args) throws Exception {
        Bootstrapper.initaliseOutsideStrangeEons();

        MigLayoutUtils.setDebug(true);

        Path rootPath = Paths.get("D:\\Temp\\Buffy Investigator Cards SE-Hades\\Buffy Investigators Set One\\1 Investigator and Signature");
        List<Path> hadesFiles = Files.list(rootPath).filter(MemberUtils::isPathHadesFile).collect(Collectors.toList());

        ItemSource<CardView> cardReviewSource = ItemSources.cardViewFromPath(new ListItemSource<>(hadesFiles));

        new CardReviewDialog(StrangeEons.getWindow(), cardReviewSource);
    }
}
