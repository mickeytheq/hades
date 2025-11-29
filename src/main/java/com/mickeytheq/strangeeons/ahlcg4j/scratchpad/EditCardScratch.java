package com.mickeytheq.strangeeons.ahlcg4j.scratchpad;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.strangeeons.ahlcg4j.CardGameComponent;
import com.mickeytheq.strangeeons.ahlcg4j.CardFaceSide;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.Card;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.CardFaceView;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.CardFaces;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.EditorContext;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.PaintContext;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.asset.Asset;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.asset.AssetView;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.treachery.Treachery;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.treachery.TreacheryView;
import com.mickeytheq.strangeeons.ahlcg4j.plugin.Bootstrapper;
import com.mickeytheq.strangeeons.ahlcg4j.util.MarkupUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class EditCardScratch {
    public static void main(String[] args) {
        new EditCardScratch().run();
    }

    private void run() {
        Bootstrapper.initaliseOutsideStrangeEons();

        asset();
//        treachery();
    }

    private void asset() {
        Asset model = new Asset();
        model.getCommonCardFieldsModel().setTitle("Rat Swarm");
        model.getCommonCardFieldsModel().setRules("<rev> Do something with <t>A trait</t>.");

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
            frame.setPreferredSize(new Dimension(1400, 1200));
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
        public void addTagReplacement(String tag, String replacement) {

        }

        @Override
        public MarkupRenderer createMarkupRenderer() {
            return new MarkupRenderer(150);
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
