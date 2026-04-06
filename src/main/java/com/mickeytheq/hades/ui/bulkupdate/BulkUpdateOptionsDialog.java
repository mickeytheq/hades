package com.mickeytheq.hades.ui.bulkupdate;

import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.CardFaceModel;
import com.mickeytheq.hades.core.model.common.HasCommonCardFieldsModel;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.ui.DialogEx;
import com.mickeytheq.hades.ui.LoggingLevel;
import com.mickeytheq.hades.ui.bulkupdate.field.StringField;
import resources.Language;
import resources.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

public class BulkUpdateOptionsDialog extends DialogEx {
    private JPanel optionsPanel;
    private JComboBox<LoggingLevel> loggingLevelCombo;
    private JCheckBox dryRunCheckbox;
    private JComboBox<UpdateFieldComboBoxItem> fieldComboBox;
    private JComponent selectedFieldEditor;

    private static final List<BulkUpdateField> UPDATE_FIELDS = new ArrayList<>();

    static {
        UPDATE_FIELDS.add(new StringField() {
            @Override
            public boolean isApplicable(CardFaceModel model) {
                return HasCommonCardFieldsModel.getInstance(model).isPresent();
            }

            @Override
            protected String getOldValueDisplay(CardFaceModel model) {
                return HasCommonCardFieldsModel.getInstance(model).get().getCopyright();
            }

            @Override
            protected void update(String value, CardFaceModel model) {
                HasCommonCardFieldsModel.getInstance(model).get().setCopyright(value);
            }

            @Override
            public String getDisplayName() {
                return Language.string(InterfaceConstants.COPYRIGHT);
            }
        });
    }

    public BulkUpdateOptionsDialog(Window window, boolean trackDialogSizeToContent) {
        super(window, trackDialogSizeToContent);

        // logging
        loggingLevelCombo = new JComboBox<>();
        loggingLevelCombo.addItem(LoggingLevel.Normal);
        loggingLevelCombo.addItem(LoggingLevel.Verbose);
        loggingLevelCombo.addItem(LoggingLevel.Debug);
        dryRunCheckbox = new JCheckBox();
        fieldComboBox = new JComboBox<>();

        fieldComboBox.addItemListener(e -> {
            if (e.getStateChange() != ItemEvent.SELECTED)
                return;

            selectedFieldEditor = getBulkUpdateField().createEditor();
        });

        UPDATE_FIELDS.forEach(bulkUpdateField -> fieldComboBox.addItem(new UpdateFieldComboBoxItem(bulkUpdateField)));

        // layout
        optionsPanel = MigLayoutUtils.createDialogPanel();

        // force a minimum size as with the dynamic controls the initial render with the first option in the list
        // may have a default size that is narrower than other options require. for example a dropdown containing long strings
        optionsPanel.setMinimumSize(new Dimension(700, 100));

        optionsPanel.setBorder(BorderFactory.createTitledBorder("Options")); // TODO: i18n

        MigLayoutUtils.addLabelledComponentWrapGrowPush(optionsPanel, "Logging level", loggingLevelCombo);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(optionsPanel, "Dry run (does not update cards)", dryRunCheckbox);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(optionsPanel, "Field to update", fieldComboBox);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(optionsPanel, "New value", selectedFieldEditor);
    }

    private static final String SETTINGS_PREFIX = "ZoopBulkUpdate-";
    private static final String SETTINGS_LOGGING_LEVEL = SETTINGS_PREFIX + "LoggingLevel";
    private static final String SETTINGS_DRY_RUN = SETTINGS_PREFIX + "DryRun";

//    @Override
//    public void load() {
//        Settings settings = Settings.getUser();
//
//        LoggingLevel loggingLevel = LoggingLevel.Normal;
//        try {
//            loggingLevel = LoggingLevel.valueOf(settings.get(SETTINGS_LOGGING_LEVEL, LoggingLevel.Normal.toString()));
//        } catch (IllegalArgumentException e) {
//            // ignore, probably an missing or invalid setting
//        }
//        loggingLevelCombo.setSelectedItem(loggingLevel);
//
//        dryRunCheckbox.setSelected(settings.getBoolean(SETTINGS_DRY_RUN, true));
//    }
//
//    @Override
//    public void save() {
//        Settings settings = Settings.getUser();
//
//        settings.set(SETTINGS_LOGGING_LEVEL, ((LoggingLevel) loggingLevelCombo.getSelectedItem()).name());
//        settings.setBoolean(SETTINGS_DRY_RUN, dryRunCheckbox.isSelected());
//    }

    public LoggingLevel getLoggingLevel() {
        return (LoggingLevel) loggingLevelCombo.getSelectedItem();
    }

    public boolean isDryRun() {
        return dryRunCheckbox.isSelected();
    }

    public BulkUpdateField getBulkUpdateField() {
        return ((UpdateFieldComboBoxItem) fieldComboBox.getSelectedItem()).getBulkUpdateField();
    }

    public JComponent getSelectedFieldEditor() {
        return selectedFieldEditor;
    }

    static class UpdateFieldComboBoxItem {
        private final BulkUpdateField bulkUpdateField;

        public UpdateFieldComboBoxItem(BulkUpdateField bulkUpdateField) {
            this.bulkUpdateField = bulkUpdateField;
        }

        public BulkUpdateField getBulkUpdateField() {
            return bulkUpdateField;
        }

        @Override
        public String toString() {
            return bulkUpdateField.getDisplayName();
        }
    }
}
