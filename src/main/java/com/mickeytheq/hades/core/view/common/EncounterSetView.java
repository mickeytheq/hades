package com.mickeytheq.hades.core.view.common;

import ca.cgjennings.layout.MarkupRenderer;
import ca.cgjennings.layout.TextStyle;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.common.EncounterSetModel;
import com.mickeytheq.hades.core.project.configuration.EncounterSetInfo;
import com.mickeytheq.hades.core.project.configuration.ProjectConfiguration;
import com.mickeytheq.hades.core.view.CardFaceOrientation;
import com.mickeytheq.hades.core.view.CardFaceView;
import com.mickeytheq.hades.core.view.EditorContext;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.utils.EditorUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.core.view.utils.PaintUtils;
import com.mickeytheq.hades.core.view.utils.TextStyleUtils;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class EncounterSetView {
    private static final Rectangle ENCOUNTER_NUMBERS_DRAW_REGION_PORTRAIT = new Rectangle(494, 1024, 110, 20);
    private static final Rectangle ENCOUNTER_NUMBERS_DRAW_REGION_LANDSCAPE = new Rectangle(802, 725, 110, 20);

    private final EncounterSetModel model;
    private final CardFaceView cardFaceView;

    private JCheckBox copyOtherFaceEditor;
    private JComboBox<EncounterSetInfo> encounterSetEditor;
    private JTextField numberEditor;
    private JTextField totalEditor;

    public EncounterSetView(EncounterSetModel model, CardFaceView cardFaceView) {
        this.model = model;
        this.cardFaceView = cardFaceView;
    }

    public EncounterSetModel getModel() {
        return model;
    }

    public void createEditors(EditorContext editorContext) {
        ProjectConfiguration projectConfiguration = editorContext.getProjectContext().getProjectConfiguration();

        // encounter
        encounterSetEditor = EditorUtils.createNullableComboBox();

        for (EncounterSetInfo encounterSetInfo : projectConfiguration.getEncounterSetConfiguration().getEncounterSetInfos()) {
            encounterSetEditor.addItem(encounterSetInfo);
        }

        numberEditor = EditorUtils.createTextField(8);
        numberEditor.setHorizontalAlignment(JTextField.RIGHT);
        totalEditor = EditorUtils.createTextField(4);

        copyOtherFaceEditor = EditorUtils.createCheckBox();
        copyOtherFaceEditor.addChangeListener(e -> {
            boolean useOtherFace = copyOtherFaceEditor.isSelected();

            encounterSetEditor.setEnabled(!useOtherFace);
            numberEditor.setEnabled(!useOtherFace);
            totalEditor.setEnabled(!useOtherFace);
        });

        EditorUtils.bindToggleButton(copyOtherFaceEditor, editorContext.wrapConsumerWithMarkedChanged(model::setCopyOtherFace));
        EditorUtils.bindComboBox(encounterSetEditor, editorContext.wrapConsumerWithMarkedChanged(model::setEncounterSet));
        EditorUtils.bindTextComponent(numberEditor, editorContext.wrapConsumerWithMarkedChanged(model::setNumber));
        EditorUtils.bindTextComponent(totalEditor, editorContext.wrapConsumerWithMarkedChanged(model::setTotal));

        copyOtherFaceEditor.setSelected(model.isCopyOtherFace());
        encounterSetEditor.setSelectedItem(model.getEncounterSet());
        numberEditor.setText(model.getNumber());
        totalEditor.setText(model.getTotal());
    }

    public JPanel createStandardEncounterPanel(EditorContext editorContext) {
        JPanel encounterDetailPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.ENCOUNTERSET));
        MigLayoutUtils.addLabelledComponentWrapGrowPush(encounterDetailPanel, Language.string(InterfaceConstants.COPY_OTHER_FACE), copyOtherFaceEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(encounterDetailPanel, Language.string(InterfaceConstants.ENCOUNTERSET), encounterSetEditor);
        MigLayoutUtils.addLabel(encounterDetailPanel, "Encounter number"); // TODO: i18n
        encounterDetailPanel.add(numberEditor, "split");
        encounterDetailPanel.add(new JLabel(" / "), "split");
        encounterDetailPanel.add(totalEditor, "split, wrap");

        return encounterDetailPanel;
    }

    public void paintEncounterPortrait(PaintContext paintContext, Rectangle encounterPortraitDrawRegion) {
        Optional<EncounterSetModel> modelOpt = getModelToUse(getModel().isCopyOtherFace());

        if (!modelOpt.isPresent())
            return;

        EncounterSetModel modelToUse = modelOpt.get();

        if (modelToUse.getEncounterSet() == null)
            return;

        PaintUtils.paintBufferedImage(paintContext.getGraphics(), modelToUse.getEncounterSet().getImage().get(), encounterPortraitDrawRegion);
    }

    public void paintEncounterNumbers(PaintContext paintContext, CardFaceOrientation orientation) {
        Rectangle drawRegion;

        if (orientation == CardFaceOrientation.Portrait)
            drawRegion = ENCOUNTER_NUMBERS_DRAW_REGION_PORTRAIT;
        else
            drawRegion = ENCOUNTER_NUMBERS_DRAW_REGION_LANDSCAPE;

        paintEncounterNumbers(paintContext, drawRegion, TextStyleUtils.getEncounterNumberTextStyle());
    }

    public void paintEncounterNumbers(PaintContext paintContext, Rectangle drawRegion, TextStyle textStyle) {
        Optional<EncounterSetModel> modelOpt = getModelToUse(getModel().isCopyOtherFace());

        if (!modelOpt.isPresent())
            return;

        EncounterSetModel modelToUse = modelOpt.get();

        if (!StringUtils.isEmpty(modelToUse.getNumber()) || !StringUtils.isEmpty(modelToUse.getTotal())) {
            MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
            markupRenderer.setDefaultStyle(textStyle);
            markupRenderer.setAlignment(MarkupRenderer.LAYOUT_RIGHT | MarkupRenderer.LAYOUT_MIDDLE);

            String text = StringUtils.defaultIfEmpty(modelToUse.getNumber(), "") + " / " + StringUtils.defaultIfEmpty(modelToUse.getTotal(), "");

            markupRenderer.setMarkupText(text);
            markupRenderer.drawAsSingleLine(paintContext.getGraphics(), drawRegion);
        }
    }

    // gets the Model to use
    // this might return the model of the card if requested for copying settings from the opposite face
    // if the useOtherFace is specified but the other card face doesn't have numbering, empty is returned
    private Optional<EncounterSetModel> getModelToUse(boolean useOtherFace) {
        if (!useOtherFace)
            return Optional.of(model);

        Optional<CardFaceView> otherFaceViewOpt = cardFaceView.getOtherFaceView();

        if (!otherFaceViewOpt.isPresent())
            return Optional.empty();

        CardFaceView otherCardFaceView = otherFaceViewOpt.get();

        if (otherCardFaceView instanceof HasEncounterSetView) {
            EncounterSetView encounterSetView = ((HasEncounterSetView)otherCardFaceView).getEncounterSetView();

            return Optional.of(encounterSetView.getModel());
        }

        return Optional.empty();
    }
}
