package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.graphics.filters.GreyscaleFilter;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.CardDimensions;
import com.mickeytheq.hades.core.model.cardfaces.InvestigatorMiniCard;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.view.common.HasArtPortraitView;
import com.mickeytheq.hades.core.view.common.PortraitView;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.util.shape.RectangleEx;
import resources.Language;

import javax.swing.*;
import java.awt.image.BufferedImageOp;
import java.util.List;

@View(interfaceLanguageKey = InterfaceConstants.INVESTIGATOR_MINI_CARD)
public class InvestigatorMiniCardView extends BaseCardFaceView<InvestigatorMiniCard> implements HasArtPortraitView {
    private PortraitView portraitView;

    private static final RectangleEx ART_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(0, 0, CardDimensions.MINI_CARD_PORTRAIT_INCHES_WITH_BLEED);

    @Override
    public void initialiseView() {
        portraitView = PortraitView.createWithDefaultImage(getModel().getPortraitModel(), this, ART_PORTRAIT_DRAW_REGION);
    }

    @Override
    public PortraitView getArtPortraitView() {
        return portraitView;
    }

    @Override
    protected List<TemplateInfo> getAvailableTemplateInfos() {
        return Lists.newArrayList(TemplateInfos.createBlankMiniCard600(CardFaceOrientation.Portrait));
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void createEditors(EditorContext editorContext) {
        portraitView.createEditors(editorContext);

        JPanel mainPanel = MigLayoutUtils.createVerticalFlowOrganiserPanel(
                portraitView.createStandardArtPanel(editorContext, getCardFaceSide() == CardFaceSide.Back)
        );

        // add the panel to the main tab control
        editorContext.addDisplayComponent(Language.string(InterfaceConstants.GENERAL), mainPanel);
    }

    @Override
    public void paint(PaintContext paintContext) {
        BufferedImageOp filter = null;

        // apply a greyscale filter on the back
        if (getCardFaceSide() == CardFaceSide.Back)
            filter = new GreyscaleFilter();

        portraitView.paintArtPortrait(paintContext, paintContext.toPixelRect(ART_PORTRAIT_DRAW_REGION), filter);
    }
}
