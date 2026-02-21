package com.mickeytheq.hades.strangeeons.gamecomponent;

import ca.cgjennings.apps.arkham.*;
import ca.cgjennings.apps.arkham.project.Member;
import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.global.carddatabase.CardDatabase;
import com.mickeytheq.hades.core.global.carddatabase.CardDatabases;
import com.mickeytheq.hades.core.global.configuration.CardPreviewConfiguration;
import com.mickeytheq.hades.core.global.configuration.GlobalConfigurations;
import com.mickeytheq.hades.core.model.common.Distance;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.core.view.CardFaceView;
import com.mickeytheq.hades.core.view.EditorContext;
import com.mickeytheq.hades.core.view.utils.CardFaceViewUtils;
import com.mickeytheq.hades.core.view.utils.CardFaceViewViewer;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.serialise.CardIO;
import com.mickeytheq.hades.strangeeons.ui.ExportCardDialog;
import com.mickeytheq.hades.ui.DialogEx;
import com.mickeytheq.hades.util.shape.Unit;
import com.mickeytheq.hades.util.shape.UnitConversionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import static resources.Language.string;

public class CardEditor extends AbstractGameComponentEditor<CardGameComponent> {
    private final JTabbedPane previewPane;

    public CardEditor(CardGameComponent cardGameComponent) {
        setGameComponent(cardGameComponent);

        updateTitle();

        // delegate to the individual card faces to create editor controls to go in the editor
        JTabbedPane editorTabbedPane = new JTabbedPane();

        previewPane = new JTabbedPane();

        createEditorAndViewerForCardFaceView(editorTabbedPane, Language.string(InterfaceConstants.FRONT), cardGameComponent.getCardView().getFrontFaceView());

        if (cardGameComponent.getCardView().hasBack()) {
            createEditorAndViewerForCardFaceView(editorTabbedPane, Language.string(InterfaceConstants.BACK), cardGameComponent.getCardView().getBackFaceView());
        }

        cardGameComponent.getCardView().addCommentsTab(new EditorContextImpl(editorTabbedPane, null, null));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editorTabbedPane, previewPane);

        getContentPane().add(splitPane);

        pack();
    }

    /**
     * the default StrangeEons approach for the preview is to use the {@link ca.cgjennings.apps.arkham.sheet.Sheet} base class and add implementation details on top with a sub-class
     * however performance testing has shown that somewhere in the SheetViewer class there is a significant performance penalty on two fronts
     * 1. painting to the buffered image hosted by the sheet is much slower than it should be (e.g. 100-200ms to paint the template vs 10-20ms). it is not clear
     *    why this performance is so different. loading the Sheet alone and doing painting performance tests did not show this slowdown which suggests
     *    the issue is somewhere in the SheetViewer
     * 2. when the mouse moves in the preview window the preview pane is repainted. while the Sheet caches the image so a full re-paint of the card
     *    face doesn't happen there is still a significant performance hit incurred by how the {@link SheetViewer} scales that sheet image
     *
     * instead a simple {@link AbstractViewer) implementation {@link CardFaceViewViewer) is used that does not have these
     * performance issues
     */
    private void createEditorAndViewerForCardFaceView(JTabbedPane editorTabbedPane, String previewLabel, CardFaceView cardFaceView) {
        CardPreviewConfiguration cardPreviewConfiguration = GlobalConfigurations.get().getCardPreviewConfiguration();

        int resolutionPpi = cardPreviewConfiguration.getDesiredPreviewResolutionPpi();

        // set the desired bleed margin. this is zero if showing bleed margin is disabled
        int desiredBleedMarginInPixels = cardPreviewConfiguration.isShowBleedMargin() ?
                (int)UnitConversionUtils.convertUnit(Unit.Point, Unit.Pixel, cardPreviewConfiguration.getDesiredBleedMarginInPoints(), resolutionPpi) : 0;

        // creates the viewer to show the rendered card face in the preview pane
        CardFaceViewViewer viewer = new CardFaceViewViewer(cardFaceView, resolutionPpi, desiredBleedMarginInPixels);
        previewPane.addTab(previewLabel, viewer);

        // creates editors to change the card face details and attaches the viewer to be told when something changes
        EditorContext editorContext = new EditorContextImpl(editorTabbedPane, cardFaceView.getCardFaceSide(), viewer);
        cardFaceView.createEditors(editorContext);
    }

    @Override
    public void setFile(File newFile) {
        super.setFile(newFile);

        if (getFile() != null) {
            CardDatabase cardDatabase = CardDatabases.getCardDatabase();
            cardDatabase.register(cardDatabaseLoader -> cardDatabaseLoader.registerCard(getGameComponent().getCardView().getCard(), getFile().toPath()), this);
        }
    }

    public void selectPreviewFace(CardFaceSide cardFaceSide) {
        int sheetIndex = cardFaceSide == CardFaceSide.Front ? 0 : 1;

        previewPane.setSelectedIndex(sheetIndex);
    }

    @Override
    public void save() {
        Path path = getFile().toPath();

        CardGameComponent cardGameComponent = getGameComponent();

        CardIO.writeCard(path, cardGameComponent.getCardView().getCard(), cardGameComponent.getCardView().getProjectContext());
        cardGameComponent.markSaved();

        RecentFiles.addRecentDocument(getFile());

        CardDatabases.getCardDatabase().update(cardGameComponent.getCardView().getCard());

        // tell the project view to repaint this member (to un-bold it)
        Member member = StrangeEons.getOpenProject().findMember(getFile());
        if (member != null)
            StrangeEons.getWindow().getOpenProjectView().repaint(member);
    }

    @Override
    protected void exportImpl() {
        ExportCardDialog dialog = new ExportCardDialog(StrangeEons.getWindow());
        if (dialog.showDialog() != DialogEx.OK_OPTION)
            return;

        exportFace(CardFaceSide.Front, dialog);
        exportFace(CardFaceSide.Back, dialog);
    }

    private void exportFace(CardFaceSide cardFaceSide, ExportCardDialog dialog) {
        Optional<CardFaceView> cardFaceViewOptional = getGameComponent().getCardView().getCardFaceView(cardFaceSide);

        if (!cardFaceViewOptional.isPresent())
            return;

        Distance distance = dialog.getBleedMargin();
        int bleedInPixels = (int)UnitConversionUtils.convertUnit(distance.getUnit(), Unit.Pixel, distance.getAmount(), dialog.getResolution());

        BufferedImage image = CardFaceViewUtils.paintCardFace(cardFaceViewOptional.get(), RenderTarget.EXPORT, dialog.getResolution(), bleedInPixels);

        Path outputDirectory = dialog.getOutputDirectory();
        String filename = FilenameUtils.removeExtension(getFile().getName()) + "-" + cardFaceSide.name() + ".png";
        Path outputPath = outputDirectory.resolve(filename);

        try {
            ImageIO.write(image, "png", outputPath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Error writing output file when exporting", e);
        }
    }

    @Override
    protected void populateComponentFromDelayedFields() {

    }

    // Strange Eons has a memory leak where in AbstractGameComponentEditor there is the following line in the constructor
    //
    // AppFrame.getApp().addPropertyChangeListener(StrangeEonsAppWindow.VIEW_BACKDROP_PROPERTY, pcl);
    //
    // there is no corresponding line in the dispose() method to remove this listener. there is this line in dispose()
    //
    // AppFrame.getApp().removePropertyChangeListener(StrangeEonsAppWindow.VIEW_QUALITY_PROPERTY, pcl);
    //
    // so it might be that the remove line above has a typo and it should be specifying VIEW_BACKDROP_PROPERTY instead of VIEW_QUALITY_PROPERTY
    //
    // this results in the main StrangeEons app window holding a reference to the property change listener which in turn
    // keeps the editor class and all of its children (UI controls, components, layout managers etc) alive and unavailable for garbage collection
    //
    // after enough editors are opened and closed without these references being clean up, SE runs out of memory
    //
    // the fix is override dispose() here, reflectively access the 'pcl' field (PropertyChangeListener) in AbstractGameComponentEditor
    // and remove that listener from the StrangeEons app window, basically reversing the line mentioned above in the constructor of AbstractGameComponentEditor
    private static final Field pclField;

    static {
        try {
            pclField = AbstractGameComponentEditor.class.getDeclaredField("pcl");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("No 'pcl' field found on AbstractGameComponentEditor", e);
        }
        pclField.setAccessible(true);
    }

    @Override
    public void dispose() {
        super.dispose();

        PropertyChangeListener propertyChangeListener;
        try {
            propertyChangeListener = (PropertyChangeListener) pclField.get(this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error retrieving PropertyChangeListener from pcl private field via reflection", e);
        }

        StrangeEons.getWindow().removePropertyChangeListener(StrangeEonsAppWindow.VIEW_BACKDROP_PROPERTY, propertyChangeListener);

        CardDatabases.getCardDatabase().unregister(this);
    }

    @Override
    protected void createTimer(int updatePeriod) {
        // SE has a heartbeat mechanic that beats on a timer and on each beat checks if anything needs redrawing
        // this results in delays between pressing a key and the card being redrawn
        // Hades rendering is fast enough that keystrokes can result in an immediate repaint
        //
        // therefore we override this method to intercept and stop the heartbeat timer from being created
        // in conjunction with rerenderSheetViewers below this gives us much better response times in the UI
    }

    private void updateTitle() {
        // update the title that shows in the UI tab for the card to the front face's title
        String title = getGameComponent().getCardView().getFrontFaceView().getTitle();

        if (StringUtils.isEmpty(title)) {
            title = "(No title)"; // TODO: i18n
        }

        if (!Objects.equals(getTitle(), title))
            setTitle(title);
    }

    private class EditorContextImpl implements EditorContext {
        private final JTabbedPane tabbedPane;
        private final CardFaceSide cardFaceSide;
        private final CardFaceViewViewer viewer;

        public EditorContextImpl(JTabbedPane tabbedPane, CardFaceSide cardFaceSide, CardFaceViewViewer viewer) {
            this.tabbedPane = tabbedPane;
            this.cardFaceSide = cardFaceSide;
            this.viewer = viewer;
        }

        @Override
        public void addDisplayComponent(String title, Component component) {
            JPanel spacingPanel = new JPanel(MigLayoutUtils.createTopLevelLayout());
            spacingPanel.add(component, "wrap, grow, push");

            // when a display component is added, create a new tab in the tabbed pane and set the title
            // to the given title with Front/Back prefixed
            String tabTitle = title;
            if (cardFaceSide == CardFaceSide.Front) {
                tabTitle = Language.string(InterfaceConstants.FRONT) + " - " + tabTitle;
            }
            else if (cardFaceSide == CardFaceSide.Back) {
                tabTitle = Language.string(InterfaceConstants.BACK) + " - " + tabTitle;
            }

            tabbedPane.addTab(tabTitle, spacingPanel);
        }

        @Override
        public ProjectContext getProjectContext() {
            return getGameComponent().getCardView().getProjectContext();
        }

        @Override
        public void markChanged() {
            // check if the title needs updating
            updateTitle();

            // mark the game component as unsaved
            getGameComponent().markUnsavedChanges();

            // tell the corresponding viewer to update/refresh
            if (viewer != null)
                viewer.markChanged();
        }
    }
}
