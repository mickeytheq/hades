package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.graphics.filters.GreyscaleFilter;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.cardfaces.Image;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.common.PortraitView;
import com.mickeytheq.hades.core.view.component.DimensionExComponent;
import com.mickeytheq.hades.core.view.utils.Binder;
import com.mickeytheq.hades.core.view.utils.EditorUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.util.shape.DimensionEx;
import com.mickeytheq.hades.util.shape.RectangleEx;
import resources.Language;

import javax.swing.*;
import java.awt.image.BufferedImageOp;
import java.util.List;

@View(interfaceLanguageKey = InterfaceConstants.IMAGE_CARD_TYPE)
public class ImageView extends BaseCardFaceView<Image> {
    private PortraitView portraitView;

    @Override
    public void initialiseView() {
        portraitView = PortraitView.createWithDefaultImage(getModel().getPortraitModel(), this, getDimension());
    }

    private DimensionEx getDimension() {
        Image.ImageFieldsModel imageFieldsModel = getModel().getImageFieldsModel();

        if (imageFieldsModel.getImageType() == Image.ImageType.Custom)
            return imageFieldsModel.getCustomDimension();

        return imageFieldsModel.getImageType().getDimension();
    }

    @Override
    protected List<TemplateInfo> getAvailableTemplateInfos() {
        return Lists.newArrayList(TemplateInfos.createBlankCustom600(getDimension()));
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void createEditors(EditorContext editorContext) {
        portraitView.createEditors(editorContext);

        JComboBox<Image.ImageType> imageTypeEditor = EditorUtils.createEnumComboBox(Image.ImageType.class);
        DimensionExComponent customDimensionEditor = new DimensionExComponent();
        JComboBox<Image.ImageFilter> imageFilterEditor = EditorUtils.createEnumComboBoxNullable(Image.ImageFilter.class);

        Binder.create()
                .onAnyChange(editorContext::markChanged)
                // on any change check and update the custom dimension editor
                .onAnyChange(() -> updateCustomDimensionEditor(customDimensionEditor))
                // and update the portrait to the new size
                .onAnyChange(this::updatePortrait)
                .comboBox(imageTypeEditor, getModel().getImageFieldsModel()::setImageType)
                .dimension(customDimensionEditor, getModel().getImageFieldsModel()::setCustomDimension)
                .comboBox(imageFilterEditor, getModel().getImageFieldsModel()::setFilter);

        imageTypeEditor.setSelectedItem(getModel().getImageFieldsModel().getImageType());
        customDimensionEditor.setDimensionEx(getModel().getImageFieldsModel().getCustomDimension());
        imageFilterEditor.setSelectedItem(getModel().getImageFieldsModel().getFilter());

        updateCustomDimensionEditor(customDimensionEditor);

        JPanel imagePanel = MigLayoutUtils.createTitledPanel("Image properties");
        MigLayoutUtils.addLabelledComponentWrapGrowPush(imagePanel, "Type", imageTypeEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(imagePanel, "Custom dimensions", customDimensionEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(imagePanel, "Filter", imageFilterEditor);

        JPanel mainPanel = MigLayoutUtils.createVerticalFlowOrganiserPanel(
                imagePanel,
                portraitView.createStandardArtPanel(editorContext)
        );

        // add the panel to the main tab control
        editorContext.addDisplayComponent(Language.string(InterfaceConstants.GENERAL), mainPanel);
    }

    private void updateCustomDimensionEditor(DimensionExComponent dimensionEditor) {
        Image.ImageType imageType = getModel().getImageFieldsModel().getImageType();

        boolean custom = imageType == Image.ImageType.Custom;

        // enable the dimension editor if custom is selected
        dimensionEditor.setEnabled(custom);
    }

    private void updatePortrait() {
        portraitView.setDimension(getDimension());
    }

    @Override
    public void paint(PaintContext paintContext) {
        // paint the portrait on to the full size of the template
        RectangleEx rectangle = RectangleEx.millimetres(0, 0, getDimension());

        BufferedImageOp filter = null;

        Image.ImageFilter imageFilter = getModel().getImageFieldsModel().getFilter();

        if (imageFilter != null) {
            switch (imageFilter) {
                case Greyscale:
                    filter = new GreyscaleFilter();
                    break;
            }
        }

        portraitView.paintArtPortrait(paintContext, paintContext.toPixelRect(rectangle), filter);
    }
}

