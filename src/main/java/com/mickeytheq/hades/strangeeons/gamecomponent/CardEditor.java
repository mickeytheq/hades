package com.mickeytheq.hades.strangeeons.gamecomponent;

import ca.cgjennings.apps.arkham.AbstractGameComponentEditor;
import ca.cgjennings.apps.arkham.SheetViewer;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.core.view.EditorContext;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class CardEditor extends AbstractGameComponentEditor<CardGameComponent> {
    private final CardGameComponent cardGameComponent;

    public CardEditor(CardGameComponent cardGameComponent) {
        this.cardGameComponent = cardGameComponent;

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

        JTabbedPane previewPane = new JTabbedPane();
        initializeSheetViewers(previewPane);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editorTabbedPane, previewPane);

        getContentPane().add(splitPane);

        pack();
    }

    @Override
    protected void populateComponentFromDelayedFields() {

    }

    @Override
    protected void createTimer(int updatePeriod) {
        // SE has a heartbeat mechanic beats on a timer and on each beat checks if anything needs redrawing
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
        // set the title of the card to the front face's title
        String title = cardGameComponent.getCardView().getFrontFaceView().getTitle();

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
        public void markChanged() {
            // check if the title needs updating
            updateTitle();

            cardGameComponent.markChanged(sheetIndex);

            // on any change tell the sheets to repaint
            rerenderSheetViewers();
        }
    }
}
