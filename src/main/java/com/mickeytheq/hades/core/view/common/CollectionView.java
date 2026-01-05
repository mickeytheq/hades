package com.mickeytheq.hades.core.view.common;

import ca.cgjennings.graphics.filters.InversionFilter;
import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.common.CollectionModel;
import com.mickeytheq.hades.core.project.configuration.CollectionInfo;
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
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.Optional;

public class CollectionView {
    private static final Rectangle COLLECTION_IMAGE_DRAW_REGION_PORTRAIT = new Rectangle(640, 1020, 26, 26);
    private static final Rectangle COLLECTION_IMAGE_DRAW_REGION_LANDSCAPE = new Rectangle(952, 720, 26, 26);
    private static final Rectangle COLLECTION_NUMBER_DRAW_REGION_PORTRAIT = new Rectangle(636, 1024, 74, 20);
    private static final Rectangle COLLECTION_NUMBER_DRAW_REGION_LANDSCAPE = new Rectangle(944, 725, 74, 20);
    private final CollectionModel model;
    private final CardFaceView cardFaceView;

    private JCheckBox copyOtherFaceEditor;
    private JComboBox<CollectionInfo> collectionEditor;
    private JTextField numberEditor;

    public CollectionView(CollectionModel model, CardFaceView cardFaceView) {
        this.model = model;
        this.cardFaceView = cardFaceView;
    }

    public CollectionModel getModel() {
        return model;
    }

    public void createEditors(EditorContext editorContext) {
        ProjectConfiguration projectConfiguration = editorContext.getProjectContext().getProjectConfiguration();

        // collection
        collectionEditor = EditorUtils.createNullableComboBox();

        for (CollectionInfo collectionInfo : projectConfiguration.getCollectionConfiguration().getCollectionInfos()) {
            collectionEditor.addItem(collectionInfo);
        }

        numberEditor = EditorUtils.createTextField(8);
        numberEditor.setHorizontalAlignment(JTextField.RIGHT);

        copyOtherFaceEditor = EditorUtils.createCheckBox();
        copyOtherFaceEditor.addChangeListener(e -> {
            boolean useOtherFace = copyOtherFaceEditor.isSelected();

            collectionEditor.setEnabled(!useOtherFace);
            numberEditor.setEnabled(!useOtherFace);
        });

        EditorUtils.bindToggleButton(copyOtherFaceEditor, editorContext.wrapConsumerWithMarkedChanged(model::setCopyOtherFace));
        EditorUtils.bindComboBox(collectionEditor, editorContext.wrapConsumerWithMarkedChanged(model::setCollection));
        EditorUtils.bindTextComponent(numberEditor, editorContext.wrapConsumerWithMarkedChanged(model::setNumber));

        copyOtherFaceEditor.setSelected(model.isCopyOtherFace());
        collectionEditor.setSelectedItem(model.getCollection());
        numberEditor.setText(model.getNumber());
    }

    public JPanel createStandardCollectionPanel(EditorContext editorContext) {
        JPanel collectionDetailPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.COLLECTION));
        MigLayoutUtils.addLabelledComponentWrapGrowPush(collectionDetailPanel, Language.string(InterfaceConstants.COPY_OTHER_FACE), copyOtherFaceEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(collectionDetailPanel, Language.string(InterfaceConstants.COLLECTION), collectionEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(collectionDetailPanel, Language.string(InterfaceConstants.COLLECTIONNUMBER), numberEditor);

        return collectionDetailPanel;
    }

    public void paintCollectionImage(PaintContext paintContext, CardFaceOrientation orientation, boolean paintInverted) {
        Rectangle drawRegion;

        if (orientation == CardFaceOrientation.Portrait)
            drawRegion = COLLECTION_IMAGE_DRAW_REGION_PORTRAIT;
        else
            drawRegion = COLLECTION_IMAGE_DRAW_REGION_LANDSCAPE;

        paintCollectionImage(paintContext, drawRegion, paintInverted);
    }

    public void paintCollectionImage(PaintContext paintContext, Rectangle imageDrawRegion, boolean paintInverted) {
        Optional<CollectionModel> modelOpt = getModel(getModel().isCopyOtherFace());

        if (!modelOpt.isPresent())
            return;

        CollectionModel model = modelOpt.get();

        if (model.getCollection() == null)
            return;

        // collection icon sometimes needs inverting
        // the source icon is always black but the background on most cards is black as well therefore we want the icon inverted to white
        // this isn't always the case therefore it is at the discretion of the owning card face to decide
        BufferedImage collectionImage = model.getCollection().getImage().get();

        if (paintInverted) {
            BufferedImageOp inversionOp = new InversionFilter();
            collectionImage = inversionOp.filter(collectionImage, null);
        }

        PaintUtils.paintBufferedImage(paintContext.getGraphics(), collectionImage, imageDrawRegion);
    }

    public void paintCollectionNumber(PaintContext paintContext, CardFaceOrientation orientation) {
        Rectangle drawRegion;

        if (orientation == CardFaceOrientation.Portrait)
            drawRegion = COLLECTION_NUMBER_DRAW_REGION_PORTRAIT;
        else
            drawRegion = COLLECTION_NUMBER_DRAW_REGION_LANDSCAPE;

        paintCollectionNumber(paintContext, drawRegion);
    }

    public void paintCollectionNumber(PaintContext paintContext, Rectangle drawRegion) {
        Optional<CollectionModel> modelOpt = getModel(getModel().isCopyOtherFace());

        if (!modelOpt.isPresent())
            return;

        CollectionModel model = modelOpt.get();

        if (!StringUtils.isEmpty(model.getNumber())) {
            MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
            markupRenderer.setDefaultStyle(TextStyleUtils.getCollectionNumberTextStyle());
            markupRenderer.setAlignment(MarkupRenderer.LAYOUT_RIGHT | MarkupRenderer.LAYOUT_MIDDLE);
            markupRenderer.setMarkupText(model.getNumber());
            markupRenderer.drawAsSingleLine(paintContext.getGraphics(), drawRegion);
        }
    }

    // gets the NumberingModel to use
    // this might return the model of the card if requested for copying settings from the opposite face
    // if the useOtherFace is specified but the other card face doesn't have numbering, empty is returned
    private Optional<CollectionModel> getModel(boolean useOtherFace) {
        if (!useOtherFace)
            return Optional.of(model);

        Optional<CardFaceView> otherFaceViewOpt = cardFaceView.getOtherFaceView();

        if (!otherFaceViewOpt.isPresent())
            return Optional.empty();

        CardFaceView otherCardFaceView = otherFaceViewOpt.get();

        if (otherCardFaceView instanceof HasCollectionView) {
            CollectionView collectionView = ((HasCollectionView)otherCardFaceView).getCollectionView();

            return Optional.of(collectionView.getModel());
        }

        return Optional.empty();
    }
}
