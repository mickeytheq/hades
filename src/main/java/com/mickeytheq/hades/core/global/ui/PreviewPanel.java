package com.mickeytheq.hades.core.global.ui;

import com.mickeytheq.hades.core.global.configuration.CardPreviewConfiguration;
import com.mickeytheq.hades.core.global.configuration.GlobalConfiguration;
import com.mickeytheq.hades.core.view.utils.Binder;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;

import javax.swing.*;
import java.awt.*;

public class PreviewPanel extends JPanel {
    public PreviewPanel(CardPreviewConfiguration cardPreviewConfiguration) {
        setLayout(MigLayoutUtils.createTopLevelLayout());

        JComboBox<Integer> resolutionEditor = new JComboBox<>();
        resolutionEditor.addItem(300);
        resolutionEditor.addItem(600);

        JCheckBox showBleedMarginEditor = new JCheckBox();

        JSpinner desiredBleedMarginEditor = new JSpinner(new SpinnerNumberModel(0, 0, Double.MAX_VALUE, 0.01));

        MigLayoutUtils.addLabelledComponentWrapGrowPush(this, "Desired resolution (ppi)", resolutionEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(this, "Show bleed margin", showBleedMarginEditor);

        // spinner controls use the maximum size to guess their preferred width which when specifying max double is very very large so constrain it to something sensible here
        MigLayoutUtils.addLabelledComponentWrapGrowPush(this, "Desired bleed margin (points)", desiredBleedMarginEditor, "width 100!");

        Binder.create()
                .comboBox(resolutionEditor, cardPreviewConfiguration::setDesiredPreviewResolutionPpi)
                .toggleButton(showBleedMarginEditor, cardPreviewConfiguration::setShowBleedMargin)
                .spinnerDouble(desiredBleedMarginEditor, cardPreviewConfiguration::setDesiredBleedMarginInPoints);

        resolutionEditor.setSelectedItem(cardPreviewConfiguration.getDesiredPreviewResolutionPpi());
        showBleedMarginEditor.setSelected(cardPreviewConfiguration.isShowBleedMargin());
        desiredBleedMarginEditor.setValue(cardPreviewConfiguration.getDesiredBleedMarginInPoints());
    }
}
