package com.mickeytheq.hades.ui.bulkupdate.field;

import com.mickeytheq.hades.core.model.CardFaceModel;
import com.mickeytheq.hades.core.view.utils.EditorUtils;
import com.mickeytheq.hades.ui.bulkupdate.BulkUpdateField;

import javax.swing.*;
import java.util.Objects;

public abstract class StringField implements BulkUpdateField {
    @Override
    public JComponent createEditor() {
        return EditorUtils.createTextField(30);
    }

    protected abstract String getOldValueDisplay(CardFaceModel model);

    protected abstract void update(String value, CardFaceModel model);

    @Override
    public Updater createUpdater(JComponent editor, CardFaceModel model) {
        JTextField textField = (JTextField) editor;
        return new Updater() {
            @Override
            public boolean isUpdateRequired() {
                return Objects.equals(getOldValueDisplay(), getNewValueDisplay());
            }

            @Override
            public String getOldValueDisplay() {
                return StringField.this.getOldValueDisplay(model);
            }

            @Override
            public String getNewValueDisplay() {
                return textField.getText();
            }

            @Override
            public void update() {
                StringField.this.update(textField.getText(), model);
            }
        };
    }
}
