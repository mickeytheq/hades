package com.mickeytheq.hades.strangeeons.gamecomponent;

import ca.cgjennings.apps.arkham.*;
import ca.cgjennings.apps.arkham.project.Member;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.global.CardDatabase;
import com.mickeytheq.hades.core.global.CardDatabases;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.core.view.EditorContext;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.serialise.CardIO;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Objects;

import static resources.Language.string;

public class CardEditor extends AbstractGameComponentEditor<CardGameComponent> {
    private final JTabbedPane previewPane;

    public CardEditor(CardGameComponent cardGameComponent) {
        setGameComponent(cardGameComponent);

        updateTitle();

        // delegate to the individual card faces to create editor controls to go in the editor
        JTabbedPane editorTabbedPane = new JTabbedPane();

        EditorContext editorContext = new EditorContextImpl(editorTabbedPane, CardFaceSide.Front);
        cardGameComponent.getCardView().getFrontFaceView().createEditors(editorContext);

        if (cardGameComponent.getCardView().hasBack()) {
            editorContext = new EditorContextImpl(editorTabbedPane, CardFaceSide.Back);
            cardGameComponent.getCardView().getBackFaceView().createEditors(editorContext);
        }

        cardGameComponent.getCardView().addCommentsTab(new EditorContextImpl(editorTabbedPane, null));

        // TODO: decide whether to have encounter set info created by the 'card' rather than the face
        // TODO: or delegate to the face. however having different encounter set info on the front and back would be quite whacky although perhaps we should make this possible but not the default
        // TODO: same goes for collection although I think this is more certain to be 'card' level

        previewPane = new JTabbedPane();
        initializeSheetViewers(previewPane);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editorTabbedPane, previewPane);

        getContentPane().add(splitPane);

        pack();
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

        CardIO.writeCard(path, cardGameComponent.getCardView().getCard(), cardGameComponent.getProjectContext());
        cardGameComponent.markSaved();

        RecentFiles.addRecentDocument(getFile());

        CardDatabases.getCardDatabase().update(cardGameComponent.getCardView().getCard());

        // tell the project view to repaint this member (to un-bold it)
        Member member = StrangeEons.getOpenProject().findMember(getFile());
        if (member != null)
            StrangeEons.getWindow().getOpenProjectView().repaint(member);
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

    private void rerenderSheetViewers() {
        // effectively a re-implementation of SheetViewer.rerenderImage but that's package private
        if (viewers == null)
            return;

        for (SheetViewer viewer : viewers) {
            if (viewer.isShowing()) {
                viewer.repaint();
            }
        }
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

        private final int sheetIndex;

        public EditorContextImpl(JTabbedPane tabbedPane, CardFaceSide cardFaceSide) {
            this.tabbedPane = tabbedPane;
            this.cardFaceSide = cardFaceSide;

            sheetIndex = cardFaceSide == CardFaceSide.Front ? 0 : 1;
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
            return getGameComponent().getProjectContext();
        }

        @Override
        public void markChanged() {
            // check if the title needs updating
            updateTitle();

            // this will mark the sheet as needing repainting the next time a repaint check is done and also
            // tell the SE framework that the content has changed so it will show as un-saved in the UI
            getGameComponent().markChanged(sheetIndex);

            // on any change tell the sheet(s) to repaint
            rerenderSheetViewers();
        }
    }
}
