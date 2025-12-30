package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.layout.PageShape;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.cardfaces.Agenda;
import com.mickeytheq.hades.core.view.BaseCardFaceView;
import com.mickeytheq.hades.core.view.EditorContext;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.View;
import com.mickeytheq.hades.core.view.common.*;
import com.mickeytheq.hades.core.view.component.StatisticComponent;
import com.mickeytheq.hades.core.view.utils.*;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

@View(interfaceLanguageKey = InterfaceConstants.AGENDA)
public class AgendaView extends BaseCardFaceView<Agenda> implements HasNumberingView {
    private CommonCardFieldsView commonCardFieldsView;
    private ActAgendaCommonFieldsView agendaCommonFieldsView;
    private NumberingView numberingView;
    private PortraitWithArtistView portraitWithArtistView;

    private JTextField agendaNumberEditor;
    private JTextField deckIdEditor;
    private StatisticComponent doomEditor;

    private static final Rectangle ART_PORTRAIT_DRAW_REGION = new Rectangle(0, 0, 610, 750);

    @Override
    public void initialiseView() {
        super.initialiseView();

        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel());
        agendaCommonFieldsView = new ActAgendaCommonFieldsView(getModel().getAgendaCommonFieldsModel());
        numberingView = new NumberingView(getModel().getNumberingModel(), this);
        portraitWithArtistView = new PortraitWithArtistView(getModel().getPortraitWithArtistModel(), ART_PORTRAIT_DRAW_REGION.getSize());
    }

    @Override
    public NumberingView getNumberingView() {
        return numberingView;
    }

    @Override
    protected BufferedImage getTemplateImage() {
        return ImageUtils.loadImage("/templates/act_agenda/agenda.png");
    }

    @Override
    public String getTitle() {
        return StringUtils.defaultIfEmpty(getModel().getCommonCardFieldsModel().getTitle(), null);
    }

    @Override
    public void createEditors(EditorContext editorContext) {
        commonCardFieldsView.createEditors(editorContext);
        agendaCommonFieldsView.createEditors(editorContext);
        numberingView.createEditors(editorContext);
        portraitWithArtistView.createEditors(editorContext);

        agendaNumberEditor = EditorUtils.createTextField(20);
        deckIdEditor = EditorUtils.createTextField(20);
        doomEditor = new StatisticComponent();

        EditorUtils.bindTextComponent(agendaNumberEditor, editorContext.wrapConsumerWithMarkedChanged(getModel()::setAgendaNumber));
        EditorUtils.bindTextComponent(deckIdEditor, editorContext.wrapConsumerWithMarkedChanged(getModel()::setDeckId));
        EditorUtils.bindStatisticComponent(doomEditor, editorContext.wrapConsumerWithMarkedChanged(getModel()::setDoom));

        agendaNumberEditor.setText(getModel().getAgendaNumber());
        deckIdEditor.setText(getModel().getDeckId());
        doomEditor.setStatistic(getModel().getDoom());

        layoutMainTab(editorContext);

        JPanel numberingPanel = numberingView.createStandardCollectionEncounterPanel(editorContext);
        editorContext.addDisplayComponent("Collection / encounter", numberingPanel);
    }

    private void layoutMainTab(EditorContext editorContext) {
        JPanel titlePanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.TITLE));
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, false, false);

        JPanel statsPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.AGENDA));
        MigLayoutUtils.addLabelledComponent(statsPanel, Language.string(InterfaceConstants.AGENDANUMBER), agendaNumberEditor, "pushx, growx");
        MigLayoutUtils.addLabelledComponent(statsPanel, Language.string(InterfaceConstants.DOOM), doomEditor, "pushx, growx, wrap");
        MigLayoutUtils.addLabelledComponent(statsPanel, Language.string(InterfaceConstants.SCENARIODECKID), deckIdEditor, "pushx, growx");

        JPanel rulesPanel = agendaCommonFieldsView.createPanel(false);

        JPanel artPanel = portraitWithArtistView.createStandardArtPanel(editorContext);

        JPanel copyrightPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.COPYRIGHT));
        commonCardFieldsView.addCopyrightEditorToPanel(copyrightPanel);

        JPanel mainPanel = MigLayoutUtils.createOrganiserPanel();
        mainPanel.add(titlePanel, "growx, pushx, wrap");
        mainPanel.add(statsPanel, "growx, pushx, wrap");
        mainPanel.add(rulesPanel, "growx, pushx, wrap");
        mainPanel.add(artPanel, "growx, pushx, wrap");
        mainPanel.add(copyrightPanel, "growx, pushx, wrap");

        editorContext.addDisplayComponent(Language.string(InterfaceConstants.GENERAL), mainPanel);
    }

    private static final Rectangle LABEL_DRAW_REGION = new Rectangle(38, 128, 76, 28);
    private static final Rectangle TITLE_DRAW_REGION = new Rectangle(510, 110, 500, 88);
    private static final Rectangle BODY_DRAW_REGION = new Rectangle(500, 200, 528, 474);
    private static final Rectangle ENCOUNTER_PORTRAIT_DRAW_REGION = new Rectangle(728, 59, 64, 64);
    private static final Rectangle COLLECTION_PORTRAIT_DRAW_REGION = new Rectangle(952, 720, 26, 26);
    private static final Rectangle DOOM_DRAW_REGION = new Rectangle(515, 607, 0, 40);

    private static final PageShape BODY_PAGE_SHAPE = MarkupUtils.createStraightLinePathingPageShape(BODY_DRAW_REGION, Lists.newArrayList(
            new Point2D.Double(0.0, 0.0),
            new Point2D.Double(0.0, 0.85),
            new Point2D.Double(0.148, 0.85),
            new Point2D.Double(0.148, 1.0),
            new Point2D.Double(1.0, 1.0),
            new Point2D.Double(1.0, 0.0)
    ));

    @Override
    public void paint(PaintContext paintContext) {
        // paint the main/art portrait first as it sits behind the card template
        portraitWithArtistView.paintArtPortrait(paintContext, ART_PORTRAIT_DRAW_REGION);

        // draw the template
        paintContext.getGraphics().drawImage(getTemplateImage(), 0, 0, null);

        // title
        // TODO: title has a two row option
        commonCardFieldsView.paintTitle(paintContext, TITLE_DRAW_REGION);

        numberingView.paintEncounterNumbers(paintContext);
        numberingView.paintEncounterPortrait(paintContext, ENCOUNTER_PORTRAIT_DRAW_REGION);

        portraitWithArtistView.paintArtist(paintContext);

        numberingView.paintCollectionPortrait(paintContext, COLLECTION_PORTRAIT_DRAW_REGION, true);
        numberingView.paintCollectionNumber(paintContext);

        agendaCommonFieldsView.paintBody(paintContext, BODY_DRAW_REGION, BODY_PAGE_SHAPE);

        PaintUtils.paintStatistic(paintContext, DOOM_DRAW_REGION, getModel().getDoom(), Color.BLACK, PaintUtils.LIGHT_TEXT_COLOUR);
    }
}
