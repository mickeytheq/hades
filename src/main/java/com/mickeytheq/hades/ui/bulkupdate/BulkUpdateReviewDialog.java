package com.mickeytheq.hades.ui.bulkupdate;

import com.mickeytheq.hades.core.model.CardFaceModel;
import com.mickeytheq.hades.core.model.common.CommonCardFieldsModel;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.ui.DialogEx;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BulkUpdateReviewDialog extends DialogEx {

    public BulkUpdateReviewDialog(Window window, boolean trackDialogSizeToContent, BulkUpdateField bulkUpdateField, JComponent editor, List<CardFaceModel> cardFaceModels) {
        super(window, trackDialogSizeToContent);

        JPanel panel = MigLayoutUtils.createDialogPanel();

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("CardFaceModel");
        tableModel.addColumn("Title");
        tableModel.addColumn("Path");
        tableModel.addColumn("Type");
        tableModel.addColumn("Field");
        tableModel.addColumn("Old value");
        tableModel.addColumn("New value");

        String fieldDisplayName = bulkUpdateField.getDisplayName();

        for (CardFaceModel cardFaceModel : cardFaceModels) {
            if (bulkUpdateField.isApplicable(cardFaceModel)) {
                BulkUpdateField.Updater updater = bulkUpdateField.createUpdater(editor, cardFaceModel);
            }



            tableModel.addRow(new Object[]{cardFaceModel, CommonCardFieldsModel.getTitle(cardFaceModel), fieldDisplayName});
        }
        JTable table = new JTable();
    }
}
