package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.layout.PageShape;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.codegenerated.GameConstants;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.cardfaces.Agenda;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.view.PaintContext;
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

@View(interfaceLanguageKey = InterfaceConstants.AGENDA)
public class AgendaView extends BaseCardFaceView<Agenda> implements HasCollectionView, HasEncounterSetView {
    private CommonCardFieldsView commonCardFieldsView;
    private StorySectionView agendaCommonFieldsView;
    private EncounterSetView encounterSetView;
    private CollectionView collectionView;
    private PortraitView portraitView;

    private JTextField agendaNumberEditor;
    private JTextField deckIdEditor;
    private StatisticComponent doomEditor;

    private static final RectangleEx ART_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(0.00, 0.00, 51.65, 63.50);

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel(), this);
        agendaCommonFieldsView = new StorySectionView(getModel().getStorySectionModel());
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
        return ImageUtils.loadImageReadOnly("/templates/act_agenda/agenda.png");
    }

    @Override
    public String getTitle() {
        return StringUtils.defaultIfEmpty(getModel().getCommonCardFieldsModel().getTitle(), null);
    }

    @Override
    public void createEditors(EditorContext editorContext) {
        commonCardFieldsView.createEditors(editorContext);
        agendaCommonFieldsView.createEditors(editorContext);
        collectionView.createEditors(editorContext);
        encounterSetView.createEditors(editorContext);
        portraitView.createEditors(editorContext);

        agendaNumberEditor = EditorUtils.createTextField(20);
        deckIdEditor = EditorUtils.createTextField(20);
        doomEditor = new StatisticComponent();

        EditorUtils.bindTextComponent(agendaNumberEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getAgendaFieldsModel()::setNumber));
        EditorUtils.bindTextComponent(deckIdEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getAgendaFieldsModel()::setDeckId));
        EditorUtils.bindStatisticComponent(doomEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getAgendaFieldsModel()::setDoom));

        agendaNumberEditor.setText(getModel().getAgendaFieldsModel().getNumber());
        deckIdEditor.setText(getModel().getAgendaFieldsModel().getDeckId());
        doomEditor.setStatistic(getModel().getAgendaFieldsModel().getDoom());

        layoutMainTab(editorContext);

        CardFaceViewUtils.createEncounterSetCollectionTab(editorContext, encounterSetView, collectionView);
    }

    private void layoutMainTab(EditorContext editorContext) {
        JPanel titlePanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.TITLE));
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, false, false, false);

        JPanel statsPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.AGENDA));
        MigLayoutUtils.addLabelledComponent(statsPanel, Language.string(InterfaceConstants.AGENDANUMBER), agendaNumberEditor, "pushx, growx");
        MigLayoutUtils.addLabelledComponent(statsPanel, Language.string(InterfaceConstants.DOOM), doomEditor, "pushx, growx, wrap");
        MigLayoutUtils.addLabelledComponent(statsPanel, Language.string(InterfaceConstants.SCENARIODECKID), deckIdEditor, "pushx, growx");

        JPanel rulesPanel = agendaCommonFieldsView.createPanel(false);

        JPanel artPanel = portraitView.createStandardArtPanel(editorContext);

        JPanel copyrightPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.COPYRIGHT));
        commonCardFieldsView.addCopyrightEditorToPanel(copyrightPanel);

        JPanel mainPanel = MigLayoutUtils.createVerticalFlowOrganiserPanel(
                titlePanel, statsPanel, rulesPanel, artPanel, copyrightPanel
        );

        editorContext.addDisplayComponent(Language.string(InterfaceConstants.GENERAL), mainPanel);
    }

    private static final RectangleEx SCENARIO_INDEX_DRAW_REGION = RectangleEx.millimetres(53.34, 2.37, 21.00, 3.56);
    private static final RectangleEx TITLE_DRAW_REGION = RectangleEx.millimetres(43.18, 9.99, 42.33, 7.45);
    private static final RectangleEx BODY_DRAW_REGION = RectangleEx.millimetres(42.33, 17.61, 44.70, 40.13);
    private static final RectangleEx COPYRIGHT_DRAW_REGION = RectangleEx.millimetres(64.00, 61.38, 14.10, PaintConstants.FOOTER_TEXT_HEIGHT_MMS);
    private static final RectangleEx ARTIST_DRAW_REGION = RectangleEx.millimetres(39.00, 61.38, 14.10, PaintConstants.FOOTER_TEXT_HEIGHT_MMS);
    private static final RectangleEx ENCOUNTER_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(61.64, 5.00, 5.0, 5.0);
    private static final RectangleEx DOOM_DRAW_REGION = RectangleEx.millimetres(43.60, 51.39, 0.00, 3.39);

    private static final PageShape BODY_PAGE_SHAPE = MarkupUtils.createStraightLinePathingPageShape(BODY_DRAW_REGION.toPixelRectangle(CardFaceViewUtils.HARDCODED_DPI), Lists.newArrayList(
            new Point2D.Double(0.0, 0.0),
            new Point2D.Double(0.0, 0.80),
            new Point2D.Double(0.148, 0.80),
            new Point2D.Double(0.148, 1.0),
            new Point2D.Double(1.0, 1.0),
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

        portraitView.paintArtist(paintContext, paintContext.toPixelRect(ARTIST_DRAW_REGION));

        collectionView.paintCollectionImage(paintContext, CardFaceOrientation.Landscape, true);
        collectionView.paintCollectionNumber(paintContext, CardFaceOrientation.Landscape);

        agendaCommonFieldsView.paintBody(paintContext, paintContext.toPixelRect(BODY_DRAW_REGION), BODY_PAGE_SHAPE);

        commonCardFieldsView.paintCopyright(paintContext, paintContext.toPixelRect(COPYRIGHT_DRAW_REGION));

        PaintUtils.paintStatistic(paintContext, paintContext.toPixelRect(DOOM_DRAW_REGION), getModel().getAgendaFieldsModel().getDoom(), Color.BLACK, PaintUtils.STATISTIC_LIGHT_TEXT_COLOUR);

        PaintUtils.paintScenarioIndex(paintContext, paintContext.toPixelRect(SCENARIO_INDEX_DRAW_REGION), Language.gstring(GameConstants.LABEL_AGENDA), getModel().getAgendaFieldsModel().getNumber(), getModel().getAgendaFieldsModel().getDeckId());
    }
}
