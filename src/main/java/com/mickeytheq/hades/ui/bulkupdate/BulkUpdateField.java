package com.mickeytheq.hades.ui.bulkupdate;

import com.mickeytheq.hades.core.model.CardFaceModel;

import javax.swing.*;

public interface BulkUpdateField {
    String getDisplayName();

    boolean isApplicable(CardFaceModel model);

    JComponent createEditor();

    Updater createUpdater(JComponent editor, CardFaceModel model);

    interface Updater {
        boolean isUpdateRequired();

        String getOldValueDisplay();

        String getNewValueDisplay();

        void update();
    }
}
