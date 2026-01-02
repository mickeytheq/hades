package com.mickeytheq.hades.scratchpad;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.model.cardfaces.*;
import com.mickeytheq.hades.core.model.cardfaces.Event;
import com.mickeytheq.hades.core.model.image.ImagePersister;
import com.mickeytheq.hades.core.model.image.ImageProxy;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.ProjectContexts;
import com.mickeytheq.hades.core.project.StandardProjectContext;
import com.mickeytheq.hades.core.project.configuration.CollectionInfo;
import com.mickeytheq.hades.core.project.configuration.EncounterSetInfo;
import com.mickeytheq.hades.core.project.configuration.ProjectConfiguration;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.model.common.PlayerCardSkillIcon;
import com.mickeytheq.hades.core.model.common.Statistic;
import com.mickeytheq.hades.core.view.utils.ImageUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.generator.CardFaceGenerator;
import com.mickeytheq.hades.serialise.CardIO;
import com.mickeytheq.hades.strangeeons.plugin.Bootstrapper;
import com.mickeytheq.hades.core.CardFaces;
import com.mickeytheq.hades.ui.DialogWithButtons;
import org.checkerframework.checker.units.qual.C;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

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

            CollectionInfo collectionInfo = new CollectionInfo();
            collectionInfo.setDisplayName("Rats");
            collectionInfo.setTag("rats");
            collectionInfo.setImage(ImageProxy.createStatic(ImageUtils.loadImage("/test/AHLCG-Rats.png")));
            config.getCollectionConfiguration().getCollectionInfos().add(collectionInfo);

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
//            random();
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

        AgendaBack agendaBack = new AgendaBack();
        agendaBack.setAgendaNumber("1");
        agendaBack.setDeckId("b");
        agendaBack.getCommonCardFieldsModel().setTitle("Back title");
        agendaBack.getSection1().setHeader("Header");
        agendaBack.getSection1().setRules("Rules rules");
        agendaBack.getSection1().setStory("Story story");
        agendaBack.getSection2().setHeader("Header");
        agendaBack.getSection2().setRules("Rules rules");
        agendaBack.getSection2().setStory("Story story");
        agendaBack.getSection3().setHeader("Header");
        agendaBack.getSection3().setRules("Rules rules");
        agendaBack.getSection3().setStory("Story story");

        Card card = CardFaces.createCardModel(model, agendaBack);

        displayEditor(card);
    }

    private void random() {
        new Editor(new RandomCardIterator()).create();
    }

    private void displayEditor(Card card) {
        CardView cardView = CardFaces.createCardView(card);
        List<CardView> cardViews = Collections.singletonList(cardView);
        new Editor(cardViews.listIterator()).create();
    }

    private class RandomCardIterator implements ListIterator<CardView> {
        private final List<CardView> accumulated = new ArrayList<>();
        private final CardFaceGenerator generator = new CardFaceGenerator(projectContext);

        private int currentIndex = -1;

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public CardView next() {
            currentIndex++;

            if (currentIndex < accumulated.size()) {
                return accumulated.get(currentIndex);
            }

            Asset asset = generator.createAsset();

            Card card = CardFaces.createCardModel(asset, null);
            CardView cardView = CardFaces.createCardView(card);
            accumulated.add(cardView);

            return cardView;
        }

        @Override
        public boolean hasPrevious() {
            return currentIndex > 0;
        }

        @Override
        public CardView previous() {
            CardView cardView = accumulated.get(currentIndex);
            currentIndex--;
            return cardView;
        }

        @Override
        public int nextIndex() {
            return currentIndex + 1;
        }

        @Override
        public int previousIndex() {
            return currentIndex;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(CardView cardView) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(CardView cardView) {
            throw new UnsupportedOperationException();
        }
    }

    private class Editor {
        private final AlwaysStepIterator<CardView> cardViewIterator;

        private CardView currentCardView;

        private JTabbedPane editTabbedPane;
        private JTabbedPane drawTabbedPane;

        public Editor(ListIterator<CardView> cardViewIterator) {
            this.cardViewIterator = new AlwaysStepIterator<>(cardViewIterator);

            currentCardView = cardViewIterator.next();
        }

        public void create() {
            // draw/renderer
            drawTabbedPane = new JTabbedPane();

            // editors
            editTabbedPane = new JTabbedPane();

            // pane for both
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editTabbedPane, drawTabbedPane);

            JFrame frame = new JFrame();

            // buttons
            JPanel buttonPanel = MigLayoutUtils.createDialogPanel();

            JButton showJsonButton = new JButton("Show JSON");
            showJsonButton.addActionListener(e -> {
                StringWriter writer = new StringWriter();
                CardIO.writeCard(writer, currentCardView.getCard(), projectContext);

                showJson(frame, writer.toString());
            });

            JButton nextButton = new JButton("Next");
            nextButton.addActionListener(e -> {
                if (!cardViewIterator.hasNext())
                    return;

                currentCardView = cardViewIterator.next();

                populateCard();
            });

            JButton previousButton = new JButton("Previous");
            previousButton.addActionListener(e -> {
                if (!cardViewIterator.hasPrevious())
                    return;

                currentCardView = cardViewIterator.previous();

                populateCard();
            });

            JToggleButton toggleDebug = new JToggleButton("Debug");
            toggleDebug.addActionListener(e -> {
                boolean enableDebug = toggleDebug.isSelected();

                MarkupRenderer.DEBUG = enableDebug;
                MigLayoutUtils.setDebug(enableDebug);

                populateCard();
            });

            buttonPanel.add(showJsonButton);
            buttonPanel.add(previousButton);
            buttonPanel.add(nextButton);
            buttonPanel.add(toggleDebug);

            populateCard();

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

        private void populateCard() {
            // clear the panes of content
            drawTabbedPane.removeAll();
            editTabbedPane.removeAll();

            // create renderers
            Renderer frontRenderer = new Renderer(currentCardView, currentCardView.getFrontFaceView());
            drawTabbedPane.addTab("Front", frontRenderer);

            Renderer backRenderer = null;
            if (currentCardView.hasBack()) {
                backRenderer = new Renderer(currentCardView, currentCardView.getBackFaceView());
                drawTabbedPane.addTab("Back", backRenderer);
            }

            // create editors
            final Renderer backRendererFinal = backRenderer;

            EditorContext editorContext = new EditorContextImpl(editTabbedPane, () -> {
                frontRenderer.repaint();

                if (backRendererFinal != null)
                    backRendererFinal.repaint();
            });

            currentCardView.getFrontFaceView().createEditors(editorContext);
            if (currentCardView.hasBack()) {
                currentCardView.getBackFaceView().createEditors(editorContext);
            }

            currentCardView.addCommentsTab(new EditorContextImpl(editTabbedPane, () -> {}));
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
            super.paintComponent(graphics);

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
            return "<random UUID>";
        }
    }

    private class PaintContextImpl extends BasePaintContext {
        private final BufferedImage bufferedImage;
        private final Graphics2D graphics2D;
        private final double dpi = 150;

        public PaintContextImpl(RenderTarget renderTarget, CardView cardView, BufferedImage bufferedImage) {
            super(renderTarget, cardView);
            this.bufferedImage = bufferedImage;
            this.graphics2D = bufferedImage.createGraphics();

            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        @Override
        public Graphics2D getGraphics() {
            return graphics2D;
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

    // iterator that always steps to a next/previous element
    // for example doing next(), next(), previous() will return item 1, item 2, item 1 instead of the ListIterator behaviour
    // of item 1, item 2, item 2
    public static class AlwaysStepIterator<T> {

        private final ListIterator<T> listIterator;

        private boolean nextWasCalled = false;
        private boolean previousWasCalled = false;

        public AlwaysStepIterator(ListIterator<T> listIterator) {
            this.listIterator = listIterator;
        }

        public boolean hasNext() {
            if (previousWasCalled)
                return true;

            return listIterator.hasNext();
        }

        public boolean hasPrevious() {
            if (nextWasCalled)
                return true;

            return listIterator.hasPrevious();
        }

        public T next() {
            nextWasCalled = true;
            if (previousWasCalled) {
                previousWasCalled = false;
                listIterator.next();
            }
            return listIterator.next();
        }

        public T previous() {
            if (nextWasCalled) {
                listIterator.previous();
                nextWasCalled = false;
            }
            previousWasCalled = true;
            return listIterator.previous();
        }

    }
}
