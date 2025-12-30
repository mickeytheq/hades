package com.mickeytheq.hades.scratchpad;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.model.cardfaces.*;
import com.mickeytheq.hades.core.model.cardfaces.Event;
import com.mickeytheq.hades.core.model.image.ImagePersister;
import com.mickeytheq.hades.core.model.image.ImageProxy;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.ProjectContexts;
import com.mickeytheq.hades.core.project.StandardProjectContext;
import com.mickeytheq.hades.core.project.configuration.EncounterSetInfo;
import com.mickeytheq.hades.core.project.configuration.ProjectConfiguration;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.model.common.PlayerCardSkillIcon;
import com.mickeytheq.hades.core.model.common.Statistic;
import com.mickeytheq.hades.core.view.utils.ImageUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.serialise.CardIO;
import com.mickeytheq.hades.serialise.JsonCardSerialiser;
import com.mickeytheq.hades.strangeeons.plugin.Bootstrapper;
import com.mickeytheq.hades.core.CardFaces;
import com.mickeytheq.hades.ui.DialogWithButtons;
import org.checkerframework.checker.units.qual.N;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.StringWriter;

public class QuickCardView {
    public static void main(String[] args) {
        new QuickCardView().run();
    }

    private ProjectContext projectContext;

    private void run() {
        Bootstrapper.initaliseOutsideStrangeEons();

        ProjectContext temporaryProjectContext = new StandardProjectContext(null, new NothingImagePersister());

        ProjectConfiguration projectConfiguration = ProjectContexts.withContextReturn(temporaryProjectContext, () -> {
            ProjectConfiguration config = new ProjectConfiguration();
            EncounterSetInfo encounterSetInfo = new EncounterSetInfo();
            encounterSetInfo.setDisplayName("Rats");
            encounterSetInfo.setTag("rats");
            encounterSetInfo.setImage(ImageProxy.createStatic(ImageUtils.loadImage("/test/AHLCG-Rats.png")));
            config.getEncounterSetConfiguration().getEncounterSetInfos().add(encounterSetInfo);
            return config;
        });

        projectContext = new StandardProjectContext(projectConfiguration, new NothingImagePersister());

        ProjectContexts.withContext(projectContext, () -> {
//        asset();
//            investigator();
//        event();
//        skill();
//        treacheryTreachery();
//            location();
            agenda();
        });
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

    private void treacheryTreachery() {
        Treachery model = new Treachery();
        model.getCommonCardFieldsModel().setTitle("Rat Swarm");
        model.getCommonCardFieldsModel().setRules("<rev> Do something with <t>A trait</t>.");

        Treachery back = new Treachery();
        model.getCommonCardFieldsModel().setTitle("Back Rat Swarm");
        model.getCommonCardFieldsModel().setRules("<rev> Back rules.");

        Card card = CardFaces.createCardModel(model, back);

        displayEditor(card);
    }

    private void location() {
        Location model = new Location();
        model.getCommonCardFieldsModel().setTitle("Location");
        model.getCommonCardFieldsModel().setRules("Rules");
        model.getLocationFieldsModel().setShroud(new Statistic("2", false));
        model.getLocationFieldsModel().setClues(new Statistic("2", true));

        LocationBack back = new LocationBack();
        back.getCommonCardFieldsModel().setTitle("Back");
        back.getCommonCardFieldsModel().setRules("Rules back");

        Card card = CardFaces.createCardModel(model, back);

        displayEditor(card);
    }

    private void agenda() {
        Agenda model = new Agenda();
        model.setDoom(new Statistic("4", false));
        model.setAgendaNumber("1");
        model.setDeckId("a");
        model.getCommonCardFieldsModel().setTitle("Title");
        model.getAgendaCommonFieldsModel().setStory("Story story");
        model.getAgendaCommonFieldsModel().setRules("Rules rules");

        Card card = CardFaces.createCardModel(model, null);

        displayEditor(card);
    }

    private void displayEditor(Card card) {
        CardView cardView = CardFaces.createCardView(card);
        new Editor(cardView).display();
    }

    private class Editor {
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

            // buttons
            JPanel buttonPanel = MigLayoutUtils.createDialogPanel();
            JButton showJsonButton = new JButton("Show JSON");
            showJsonButton.addActionListener(e -> {
                StringWriter writer = new StringWriter();
                CardIO.writeCard(writer, cardView.getCard(), projectContext);

                showJson(frame, writer.toString());
            });

            buttonPanel.add(showJsonButton);

            JPanel mainPanel = MigLayoutUtils.createDialogPanel();
            mainPanel.add(splitPane, "wrap, grow, push");
            mainPanel.add(buttonPanel, "wrap");

            frame.getContentPane().setLayout(new BorderLayout(2, 2));
            frame.getContentPane().add(mainPanel);
            frame.setPreferredSize(new Dimension(2000, 1200));
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
        }

        private void showJson(JFrame frame, String jsonString) {
            DialogWithButtons dialog = new DialogWithButtons(frame, false);
            dialog.setTitle("JSON view");
            dialog.addDialogClosingButton("Close", 0, () -> Boolean.TRUE);

            JTextArea textArea = new JTextArea(50, 100);
            textArea.setText(jsonString);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            dialog.setContentComponent(scrollPane);

            dialog.setPreferredSize(new Dimension(1000, 800));

            dialog.showDialog();
        }
    }

    class Renderer extends JPanel {
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

    private static class NothingImagePersister implements ImagePersister {
        @Override
        public BufferedImage load(String identifier) {
            return null;
        }

        @Override
        public String save(BufferedImage image, String existingIdentifier) {
            throw new UnsupportedOperationException("Save not supported");
        }
    }

    private class PaintContextImpl extends BasePaintContext {
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

        @Override
        public ProjectContext getProjectContext() {
            return projectContext;
        }
    }

    class EditorContextImpl implements EditorContext {
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

        @Override
        public ProjectContext getProjectContext() {
            return projectContext;
        }
    }
}
