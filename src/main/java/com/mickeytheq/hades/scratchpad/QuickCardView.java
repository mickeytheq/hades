package com.mickeytheq.hades.scratchpad;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.model.cardfaces.*;
import com.mickeytheq.hades.core.model.cardfaces.Event;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.model.common.PlayerCardSkillIcon;
import com.mickeytheq.hades.core.model.common.Statistic;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.strangeeons.plugin.Bootstrapper;
import com.mickeytheq.hades.core.CardFaces;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class QuickCardView {
    public static void main(String[] args) {
        new QuickCardView().run();
    }

    private void run() {
        Bootstrapper.initaliseOutsideStrangeEons();

//        asset();
//        investigator();
//        event();
        skill();
//        treachery();
    }

    private void investigator() {
        Investigator model = new Investigator();
        model.getCommonCardFieldsModel().setTitle("MickeyTheQ");

        model.setWillpower("1");
        model.setIntellect("2");
        model.setCombat("3");
        model.setAgility("4");

        model.setHealth("6");
        model.setSanity("8");

        InvestigatorBack backModel = new InvestigatorBack();
        backModel.getSection1().setHeader("<hdr>Deck Size: </hdr>");
        backModel.getSection1().setText("3fdjxk fdjsf sdkfj sdkl jdsf jk ls d f j k lsd jfsdj klfsjk fdsjkl fsdj klfs jklf jkld0");
        backModel.getSection2().setHeader("<hdr>Deck Size: </hdr>");
        backModel.getSection2().setText("3fdjxk fdjsf sdkfj sdkl jdsf jk ls d f j k lsd jfsdj klfsjk fdsjkl fsdj klfs jklf jkld0");
        backModel.getSection3().setHeader("<hdr>Deck Size: </hdr>");
        backModel.getSection3().setText("3fdjxk fdjsf sdkfj sdkl jdsf jk ls d f j k lsd jfsdj klfsjk fdsjkl fsdj klfs jklf jkld0");
        backModel.getSection4().setHeader("<hdr>Deck Size: </hdr>");
        backModel.getSection4().setText("3fdjxk fdjsf sdkfj sdkl jdsf jk ls d f j k lsd jfsdj klfsjk fdsjkl fsdj klfs jklf jkld0");
        backModel.setStory("'Waffle waffle waffle fdsfdsfdsfsd fdskl;fdskfds fdfdsfsd fdsfdsfd sfds fds fsd fds fds fsd fdsf dsf dsf dsf dsf sdfds fsd fsd fds fds fsd fsd fds fds fsd fds fdsf dsdfsfd'");

        Card card = CardFaces.createCardModel(model, backModel);

        displayEditor(card);
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


        Card card = CardFaces.createCardModel(model, null);

        displayEditor(card);
    }

    private void skill() {
        Skill model = new Skill();
        model.getCommonCardFieldsModel().setTitle("Smeg skill");
        model.getCommonCardFieldsModel().setRules("If this test succeeds, draw 1 card.");
        model.getPlayerCardFieldsModel().setSkillIcon1(PlayerCardSkillIcon.Intellect);
        model.getPlayerCardFieldsModel().setSkillIcon2(PlayerCardSkillIcon.Intellect);
        model.getPlayerCardFieldsModel().setSkillIcon3(PlayerCardSkillIcon.Intellect);
        model.getPlayerCardFieldsModel().setSkillIcon4(PlayerCardSkillIcon.Intellect);
        model.getPlayerCardFieldsModel().setSkillIcon5(PlayerCardSkillIcon.Intellect);
        model.getPlayerCardFieldsModel().setLevel(5);


        Card card = CardFaces.createCardModel(model, null);

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


        Card card = CardFaces.createCardModel(model, null);

        displayEditor(card);
    }

    private void treachery() {
        Treachery model = new Treachery();
        model.getCommonCardFieldsModel().setTitle("Rat Swarm");
        model.getCommonCardFieldsModel().setRules("<rev> Do something with <t>A trait</t>.");

        Card card = CardFaces.createCardModel(model, null);

        displayEditor(card);
    }

    private static void displayEditor(Card card) {
        CardView cardView = CardFaces.createCardView(card);
        new Editor(cardView).display();
    }

    private static class Editor {
        private final CardView cardView;

        public Editor(CardView cardView) {
            this.cardView = cardView;
        }

        public void display() {
            // draw/renderer
            JTabbedPane drawTabbedPane = new JTabbedPane();
            Renderer frontRenderer = new Renderer(cardView, cardView.getFrontFaceView());
            drawTabbedPane.addTab("Front", frontRenderer);

            Renderer backRenderer = null;
            if (cardView.hasBack()) {
                backRenderer = new Renderer(cardView, cardView.getBackFaceView());
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

            cardView.getFrontFaceView().createEditors(editorContext);
            if (cardView.hasBack()) {
                cardView.getBackFaceView().createEditors(editorContext);
            }

            cardView.addCommentsTab(new EditorContextImpl(editTabbedPane, () -> {}));

            // pane for both
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editTabbedPane, drawTabbedPane);

            JFrame frame = new JFrame();
            frame.getContentPane().setLayout(new BorderLayout(2, 2));
            frame.getContentPane().add(splitPane);
            frame.setPreferredSize(new Dimension(2000, 1200));
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
        }
    }

    static class Renderer extends JPanel {
        private final CardView cardView;
        private final CardFaceView cardFaceView;

        public Renderer(CardView cardView, CardFaceView cardFaceView) {
            this.cardView = cardView;
            this.cardFaceView = cardFaceView;
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D)graphics;

            BufferedImage bufferedImage = new BufferedImage((int) cardFaceView.getDimension().getWidth(), (int) cardFaceView.getDimension().getHeight(), BufferedImage.TYPE_INT_ARGB);
            cardFaceView.paint(new PaintContextImpl(RenderTarget.PREVIEW, cardView, bufferedImage));

            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            g.drawImage(bufferedImage, 0, 0, null);
        }
    }

    private static class PaintContextImpl extends BasePaintContext {
        private final BufferedImage bufferedImage;
        private final double dpi = 150;

        public PaintContextImpl(RenderTarget renderTarget, CardView cardView, BufferedImage bufferedImage) {
            super(renderTarget, cardView);
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
        public double getRenderingDpi() {
            return dpi;
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
        public void addDisplayComponent(String title, Component component) {
            JPanel spacingPanel = new JPanel(MigLayoutUtils.createTopLevelLayout());
            spacingPanel.add(component, "wrap, growx, pushx, pushy, growy");

            tabbedPane.addTab(title, spacingPanel);
        }

        @Override
        public void markChanged() {
            markChanged.run();
        }
    }
}
