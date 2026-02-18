package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.layout.PageShape;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.codegenerated.GameConstants;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.cardfaces.Act;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.view.common.*;
import com.mickeytheq.hades.core.view.component.StatisticComponent;
import com.mickeytheq.hades.core.view.utils.*;
import com.mickeytheq.hades.util.shape.RectangleEx;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

@View(interfaceLanguageKey = InterfaceConstants.ACT)
public class ActView extends BaseCardFaceView<Act> implements HasCollectionView, HasEncounterSetView {
    private CommonCardFieldsView commonCardFieldsView;
    private StorySectionView actCommonFieldsView;
    private EncounterSetView encounterSetView;
    private CollectionView collectionView;
    private PortraitView portraitView;

    private JTextField actNumberEditor;
    private JTextField deckIdEditor;
    private StatisticComponent cluesEditor;

    private static final RectangleEx ART_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(37.25, 0.00, 51.65, 63.50);

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel(), this);
        actCommonFieldsView = new StorySectionView(getModel().getStorySectionModel());
        collectionView = new CollectionView(getModel().getCollectionModel(), this);
        encounterSetView = new EncounterSetView(getModel().getEncounterSetModel(), this);
        portraitView = PortraitView.createWithDefaultImage(getModel().getPortraitModel(), this, ART_PORTRAIT_DRAW_REGION);
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
        return TemplateInfos.createStandard300And600("/templates/act_agenda/act", CardFaceOrientation.Landscape);
    }

    @Override
    public String getTitle() {
        return StringUtils.defaultIfEmpty(getModel().getCommonCardFieldsModel().getTitle(), null);
    }

    @Override
    public void createEditors(EditorContext editorContext) {
        commonCardFieldsView.createEditors(editorContext);
        actCommonFieldsView.createEditors(editorContext);
        collectionView.createEditors(editorContext);
        encounterSetView.createEditors(editorContext);
        portraitView.createEditors(editorContext);

        actNumberEditor = EditorUtils.createTextField(20);
        deckIdEditor = EditorUtils.createTextField(20);
        cluesEditor = new StatisticComponent();

        EditorUtils.bindTextComponent(actNumberEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getActFieldsModel()::setNumber));
        EditorUtils.bindTextComponent(deckIdEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getActFieldsModel()::setDeckId));
        EditorUtils.bindStatisticComponent(cluesEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getActFieldsModel()::setClues));

        actNumberEditor.setText(getModel().getActFieldsModel().getNumber());
        deckIdEditor.setText(getModel().getActFieldsModel().getDeckId());
        cluesEditor.setStatistic(getModel().getActFieldsModel().getClues());

        layoutMainTab(editorContext);

        CardFaceViewUtils.createEncounterSetCollectionTab(editorContext, encounterSetView, collectionView);
    }

    private void layoutMainTab(EditorContext editorContext) {
        JPanel titlePanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.TITLE));
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, false, false, false);

        JPanel statsPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.ACT));
        MigLayoutUtils.addLabelledComponent(statsPanel, Language.string(InterfaceConstants.ACTNUMBER), actNumberEditor, "pushx, growx");
        MigLayoutUtils.addLabelledComponent(statsPanel, Language.string(InterfaceConstants.CLUES), cluesEditor, "pushx, growx, wrap");
        MigLayoutUtils.addLabelledComponent(statsPanel, Language.string(InterfaceConstants.SCENARIODECKID), deckIdEditor, "pushx, growx");

        JPanel rulesPanel = actCommonFieldsView.createPanel(false);

        JPanel artPanel = portraitView.createStandardArtPanel(editorContext);

        JPanel copyrightPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.COPYRIGHT));
        commonCardFieldsView.addCopyrightEditorToPanel(copyrightPanel);

        JPanel mainPanel = MigLayoutUtils.createVerticalFlowOrganiserPanel(
                titlePanel, statsPanel, rulesPanel, artPanel, copyrightPanel
        );

        editorContext.addDisplayComponent(Language.string(InterfaceConstants.GENERAL), mainPanel);
    }

    private static final RectangleEx SCENARIO_INDEX_DRAW_REGION = RectangleEx.millimetres(13.04, 2.37, 21.00, 3.56);
    private static final RectangleEx TITLE_DRAW_REGION = RectangleEx.millimetres(3.39, 9.99, 40.64, 7.45);
    private static final RectangleEx BODY_DRAW_REGION = RectangleEx.millimetres(2.54, 17.61, 44.70, 38.44);

    private static final RectangleEx ARTIST_DRAW_REGION = RectangleEx.millimetres(1.00, 61.38, 14.10, PaintConstants.FOOTER_TEXT_HEIGHT_MMS);
    private static final RectangleEx COPYRIGHT_DRAW_REGION = RectangleEx.millimetres(17.00, 61.38, 14.10, PaintConstants.FOOTER_TEXT_HEIGHT_MMS);
    private static final RectangleEx ENCOUNTER_NUMBER_DRAW_REGION = RectangleEx.millimetres(33.00, 61.38, EncounterSetView.ENCOUNTER_NUMBERS_SIZE);
    private static final RectangleEx COLLECTION_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(46.00, 60.96, CollectionView.COLLECTION_IMAGE_SIZE);
    private static final RectangleEx COLLECTION_NUMBER_DRAW_REGION = RectangleEx.millimetres(46.00, 61.38, CollectionView.COLLECTION_NUMBER_SIZE);

    private static final RectangleEx ENCOUNTER_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(21.00, 5.00, 5.0, 5.0);
    private static final RectangleEx CLUES_DRAW_REGION = RectangleEx.millimetres(44.53, 51.39, 0.00, 3.39);

    private static final LoadingCache<Integer, PageShape> BODY_PAGE_CACHE = CacheBuilder.newBuilder().build(CacheLoader.from(ActView::createBodyPageShape));

    private static PageShape createBodyPageShape(int ppi) {
        return MarkupUtils.createStraightLinePathingPageShape(BODY_DRAW_REGION.toPixelRectangle(ppi), Lists.newArrayList(
                new Point2D.Double(0.0, 0.0),
                new Point2D.Double(0.0, 1.00),
                new Point2D.Double(0.715, 1.00),
                new Point2D.Double(0.83, 0.957),
                new Point2D.Double(0.83, 0.82),
                new Point2D.Double(1.0, 0.82),
                new Point2D.Double(1.0, 0.0)
        ));
    }

    @Override
    public void paint(PaintContext paintContext) {
        // paint the main/art portrait first as it sits behind the card template
        portraitView.paintArtPortrait(paintContext, paintContext.toPixelRect(ART_PORTRAIT_DRAW_REGION));

        // draw the template
        paintContext.paintTemplate();

        paintContext.setRenderingIncludeBleedRegion(false);

        // title
        commonCardFieldsView.paintTitleMultiline(paintContext, paintContext.toPixelRect(TITLE_DRAW_REGION));

        encounterSetView.paintEncounterNumbers(paintContext, paintContext.toPixelRect(ENCOUNTER_NUMBER_DRAW_REGION));
        encounterSetView.paintEncounterPortrait(paintContext, paintContext.toPixelRect(ENCOUNTER_PORTRAIT_DRAW_REGION));

        portraitView.paintArtist(paintContext, paintContext.toPixelRect(ARTIST_DRAW_REGION));

        collectionView.paintCollectionImage(paintContext, paintContext.toPixelRect(COLLECTION_PORTRAIT_DRAW_REGION), true);
        collectionView.paintCollectionNumber(paintContext, paintContext.toPixelRect(COLLECTION_NUMBER_DRAW_REGION));

        actCommonFieldsView.paintBody(paintContext, paintContext.toPixelRect(BODY_DRAW_REGION), BODY_PAGE_CACHE.getUnchecked(paintContext.getResolutionInPixelsPerInch()));
        commonCardFieldsView.paintCopyright(paintContext, paintContext.toPixelRect(COPYRIGHT_DRAW_REGION));

        PaintUtils.paintStatistic(paintContext, paintContext.toPixelRect(CLUES_DRAW_REGION), getModel().getActFieldsModel().getClues(), Color.BLACK, PaintUtils.STATISTIC_LIGHT_TEXT_COLOUR);

        PaintUtils.paintScenarioIndex(paintContext, paintContext.toPixelRect(SCENARIO_INDEX_DRAW_REGION), Language.gstring(GameConstants.LABEL_ACT), getModel().getActFieldsModel().getNumber(), getModel().getActFieldsModel().getDeckId());
    }
}
