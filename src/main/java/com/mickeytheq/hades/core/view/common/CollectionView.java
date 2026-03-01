package com.mickeytheq.hades.core.view.common;

import ca.cgjennings.graphics.filters.InversionFilter;
import ca.cgjennings.layout.MarkupRenderer;
import ca.cgjennings.layout.TextStyle;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.common.CollectionModel;
import com.mickeytheq.hades.core.project.configuration.CollectionConfiguration;
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
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.Optional;

public class CollectionView {
    public static final DimensionEx COLLECTION_IMAGE_SIZE = DimensionEx.millimetres(2.20, 2.20);
    public static final DimensionEx COLLECTION_NUMBER_SIZE = DimensionEx.millimetres(6.27, PaintConstants.FOOTER_TEXT_HEIGHT_MMS);

    private static final RectangleEx COLLECTION_IMAGE_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(54.19, 86.36, COLLECTION_IMAGE_SIZE);
    private static final RectangleEx COLLECTION_NUMBER_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(53.85, 86.70, COLLECTION_NUMBER_SIZE);

    private static final RectangleEx COLLECTION_IMAGE_LANDSCAPE_DRAW_REGION = RectangleEx.millimetres(80.60, 60.96, COLLECTION_IMAGE_SIZE);
    private static final RectangleEx COLLECTION_NUMBER_LANDSCAPE_DRAW_REGION = RectangleEx.millimetres(79.93, 61.38, COLLECTION_NUMBER_SIZE);

    private final CollectionModel model;
    private final CardFaceView cardFaceView;

    private JCheckBox copyOtherFaceEditor;
    private JComboBox<CollectionConfiguration> collectionEditor;
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

        for (CollectionConfiguration collectionConfiguration : projectConfiguration.getCollectionConfigurations()) {
            collectionEditor.addItem(collectionConfiguration);
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
        EditorUtils.bindComboBox(collectionEditor, editorContext.wrapConsumerWithMarkedChanged(model::setCollectionConfiguration));
        EditorUtils.bindTextComponent(numberEditor, editorContext.wrapConsumerWithMarkedChanged(model::setNumber));

        copyOtherFaceEditor.setSelected(model.isCopyOtherFace());
        collectionEditor.setSelectedItem(model.getCollectionConfiguration());
        numberEditor.setText(model.getNumber());
    }

    public JPanel createStandardCollectionPanel(EditorContext editorContext) {
        JPanel collectionDetailPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.COLLECTION));
        MigLayoutUtils.addLabelledComponentWrapGrowPush(collectionDetailPanel, Language.string(InterfaceConstants.COPY_OTHER_FACE), copyOtherFaceEditor);

        JButton jumpToButton = new JButton(new ImageIcon(ImageUtils.GEAR_IMAGE));
        jumpToButton.addActionListener(e -> {
            ProjectConfigurationDialog.openDialog(Environment.getTopLevelWindow(), editorContext.getProjectContext(), ProjectConfigurationDialog.OpenAt.Collection);
        });

        MigLayoutUtils.addLabel(collectionDetailPanel, Language.string(InterfaceConstants.COLLECTION));
        MigLayoutUtils.addComponentGrowXPushX(collectionDetailPanel, collectionEditor, "split 2");
        collectionDetailPanel.add(jumpToButton, "growy, pushy, wrap, wmax 30");

        MigLayoutUtils.addLabelledComponentWrapGrowPush(collectionDetailPanel, Language.string(InterfaceConstants.COLLECTIONNUMBER), numberEditor);

        return collectionDetailPanel;
    }

    public void paintCollectionImage(PaintContext paintContext, CardFaceOrientation orientation, boolean paintInverted) {
        Rectangle drawRegion;

        if (orientation == CardFaceOrientation.Portrait)
            drawRegion = paintContext.toPixelRect(COLLECTION_IMAGE_PORTRAIT_DRAW_REGION);
        else
            drawRegion = paintContext.toPixelRect(COLLECTION_IMAGE_LANDSCAPE_DRAW_REGION);

        paintCollectionImage(paintContext, drawRegion, paintInverted);
    }

    public void paintCollectionImage(PaintContext paintContext, Rectangle imageDrawRegion, boolean paintInverted) {
        Optional<CollectionModel> modelOpt = getModel(getModel().isCopyOtherFace());

        if (!modelOpt.isPresent())
            return;

        CollectionModel model = modelOpt.get();

        if (model.getCollectionConfiguration() == null)
            return;

        // collection icon sometimes needs inverting
        // the source icon is always black but the background on most cards is black as well therefore we want the icon inverted to white
        // this isn't always the case therefore it is at the discretion of the owning card face to decide
        BufferedImage collectionImage = model.getCollectionConfiguration().getImage().get();

        if (paintInverted) {
            BufferedImageOp inversionOp = new InversionFilter();
            collectionImage = inversionOp.filter(collectionImage, null);
        }

        PaintUtils.paintBufferedImage(paintContext.getGraphics(), collectionImage, imageDrawRegion);
    }

    public void paintCollectionNumber(PaintContext paintContext, CardFaceOrientation orientation) {
        Rectangle drawRegion;

        if (orientation == CardFaceOrientation.Portrait)
            drawRegion = paintContext.toPixelRect(COLLECTION_NUMBER_PORTRAIT_DRAW_REGION);
        else
            drawRegion = paintContext.toPixelRect(COLLECTION_NUMBER_LANDSCAPE_DRAW_REGION);

        paintCollectionNumber(paintContext, drawRegion);
    }

    public void paintCollectionNumber(PaintContext paintContext, Rectangle drawRegion) {
        paintCollectionNumber(paintContext, drawRegion, TextStyleUtils.getCollectionNumberTextStyle());
    }

    public void paintCollectionNumber(PaintContext paintContext, Rectangle drawRegion, TextStyle textStyle) {
        Optional<CollectionModel> modelOpt = getModel(getModel().isCopyOtherFace());

        if (!modelOpt.isPresent())
            return;

        CollectionModel model = modelOpt.get();

        if (!StringUtils.isEmpty(model.getNumber())) {
            MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
            markupRenderer.setDefaultStyle(textStyle);
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
