package com.mickeytheq.hades.core.view.common;

import ca.cgjennings.layout.MarkupRenderer;
import ca.cgjennings.layout.TextStyle;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.common.EncounterSetModel;
import com.mickeytheq.hades.core.project.configuration.EncounterSetConfiguration;
import com.mickeytheq.hades.core.project.configuration.ProjectConfiguration;
import com.mickeytheq.hades.core.project.ui.ProjectConfigurationDialog;
import com.mickeytheq.hades.core.view.CardFaceOrientation;
import com.mickeytheq.hades.core.view.CardFaceView;
import com.mickeytheq.hades.core.view.EditorContext;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.utils.*;
import com.mickeytheq.hades.ui.Environment;
import com.mickeytheq.hades.util.shape.DimensionEx;
import com.mickeytheq.hades.util.shape.RectangleEx;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class EncounterSetView {
    public static final DimensionEx ENCOUNTER_NUMBERS_SIZE = DimensionEx.millimetres(9.31, PaintConstants.FOOTER_TEXT_HEIGHT_MMS);

    private static final RectangleEx ENCOUNTER_NUMBERS_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(41.83, 86.70, ENCOUNTER_NUMBERS_SIZE);
    private static final RectangleEx ENCOUNTER_NUMBERS_LANDSCAPE_DRAW_REGION = RectangleEx.millimetres(67.90, 61.38, ENCOUNTER_NUMBERS_SIZE);

    private final EncounterSetModel model;
    private final CardFaceView cardFaceView;

    private JCheckBox copyOtherFaceEditor;
    private JComboBox<EncounterSetConfiguration> encounterSetEditor;
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

        for (EncounterSetConfiguration encounterSetConfiguration : projectConfiguration.getEncounterSetConfigurations()) {
            encounterSetEditor.addItem(encounterSetConfiguration);
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
        EditorUtils.bindComboBox(encounterSetEditor, editorContext.wrapConsumerWithMarkedChanged(model::setEncounterSetConfiguration));
        EditorUtils.bindTextComponent(numberEditor, editorContext.wrapConsumerWithMarkedChanged(model::setNumber));
        EditorUtils.bindTextComponent(totalEditor, editorContext.wrapConsumerWithMarkedChanged(model::setTotal));

        copyOtherFaceEditor.setSelected(model.isCopyOtherFace());
        encounterSetEditor.setSelectedItem(model.getEncounterSetConfiguration());
        numberEditor.setText(model.getNumber());
        totalEditor.setText(model.getTotal());
    }

    public JPanel createStandardEncounterPanel(EditorContext editorContext) {
        JPanel encounterDetailPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.ENCOUNTERSET));
        MigLayoutUtils.addLabelledComponentWrapGrowPush(encounterDetailPanel, Language.string(InterfaceConstants.COPY_OTHER_FACE), copyOtherFaceEditor);

        JButton jumpToEncounterDetailButton = new JButton(new ImageIcon(ImageUtils.GEAR_IMAGE));
        jumpToEncounterDetailButton.addActionListener(e -> {
            ProjectConfigurationDialog.openDialog(Environment.getTopLevelWindow(), editorContext.getProjectContext(), ProjectConfigurationDialog.OpenAt.EncounterSet);
        });

        MigLayoutUtils.addLabel(encounterDetailPanel, Language.string(InterfaceConstants.ENCOUNTERSET));
        MigLayoutUtils.addComponentGrowXPushX(encounterDetailPanel, encounterSetEditor, "split 2");
        encounterDetailPanel.add(jumpToEncounterDetailButton, "growy, pushy, wrap, wmax 30");

        MigLayoutUtils.addLabel(encounterDetailPanel, "Encounter number"); // TODO: i18n
        encounterDetailPanel.add(numberEditor, "split");
        encounterDetailPanel.add(new JLabel(" / "), "split");
        encounterDetailPanel.add(totalEditor, "split, wrap");

        return encounterDetailPanel;
    }

    public void paintEncounterImage(PaintContext paintContext, Rectangle drawRegion) {
        Optional<EncounterSetModel> modelOpt = getModelToUse(getModel().isCopyOtherFace());

        if (!modelOpt.isPresent())
            return;

        EncounterSetModel modelToUse = modelOpt.get();

        if (modelToUse.getEncounterSetConfiguration() == null)
            return;

        PaintUtils.paintBufferedImage(paintContext.getGraphics(), modelToUse.getEncounterSetConfiguration().getImage().get(), drawRegion);
    }

    public void paintEncounterNumbers(PaintContext paintContext, CardFaceOrientation orientation) {
        Rectangle drawRegion;

        if (orientation == CardFaceOrientation.Portrait)
            drawRegion = paintContext.toPixelRect(ENCOUNTER_NUMBERS_PORTRAIT_DRAW_REGION);
        else
            drawRegion = paintContext.toPixelRect(ENCOUNTER_NUMBERS_LANDSCAPE_DRAW_REGION);

        paintEncounterNumbers(paintContext, drawRegion);
    }

    public void paintEncounterNumbers(PaintContext paintContext, Rectangle drawRegion) {
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
