package com.mickeytheq.hades.core.view.common;

import com.mickeytheq.hades.core.view.EditorContext;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;

import javax.swing.*;

public class CardFaceViewUtils {
    public static void createEncounterSetCollectionTab(EditorContext editorContext, EncounterSetView encounterSetView, CollectionView collectionView) {
        if (encounterSetView == null && collectionView == null)
            return;

        JPanel mainPanel = MigLayoutUtils.createOrganiserPanel();

        if (encounterSetView != null) {
            JPanel encounterSetPanel = encounterSetView.createStandardEncounterPanel(editorContext);
            mainPanel.add(encounterSetPanel, "pushx, growx, wrap");
        }

        if (collectionView != null) {
            JPanel collectionPanel = collectionView.createStandardCollectionPanel(editorContext);
            mainPanel.add(collectionPanel, "pushx, growx, wrap");
        }

        editorContext.addDisplayComponent("Collection / encounter", mainPanel);

    }
}
