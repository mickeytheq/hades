package com.mickeytheq.hades.ui.cardreviewer;

import ca.cgjennings.apps.arkham.component.GameComponent;
import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import com.mickeytheq.hades.core.CardFaces;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.StandardProjectContext;
import com.mickeytheq.hades.core.view.CardView;
import com.mickeytheq.hades.core.view.utils.CardFaceViewUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.serialise.CardIO;
import com.mickeytheq.hades.strangeeons.util.MemberUtils;
import com.mickeytheq.hades.ui.DialogEx;
import org.apache.commons.lang3.Strings;
import resources.ResourceKit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CardCompareDialog extends DialogEx {
    private JLabel totalDisplay;
    private JPanel imagePanel;

    private final ItemSource<Path> pathItemSource;

    private final Path hadesRoot;
    private final Path ahlcgPluginRoot;

    public CardCompareDialog(Frame frame, Path hadesRoot, Path ahlcgPluginRoot) {
        super(frame, false);

        this.hadesRoot = hadesRoot;
        this.ahlcgPluginRoot = ahlcgPluginRoot;

        try (Stream<Path> stream = Files.walk(hadesRoot)) {
            pathItemSource = new ListItemSource<>(stream.filter(MemberUtils::isPathHadesFile).collect(Collectors.toList()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
            previous();
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

        if (!pathItemSource.next()) {
            return;
        }

        showCurrent();

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
        if (!pathItemSource.previous())
            return;

        showCurrent();
    }

    private void next() {
        if (!pathItemSource.next())
            return;

        showCurrent();
    }

    private void showCurrent() {
        totalDisplay.setText((pathItemSource.getCurrentIndex() + 1) + "/" + pathItemSource.getTotal());

        // load the hades card
        Path currentPath = pathItemSource.getCurrent();
        ProjectContext projectContext = StandardProjectContext.getContextForContentPath(currentPath);
        Card card = CardIO.readCard(currentPath, projectContext);
        CardView cardView = CardFaces.createCardView(card, projectContext);
        BufferedImage hadesImage = CardFaceViewUtils.paintCardFace(cardView.getFrontFaceView(), RenderTarget.PREVIEW, 300);

        // load the equivalent SE card
        Path relativePath = hadesRoot.relativize(currentPath);
        Path ahlcgEquivalentPath = Paths.get(Strings.CS.replace(ahlcgPluginRoot.resolve(relativePath).toString(), ".hades", ".eon"));

        BufferedImage eonsImage;
        if (MemberUtils.isPathEonFile(ahlcgEquivalentPath)) {
            GameComponent gameComponent = ResourceKit.getGameComponentFromFile(ahlcgEquivalentPath.toFile());
            Sheet[] sheets = gameComponent.createDefaultSheets();
            eonsImage = sheets[0].paint(RenderTarget.PREVIEW, 150);
        }
        else {
            eonsImage = new BufferedImage(hadesImage.getWidth(), hadesImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        }

        imagePanel.removeAll();

        imagePanel.add(createImagePanel("Hades - " + relativePath, hadesImage));
        imagePanel.add(createImagePanel("AHLCG plugin - " + relativePath, eonsImage));
    }

    private JPanel createImagePanel(String title, BufferedImage image) {
        JPanel panel = MigLayoutUtils.createTitledPanel(title);
        panel.add(new JLabel(new ImageIcon(image)), "wrap, push, grow");

        return panel;
    }
}
