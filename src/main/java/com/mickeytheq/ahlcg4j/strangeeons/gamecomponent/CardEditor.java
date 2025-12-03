package com.mickeytheq.ahlcg4j.strangeeons.gamecomponent;

import ca.cgjennings.apps.arkham.AbstractGameComponentEditor;
import com.mickeytheq.ahlcg4j.core.view.EditorContext;
import com.mickeytheq.ahlcg4j.core.view.utils.MigLayoutUtils;

import javax.swing.*;
import java.awt.*;

public class CardEditor extends AbstractGameComponentEditor<CardGameComponent> {
    private final CardGameComponent cardGameComponent;

    public CardEditor(CardGameComponent cardGameComponent) {
        this.cardGameComponent = cardGameComponent;

        setGameComponent(cardGameComponent);

        // delegate to the individual card faces to create editor controls to go in the editor
        JTabbedPane editorTabbedPane = new JTabbedPane();

        EditorContext editorContext = new EditorContextImpl(editorTabbedPane, 0);
        cardGameComponent.getCardView().getFrontFaceView().createEditors(editorContext);

        if (cardGameComponent.getCardView().hasBack()) {
            editorContext = new EditorContextImpl(editorTabbedPane, 1);
            cardGameComponent.getCardView().getBackFaceView().createEditors(editorContext);
        }

        // TODO: add a comment tab

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

    private class EditorContextImpl implements EditorContext {
        private final JTabbedPane tabbedPane;
        private final int sheetIndex;

        public EditorContextImpl(JTabbedPane tabbedPane, int sheetIndex) {
            this.tabbedPane = tabbedPane;
            this.sheetIndex = sheetIndex;
        }

        @Override
        public void addDisplayComponent(String title, Component component) {
            JPanel spacingPanel = MigLayoutUtils.createEmbeddedPanel();
            spacingPanel.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
            spacingPanel.add(component, "wrap, growx, pushx");

            tabbedPane.addTab(title, spacingPanel);
        }

        @Override
        public void markChanged() {
            cardGameComponent.markChanged(sheetIndex);
        }
    }
}
