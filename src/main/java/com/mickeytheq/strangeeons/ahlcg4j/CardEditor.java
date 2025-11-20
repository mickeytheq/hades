package com.mickeytheq.strangeeons.ahlcg4j;

import ca.cgjennings.apps.arkham.AbstractGameComponentEditor;

import javax.swing.*;

public class CardEditor extends AbstractGameComponentEditor<Card> {
    public CardEditor(Card card) {
        setGameComponent(card);

        // delegate to the individual card faces to create editor controls to go in the editor
        JTabbedPane editorTabbedPane = new JTabbedPane();
        card.getFrontFaceView().createEditors(editorTabbedPane);
        card.getBackFaceView().createEditors(editorTabbedPane);

        // TODO: add a comment tab

        // TODO: decide whether to have encounter set info created by the 'card' rather than the face
        // TODO: or delegate to the face. however having different encounter set info on the front and back would be quite whacky although perhaps we should make this possible but not the default
        // TODO: same goes for collection although I think this is more certain to be 'card' level

        // TODO: split pane with control pane and a preview pane
        // TODO: preview pane should be pretty much the same as the existing plugin
        // TODO: control pane should have some standard stuff but delegate to the individual faces for their control desire

        JTabbedPane previewPane = new JTabbedPane();
        initializeSheetViewers(previewPane);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editorTabbedPane, previewPane);

        getContentPane().add(splitPane);

        pack();
    }

    @Override
    protected void populateComponentFromDelayedFields() {

    }
}
