package com.mickeytheq.ahlcg4j.scratchpad;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.ahlcg4j.core.view.CardFaceView;
import com.mickeytheq.ahlcg4j.core.view.EditorContext;
import com.mickeytheq.ahlcg4j.core.view.PaintContext;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.Asset;
import com.mickeytheq.ahlcg4j.core.model.common.PlayerCardSkillIcon;
import com.mickeytheq.ahlcg4j.core.model.common.Statistic;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.Event;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.Treachery;
import com.mickeytheq.ahlcg4j.strangeeons.plugin.Bootstrapper;
import com.mickeytheq.ahlcg4j.core.Card;
import com.mickeytheq.ahlcg4j.core.CardFaces;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class EditCardScratch {
    public static void main(String[] args) {
        new EditCardScratch().run();
    }

    private void run() {
        Bootstrapper.initaliseOutsideStrangeEons();

        asset();
//        event();
//        treachery();
    }

    private void event() {
        Event model = new Event();
        model.getCommonCardFieldsModel().setTitle("Rat Swarm");
        model.getCommonCardFieldsModel().setRules("<rev> Do something with <t>A trait</t>.");
        model.getPlayerCardFieldsModel().setSkillIcon1(PlayerCardSkillIcon.Intellect);
        model.getPlayerCardFieldsModel().setSkillIcon2(PlayerCardSkillIcon.Intellect);
        model.getPlayerCardFieldsModel().setSkillIcon3(PlayerCardSkillIcon.Intellect);
        model.getPlayerCardFieldsModel().setSkillIcon4(PlayerCardSkillIcon.Intellect);
        model.getPlayerCardFieldsModel().setSkillIcon5(PlayerCardSkillIcon.Intellect);
        model.getPlayerCardFieldsModel().setCost("3");
        model.getPlayerCardFieldsModel().setLevel(5);


        Card card = CardFaces.createCard(model, null);

        displayEditor(card);
    }

    private void asset() {
        Asset model = new Asset();
        model.getCommonCardFieldsModel().setTitle("Rat Swarm");
        model.getCommonCardFieldsModel().setRules("<rev> Do something with <t>A trait</t>.");
        model.getPlayerCardFieldsModel().setSkillIcon1(PlayerCardSkillIcon.Intellect);
        model.getPlayerCardFieldsModel().setSkillIcon2(PlayerCardSkillIcon.Intellect);
        model.getPlayerCardFieldsModel().setSkillIcon3(PlayerCardSkillIcon.Intellect);
        model.getPlayerCardFieldsModel().setSkillIcon4(PlayerCardSkillIcon.Intellect);
        model.getPlayerCardFieldsModel().setSkillIcon5(PlayerCardSkillIcon.Intellect);
        model.setAssetSlot1(Asset.AssetSlot.Hand);
        model.setAssetSlot2(Asset.AssetSlot.Arcane);
        model.getPlayerCardFieldsModel().setCost("3");
        model.getPlayerCardFieldsModel().setLevel(5);
        model.setHealth(new Statistic("1", false));
        model.setSanity(new Statistic("1", true));


        Card card = CardFaces.createCard(model, null);

        displayEditor(card);
    }

    private void treachery() {
        Treachery model = new Treachery();
        model.getCommonCardFieldsModel().setTitle("Rat Swarm");
        model.getCommonCardFieldsModel().setRules("<rev> Do something with <t>A trait</t>.");

        Card card = CardFaces.createCard(model, null);

        displayEditor(card);
    }

    private static void displayEditor(Card card) {
        new Editor(card).display();
    }

    private static class Editor {
        private final Card card;

        public Editor(Card card) {
            this.card = card;
        }

        public void display() {
            // draw/renderer
            JTabbedPane drawTabbedPane = new JTabbedPane();
            Renderer frontRenderer = new Renderer(card.getFrontFaceView());
            drawTabbedPane.addTab("Front", frontRenderer);

            Renderer backRenderer = null;
            if (card.hasBack()) {
                backRenderer = new Renderer(card.getBackFaceView());
                drawTabbedPane.addTab("Back", backRenderer);
            }

            // editors
            JTabbedPane editTabbedPane = new JTabbedPane();

            final Renderer backRendererFinal = backRenderer;

            EditorContext editorContext = new EditorContextImpl(editTabbedPane, () -> {
                frontRenderer.repaint();

                if (backRendererFinal != null)
                    backRendererFinal.repaint();
            });

            card.getFrontFaceView().createEditors(editorContext);
            if (card.hasBack()) {
                card.getBackFaceView().createEditors(editorContext);
            }

            // pane for both
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editTabbedPane, drawTabbedPane);

            JFrame frame = new JFrame();
            frame.getContentPane().setLayout(new BorderLayout(2, 2));
            frame.getContentPane().add(splitPane);
            frame.setPreferredSize(new Dimension(1600, 1200));
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
        }
    }

    private static class Renderer extends JPanel {
        private final CardFaceView cardFaceView;

        public Renderer(CardFaceView cardFaceView) {
            this.cardFaceView = cardFaceView;
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D)graphics;

            BufferedImage bufferedImage = new BufferedImage((int) cardFaceView.getDimension().getWidth(), (int) cardFaceView.getDimension().getHeight(), BufferedImage.TYPE_INT_ARGB);
            cardFaceView.paint(new PaintContextImpl(bufferedImage));

            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            g.drawImage(bufferedImage, 0, 0, null);
        }
    }

    private static class PaintContextImpl implements PaintContext {
        private final BufferedImage bufferedImage;
        private final double dpi = 150;

        public PaintContextImpl(BufferedImage bufferedImage) {
            this.bufferedImage = bufferedImage;
        }

        @Override
        public Graphics2D getGraphics() {
            Graphics2D g = bufferedImage.createGraphics();

            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            return g;
        }

        @Override
        public RenderTarget getRenderTarget() {
            return RenderTarget.PREVIEW;
        }

        @Override
        public double getRenderingDpi() {
            return dpi;
        }

        @Override
        public void addTagReplacement(String tag, String replacement) {

        }

        @Override
        public MarkupRenderer createMarkupRenderer() {
            return new MarkupRenderer(dpi);
        }
    }

    static class EditorContextImpl implements EditorContext {
        private final JTabbedPane tabbedPane;
        private final Runnable markChanged;

        public EditorContextImpl(JTabbedPane tabbedPane, Runnable markChanged) {
            this.tabbedPane = tabbedPane;
            this.markChanged = markChanged;
        }

        @Override
        public JTabbedPane getTabbedPane() {
            return tabbedPane;
        }

        @Override
        public void markChanged() {
            markChanged.run();
        }
    }
}
