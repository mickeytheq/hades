package com.mickeytheq.hades.core.view.common;

import com.mickeytheq.hades.core.view.EditorContext;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;

import javax.swing.*;

public class CardFaceViewUtils {
    public static void createEncounterSetCollectionTab(EditorContext editorContext, EncounterSetView encounterSetView, CollectionView collectionView) {
        JPanel encounterSetPanel = encounterSetView.createStandardEncounterPanel(editorContext);
        JPanel collectionPanel = collectionView.createStandardCollectionPanel(editorContext);

        JPanel mainPanel = MigLayoutUtils.createOrganiserPanel();
        mainPanel.add(encounterSetPanel, "pushx, growx, wrap");
        mainPanel.add(collectionPanel, "pushx, growx, wrap");

        editorContext.addDisplayComponent("Collection / encounter", mainPanel);

    }
}
