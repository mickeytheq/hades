package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.layout.PageShape;
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
import java.awt.image.BufferedImage;

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

    private static final RectangleEx ART_PORTRAIT_DRAW_REGION = RectangleEx.millimeters(37.25, 0.00, 51.65, 63.50);

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel());
        actCommonFieldsView = new StorySectionView(getModel().getStorySectionModel());
        collectionView = new CollectionView(getModel().getCollectionModel(), this);
        encounterSetView = new EncounterSetView(getModel().getEncounterSetModel(), this);
        portraitView = PortraitView.createWithDefaultImage(getModel().getPortraitModel(), ART_PORTRAIT_DRAW_REGION.toPixelRectangle(CardFaceViewUtils.HARDCODED_DPI).getSize());
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
    protected BufferedImage getTemplateImage() {
        return ImageUtils.loadImage("/templates/act_agenda/act.png");
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

        EditorUtils.bindTextComponent(actNumberEditor, editorContext.wrapConsumerWithMarkedChanged(getModel()::setActNumber));
        EditorUtils.bindTextComponent(deckIdEditor, editorContext.wrapConsumerWithMarkedChanged(getModel()::setDeckId));
        EditorUtils.bindStatisticComponent(cluesEditor, editorContext.wrapConsumerWithMarkedChanged(getModel()::setClues));

        actNumberEditor.setText(getModel().getActNumber());
        deckIdEditor.setText(getModel().getDeckId());
        cluesEditor.setStatistic(getModel().getClues());

        layoutMainTab(editorContext);

        CardFaceViewUtils.createEncounterSetCollectionTab(editorContext, encounterSetView, collectionView);
    }

    private void layoutMainTab(EditorContext editorContext) {
        JPanel titlePanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.TITLE));
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, false, false);

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

    private static final RectangleEx SCENARIO_INDEX_DRAW_REGION = RectangleEx.millimeters(13.04, 2.37, 21.00, 3.56);
    private static final RectangleEx TITLE_DRAW_REGION = RectangleEx.millimeters(3.39, 9.99, 40.64, 7.45);
    private static final RectangleEx BODY_DRAW_REGION = RectangleEx.millimeters(2.54, 17.61, 44.70, 38.44);
    private static final RectangleEx ENCOUNTER_PORTRAIT_DRAW_REGION = RectangleEx.millimeters(20.83, 5.00, 5.42, 5.42);
    private static final RectangleEx CLUES_DRAW_REGION = RectangleEx.millimeters(44.53, 51.39, 0.00, 3.39);

    private static final PageShape BODY_PAGE_SHAPE = MarkupUtils.createStraightLinePathingPageShape(BODY_DRAW_REGION.toPixelRectangle(CardFaceViewUtils.HARDCODED_DPI), Lists.newArrayList(
            new Point2D.Double(0.0, 0.0),
            new Point2D.Double(0.0, 1.00),
            new Point2D.Double(0.715, 1.00),
            new Point2D.Double(0.83, 0.957),
            new Point2D.Double(0.83, 0.82),
            new Point2D.Double(1.0, 0.82),
            new Point2D.Double(1.0, 0.0)
    ));

    @Override
    public void paint(PaintContext paintContext) {
        // paint the main/art portrait first as it sits behind the card template
        portraitView.paintArtPortrait(paintContext, paintContext.toPixelRect(ART_PORTRAIT_DRAW_REGION));

        // draw the template
        paintContext.getGraphics().drawImage(getTemplateImage(), 0, 0, null);

        // title
        commonCardFieldsView.paintTitleMultiline(paintContext, paintContext.toPixelRect(TITLE_DRAW_REGION));

        encounterSetView.paintEncounterNumbers(paintContext, CardFaceOrientation.Landscape);
        encounterSetView.paintEncounterPortrait(paintContext, paintContext.toPixelRect(ENCOUNTER_PORTRAIT_DRAW_REGION));

        portraitView.paintArtist(paintContext);

        collectionView.paintCollectionImage(paintContext, CardFaceOrientation.Landscape, true);
        collectionView.paintCollectionNumber(paintContext, CardFaceOrientation.Landscape);

        actCommonFieldsView.paintBody(paintContext, paintContext.toPixelRect(BODY_DRAW_REGION), BODY_PAGE_SHAPE);

        PaintUtils.paintStatistic(paintContext, paintContext.toPixelRect(CLUES_DRAW_REGION), getModel().getClues(), Color.BLACK, PaintUtils.STATISTIC_LIGHT_TEXT_COLOUR);

        PaintUtils.paintScenarioIndex(paintContext, paintContext.toPixelRect(SCENARIO_INDEX_DRAW_REGION), Language.gstring(GameConstants.LABEL_ACT), getModel().getActNumber(), getModel().getDeckId());
    }
}
