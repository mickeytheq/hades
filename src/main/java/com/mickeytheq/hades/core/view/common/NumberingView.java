package com.mickeytheq.hades.core.view.common;

import ca.cgjennings.graphics.filters.InversionFilter;
import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.project.configuration.CollectionInfo;
import com.mickeytheq.hades.core.project.configuration.EncounterSetInfo;
import com.mickeytheq.hades.core.project.configuration.ProjectConfiguration;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.model.common.NumberingModel;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.utils.EditorUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.core.view.utils.PaintUtils;
import com.mickeytheq.hades.core.view.utils.TextStyleUtils;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.Optional;

public class NumberingView {
    private static final Rectangle COLLECTION_NUMBER_DRAW_REGION = new Rectangle(636, 1024, 74, 20);
    private static final Rectangle ENCOUNTER_NUMBERS_DRAW_REGION = new Rectangle(494, 1024, 110, 20);

    private final NumberingModel model;
    private final CardFaceView cardFaceView;

    private JCheckBox collectionCopyOtherFaceEditor;
    private JComboBox<CollectionInfo> collectionEditor;
    private JTextField collectionNumberEditor;

    private JCheckBox encounterCopyOtherFaceEditor;
    private JComboBox<EncounterSetInfo> encounterSetEditor;
    private JTextField encounterNumberEditor;
    private JTextField encounterTotalEditor;

    public NumberingView(NumberingModel model, CardFaceView cardFaceView) {
        this.model = model;
        this.cardFaceView = cardFaceView;
    }

    public NumberingModel getModel() {
        return model;
    }

    public void createEditors(EditorContext editorContext) {
        ProjectConfiguration projectConfiguration = editorContext.getProjectContext().getProjectConfiguration();

        // collection
        collectionEditor = EditorUtils.createNullableComboBox();

        for (CollectionInfo collectionInfo : projectConfiguration.getCollectionConfiguration().getCollectionInfos()) {
            collectionEditor.addItem(collectionInfo);
        }

        collectionNumberEditor = EditorUtils.createTextField(8);
        collectionNumberEditor.setHorizontalAlignment(JTextField.RIGHT);

        collectionCopyOtherFaceEditor = EditorUtils.createCheckBox();
        collectionCopyOtherFaceEditor.addChangeListener(e -> {
            boolean useOtherFace = collectionCopyOtherFaceEditor.isSelected();

            collectionEditor.setEnabled(!useOtherFace);
            collectionNumberEditor.setEnabled(!useOtherFace);
        });

        // encounter
        encounterSetEditor = EditorUtils.createNullableComboBox();

        for (EncounterSetInfo encounterSetInfo : projectConfiguration.getEncounterSetConfiguration().getEncounterSetInfos()) {
            encounterSetEditor.addItem(encounterSetInfo);
        }

        encounterNumberEditor = EditorUtils.createTextField(8);
        encounterNumberEditor.setHorizontalAlignment(JTextField.RIGHT);
        encounterTotalEditor = EditorUtils.createTextField(4);

        encounterCopyOtherFaceEditor = EditorUtils.createCheckBox();
        encounterCopyOtherFaceEditor.addChangeListener(e -> {
            boolean useOtherFace = encounterCopyOtherFaceEditor.isSelected();

            encounterSetEditor.setEnabled(!useOtherFace);
            encounterNumberEditor.setEnabled(!useOtherFace);
            encounterTotalEditor.setEnabled(!useOtherFace);
        });

        EditorUtils.bindToggleButton(collectionCopyOtherFaceEditor, editorContext.wrapConsumerWithMarkedChanged(model::setCollectionUseOtherFace));
        EditorUtils.bindComboBox(collectionEditor, editorContext.wrapConsumerWithMarkedChanged(model::setCollection));
        EditorUtils.bindTextComponent(collectionNumberEditor, editorContext.wrapConsumerWithMarkedChanged(model::setCollectionNumber));

        EditorUtils.bindToggleButton(encounterCopyOtherFaceEditor, editorContext.wrapConsumerWithMarkedChanged(model::setEncounterUseOtherFace));
        EditorUtils.bindComboBox(encounterSetEditor, editorContext.wrapConsumerWithMarkedChanged(model::setEncounterSet));
        EditorUtils.bindTextComponent(encounterNumberEditor, editorContext.wrapConsumerWithMarkedChanged(model::setEncounterNumber));
        EditorUtils.bindTextComponent(encounterTotalEditor, editorContext.wrapConsumerWithMarkedChanged(model::setEncounterTotal));

        collectionCopyOtherFaceEditor.setSelected(model.isCollectionUseOtherFace());
        collectionEditor.setSelectedItem(model.getCollection());
        collectionNumberEditor.setText(model.getCollectionNumber());

        encounterCopyOtherFaceEditor.setSelected(model.isEncounterUseOtherFace());
        encounterSetEditor.setSelectedItem(model.getEncounterSet());
        encounterNumberEditor.setText(model.getEncounterNumber());
        encounterTotalEditor.setText(model.getEncounterTotal());
    }

    public JPanel createStandardCollectionEncounterPanel(EditorContext editorContext) {
        JPanel collectionDetailPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.COLLECTION));
        MigLayoutUtils.addLabelledComponentWrapGrowPush(collectionDetailPanel, Language.string(InterfaceConstants.COPY_OTHER_FACE), collectionCopyOtherFaceEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(collectionDetailPanel, Language.string(InterfaceConstants.COLLECTION), collectionEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(collectionDetailPanel, Language.string(InterfaceConstants.COLLECTIONNUMBER), collectionNumberEditor);

        JPanel encounterDetailPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.ENCOUNTERSET));
        MigLayoutUtils.addLabelledComponentWrapGrowPush(encounterDetailPanel, Language.string(InterfaceConstants.COPY_OTHER_FACE), encounterCopyOtherFaceEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(encounterDetailPanel, Language.string(InterfaceConstants.ENCOUNTERSET), encounterSetEditor);
        MigLayoutUtils.addLabel(encounterDetailPanel, "Encounter number"); // TODO: i18n
        encounterDetailPanel.add(encounterNumberEditor, "split");
        encounterDetailPanel.add(new JLabel(" / "), "split");
        encounterDetailPanel.add(encounterTotalEditor, "split, wrap");

        // merge collection and encounter into a single tab
        JPanel collectionEncounterPanel = MigLayoutUtils.createOrganiserPanel();
        collectionEncounterPanel.add(collectionDetailPanel, "wrap, pushx, growx");
        collectionEncounterPanel.add(encounterDetailPanel, "wrap, pushx, growx");

        return collectionEncounterPanel;
    }

    public void paintEncounterPortrait(PaintContext paintContext, Rectangle encounterPortraitDrawRegion) {
        Optional<NumberingModel> numberingModelOpt = getNumberingModel(getModel().isEncounterUseOtherFace());

        if (!numberingModelOpt.isPresent())
            return;

        NumberingModel numberingModel = numberingModelOpt.get();

        if (numberingModel.getEncounterSet() == null)
            return;

        PaintUtils.paintBufferedImage(paintContext.getGraphics(), numberingModel.getEncounterSet().getImage().get(), encounterPortraitDrawRegion);
    }

    public void paintEncounterNumbers(PaintContext paintContext) {
        Optional<NumberingModel> numberingModelOpt = getNumberingModel(getModel().isEncounterUseOtherFace());

        if (!numberingModelOpt.isPresent())
            return;

        NumberingModel numberingModel = numberingModelOpt.get();

        if (!StringUtils.isEmpty(numberingModel.getEncounterNumber()) || !StringUtils.isEmpty(numberingModel.getEncounterTotal())) {
            MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
            markupRenderer.setDefaultStyle(TextStyleUtils.getEncounterNumberTextStyle());
            markupRenderer.setAlignment(MarkupRenderer.LAYOUT_RIGHT | MarkupRenderer.LAYOUT_MIDDLE);

            String text = StringUtils.defaultIfEmpty(numberingModel.getEncounterNumber(), "") + " / " + StringUtils.defaultIfEmpty(numberingModel.getEncounterTotal(), "");

            markupRenderer.setMarkupText(text);
            markupRenderer.drawAsSingleLine(paintContext.getGraphics(), ENCOUNTER_NUMBERS_DRAW_REGION);
        }
    }

    public void paintCollectionPortrait(PaintContext paintContext, Rectangle collectionPortraitDrawRegion, boolean paintInverted) {
        Optional<NumberingModel> numberingModelOpt = getNumberingModel(getModel().isCollectionUseOtherFace());

        if (!numberingModelOpt.isPresent())
            return;

        NumberingModel numberingModel = numberingModelOpt.get();

        if (numberingModel.getCollection() == null)
            return;

        // collection icon sometimes needs inverting
        // the source icon is always black but the background on most cards is black as well therefore we want the icon inverted to white
        // this isn't always the case therefore it is at the discretion of the owning card face to decide
        BufferedImage collectionImage = numberingModel.getCollection().getImage().get();

        if (paintInverted) {
            BufferedImageOp inversionOp = new InversionFilter();
            collectionImage = inversionOp.filter(collectionImage, null);
        }

        PaintUtils.paintBufferedImage(paintContext.getGraphics(), collectionImage, collectionPortraitDrawRegion);
    }

    public void paintCollectionNumber(PaintContext paintContext) {
        Optional<NumberingModel> numberingModelOpt = getNumberingModel(getModel().isCollectionUseOtherFace());

        if (!numberingModelOpt.isPresent())
            return;

        NumberingModel numberingModel = numberingModelOpt.get();

        if (!StringUtils.isEmpty(numberingModel.getCollectionNumber())) {
            MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
            markupRenderer.setDefaultStyle(TextStyleUtils.getCollectionNumberTextStyle());
            markupRenderer.setAlignment(MarkupRenderer.LAYOUT_RIGHT | MarkupRenderer.LAYOUT_MIDDLE);
            markupRenderer.setMarkupText(numberingModel.getCollectionNumber());
            markupRenderer.drawAsSingleLine(paintContext.getGraphics(), COLLECTION_NUMBER_DRAW_REGION);
        }
    }

    // gets the NumberingModel to use
    // this might return the model of the card if requested for copying settings from the opposite face
    // if the useOtherFace is specified but the other card face doesn't have numbering, empty is returned
    private Optional<NumberingModel> getNumberingModel(boolean useOtherFace) {
        if (!useOtherFace)
            return Optional.of(model);

        Optional<CardFaceView> otherFaceViewOpt = cardFaceView.getOtherFaceView();

        if (!otherFaceViewOpt.isPresent())
            return Optional.empty();

        CardFaceView otherCardFaceView = otherFaceViewOpt.get();

        if (otherCardFaceView instanceof HasNumberingView) {
            NumberingView numberingView = ((HasNumberingView)otherCardFaceView).getNumberingView();

            return Optional.of(numberingView.getModel());
        }

        return Optional.empty();
    }
}
