package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.layout.MarkupRenderer;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.cardfaces.Story;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.common.*;
import com.mickeytheq.hades.core.view.utils.ImageUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.core.view.utils.MultiSectionRenderer;
import com.mickeytheq.hades.core.view.utils.TextStyleUtils;
import com.mickeytheq.hades.util.shape.RectangleEx;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.Supplier;

@View(interfaceLanguageKey = InterfaceConstants.STORY)
public class StoryView extends BaseCardFaceView<Story> implements HasEncounterSetView, HasCollectionView {
    private CommonCardFieldsView commonCardFieldsView;
    private StorySectionView section1View;
    private StorySectionView section2View;
    private StorySectionView section3View;
    private EncounterSetView encounterSetView;
    private CollectionView collectionView;

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel(), this);
        section1View = new StorySectionView(getModel().getSection1());
        section2View = new StorySectionView(getModel().getSection2());
        section3View = new StorySectionView(getModel().getSection3());
        encounterSetView = new EncounterSetView(getModel().getEncounterSetModel(), this);
        collectionView = new CollectionView(getModel().getCollectionModel(), this);
    }

    @Override
    public CollectionView getCollectionView() {
        return collectionView;
    }

    @Override
    public EncounterSetView getEncounterSetView() {
        return encounterSetView;
    }

    @Override
    protected List<TemplateInfo> getAvailableTemplateInfos() {
        return Lists.newArrayList(TemplateInfos.createStandard300("/templates/story/story_default.png", CardFaceOrientation.Portrait));
    }

    @Override
    public String getTitle() {
        return StringUtils.defaultIfEmpty(commonCardFieldsView.getModel().getTitle(), null);
    }

    @Override
    public void createEditors(EditorContext editorContext) {
        commonCardFieldsView.createEditors(editorContext);
        section1View.createEditors(editorContext);
        section2View.createEditors(editorContext);
        section3View.createEditors(editorContext);
        encounterSetView.createEditors(editorContext);
        collectionView.createEditors(editorContext);

        JPanel generalPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.GENERAL));
        commonCardFieldsView.addTitleEditorsToPanel(generalPanel, false, false, false);
        commonCardFieldsView.addCopyrightEditorToPanel(generalPanel);

        editorContext.addDisplayComponent(Language.string(InterfaceConstants.GENERAL), generalPanel);

        JPanel traitsPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.TRAITS));
        commonCardFieldsView.addTraitsEditorToPanel(traitsPanel);

        JPanel section1Panel = section1View.createPanel(true);
        JPanel section2Panel = section2View.createPanel(true);
        JPanel section3Panel = section3View.createPanel(true);

        JPanel victoryPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.VICTORY));
        commonCardFieldsView.addVictoryEditorsToPanel(victoryPanel);

        JPanel mainPanel = MigLayoutUtils.createVerticalFlowOrganiserPanel(traitsPanel, section1Panel, section2Panel, section3Panel, victoryPanel);

        editorContext.addDisplayComponent(Language.string(InterfaceConstants.GENERAL), mainPanel);

        CardFaceViewUtils.createEncounterSetCollectionTab(editorContext, encounterSetView, collectionView);
    }

    private static final RectangleEx TITLE_DRAW_REGION = RectangleEx.millimetres(6.43, 4.91, 43.01, 7.45);
    private static final RectangleEx BODY_DRAW_REGION = RectangleEx.millimetres(6.10, 18.12, 51.65, 66.89);
    private static final RectangleEx ENCOUNTER_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(51.48, 5.25, 6.10, 6.10);
    private static final RectangleEx ENCOUNTER_NUMBER_DRAW_REGION = RectangleEx.millimetres(34.54, 82.72, EncounterSetView.ENCOUNTER_NUMBERS_SIZE);
    private static final RectangleEx COLLECTION_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(46.31, 82.38, 2.20, 2.20);
    private static final RectangleEx COLLECTION_NUMBER_DRAW_REGION = RectangleEx.millimetres(45.30, 82.72, CollectionView.COLLECTION_NUMBER_SIZE);
    private static final RectangleEx COPYRIGHT_DRAW_REGION = RectangleEx.millimetres(9.99, 82.72, 14.56, PaintConstants.FOOTER_TEXT_HEIGHT_MMS);

    @Override
    public void paint(PaintContext paintContext) {
        // draw the template
        paintContext.getGraphics().drawImage(paintContext.getTemplateInfo().getTemplateImage(), 0, 0, null);

        commonCardFieldsView.paintTitle(paintContext, paintContext.toPixelRect(TITLE_DRAW_REGION));

        // unlike normal cards the footer fields such as copyright/collection info are in black not white
        commonCardFieldsView.paintCopyright(paintContext, paintContext.toPixelRect(COPYRIGHT_DRAW_REGION), TextStyleUtils.withAttribute(TextStyleUtils.getCopyrightTextStyle(), TextAttribute.FOREGROUND, Color.BLACK));

        collectionView.paintCollectionNumber(paintContext, paintContext.toPixelRect(COLLECTION_NUMBER_DRAW_REGION), TextStyleUtils.withAttribute(TextStyleUtils.getCollectionNumberTextStyle(), TextAttribute.FOREGROUND, Color.BLACK));
        collectionView.paintCollectionImage(paintContext, paintContext.toPixelRect(COLLECTION_PORTRAIT_DRAW_REGION), false);

        encounterSetView.paintEncounterNumbers(paintContext, paintContext.toPixelRect(ENCOUNTER_NUMBER_DRAW_REGION), TextStyleUtils.withAttribute(TextStyleUtils.getEncounterNumberTextStyle(), TextAttribute.FOREGROUND, Color.BLACK));
        encounterSetView.paintEncounterPortrait(paintContext, paintContext.toPixelRect(ENCOUNTER_PORTRAIT_DRAW_REGION));

        // do the body back of the card that has multiple sections with different layout requirements
        // use the multi-section renderer to handle the dynamic scaling
        MultiSectionRenderer multiSectionRenderer = new MultiSectionRenderer(paintContext, paintContext.toPixelRect(BODY_DRAW_REGION));

        // traits
        String traits = getModel().getCommonCardFieldsModel().getTraits();

        if (!StringUtils.isEmpty(traits)) {
            Supplier<MarkupRenderer> markupRendererSupplier = () -> {
                MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
                markupRenderer.setDefaultStyle(TextStyleUtils.getTraitTextStyle());
                markupRenderer.setAlignment(MarkupRenderer.LAYOUT_CENTER);
                markupRenderer.setLineTightness(0.6f);
                markupRenderer.setMarkupText(traits);
                return markupRenderer;
            };

            multiSectionRenderer.getSections().add(new MultiSectionRenderer.TextSection(markupRendererSupplier));
        }

        // story sections
        CardFaceViewUtils.buildStorySections(paintContext, multiSectionRenderer, section1View, section2View, section3View);

        // victory
        String victory = getModel().getCommonCardFieldsModel().getVictory();

        if (!StringUtils.isEmpty(victory)) {
            Supplier<MarkupRenderer> markupRendererSupplier = () -> {
                MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
                markupRenderer.setDefaultStyle(TextStyleUtils.getVictoryTextStyle());
                markupRenderer.setAlignment(MarkupRenderer.LAYOUT_CENTER);
                markupRenderer.setLineTightness(0.6f);
                markupRenderer.setMarkupText(victory);
                return markupRenderer;
            };

            multiSectionRenderer.getSections().add(new MultiSectionRenderer.TextSection(markupRendererSupplier));
        }

        multiSectionRenderer.draw();
    }
}
