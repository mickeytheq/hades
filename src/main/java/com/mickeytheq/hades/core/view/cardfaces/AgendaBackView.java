package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.layout.MarkupRenderer;
import ca.cgjennings.layout.PageShape;
import ca.cgjennings.layout.TextStyle;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.codegenerated.GameConstants;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.cardfaces.Agenda;
import com.mickeytheq.hades.core.model.cardfaces.AgendaBack;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.common.*;
import com.mickeytheq.hades.core.view.utils.*;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Optional;

@View(interfaceLanguageKey = InterfaceConstants.AGENDA_BACK)
public class AgendaBackView extends BaseCardFaceView<AgendaBack> implements HasEncounterSetView {
    private CommonCardFieldsView commonCardFieldsView;
    private ActAgendaCommonFieldsView section1View;
    private ActAgendaCommonFieldsView section2View;
    private ActAgendaCommonFieldsView section3View;
    private EncounterSetView encounterSetView;

    private JTextField agendaNumberEditor;
    private JTextField deckIdEditor;
    private JCheckBox shadowFrontEditor;

    @Override
    public void initialiseView() {
        super.initialiseView();

        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel());
        section1View = new ActAgendaCommonFieldsView(getModel().getSection1());
        section2View = new ActAgendaCommonFieldsView(getModel().getSection2());
        section3View = new ActAgendaCommonFieldsView(getModel().getSection3());
        encounterSetView = new EncounterSetView(getModel().getEncounterSetModel(), this);
    }

    @Override
    public EncounterSetView getEncounterSetView() {
        return encounterSetView;
    }

    @Override
    protected BufferedImage getTemplateImage() {
        return ImageUtils.loadImage("/templates/act_agenda/agenda_back.png");
    }

    @Override
    public String getTitle() {
        return StringUtils.defaultIfEmpty(getModel().getCommonCardFieldsModel().getTitle(), null);
    }

    @Override
    public void createEditors(EditorContext editorContext) {
        commonCardFieldsView.createEditors(editorContext);
        section1View.createEditors(editorContext);
        section2View.createEditors(editorContext);
        section3View.createEditors(editorContext);
        encounterSetView.createEditors(editorContext);

        agendaNumberEditor = EditorUtils.createTextField(20);
        deckIdEditor = EditorUtils.createTextField(20);
        shadowFrontEditor = EditorUtils.createCheckBox();

        EditorUtils.bindTextComponent(agendaNumberEditor, editorContext.wrapConsumerWithMarkedChanged(getModel()::setAgendaNumber));
        EditorUtils.bindTextComponent(deckIdEditor, editorContext.wrapConsumerWithMarkedChanged(getModel()::setDeckId));
        EditorUtils.bindToggleButton(shadowFrontEditor, editorContext.wrapConsumerWithMarkedChanged(getModel()::setShadowFront));

        agendaNumberEditor.setText(getModel().getAgendaNumber());
        deckIdEditor.setText(getModel().getDeckId());
        shadowFrontEditor.setSelected(getModel().isShadowFront());

        shadowFrontEditor.addChangeListener(e -> {
            boolean shadowing = shadowFrontEditor.isSelected();

            agendaNumberEditor.setEnabled(!shadowing);
            deckIdEditor.setEnabled(!shadowing);
        });

        layoutMainTab(editorContext);

        CardFaceViewUtils.createEncounterSetCollectionTab(editorContext, encounterSetView, null);
    }

    private void layoutMainTab(EditorContext editorContext) {
        JPanel titlePanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.TITLE));
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, false, false);

        JPanel statsPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.AGENDA));
        MigLayoutUtils.addLabelledComponentWrapGrowPush(statsPanel, Language.string(InterfaceConstants.SHADOW_FRONT), shadowFrontEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(statsPanel, Language.string(InterfaceConstants.AGENDANUMBER), agendaNumberEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(statsPanel, Language.string(InterfaceConstants.SCENARIODECKID), deckIdEditor);

        JPanel section1Panel = section1View.createPanel(true);
        JPanel section2Panel = section2View.createPanel(true);
        JPanel section3Panel = section3View.createPanel(true);

        JPanel victoryPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.VICTORY));
        commonCardFieldsView.addVictoryEditorsToPanel(victoryPanel);

        JPanel mainPanel = MigLayoutUtils.createVerticalFlowOrganiserPanel(
                titlePanel, statsPanel, section1Panel, section2Panel, section3Panel, victoryPanel
        );

        editorContext.addDisplayComponent(Language.string(InterfaceConstants.GENERAL), mainPanel);
    }

    private static final Rectangle SCENARIO_INDEX_DRAW_REGION = new Rectangle(58, 65, 82, 32);
    private static final Rectangle TITLE_DRAW_REGION = new Rectangle(59, 210, 80, 442);
    private static final Rectangle BODY_DRAW_REGION = new Rectangle(220, 78, 740, 620);
    private static final Rectangle ENCOUNTER_PORTRAIT_DRAW_REGION = new Rectangle(62, 114, 72, 72);

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
        // draw the template
        paintContext.getGraphics().drawImage(getTemplateImage(), 0, 0, null);

        // title
        AffineTransform oldTransform = paintContext.getGraphics().getTransform();
        try {
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.rotate(-Math.PI / 2);
            paintContext.getGraphics().setTransform(affineTransform);
            commonCardFieldsView.paintTitleMultiline(paintContext, TITLE_DRAW_REGION);
        }
        finally {
            paintContext.getGraphics().setTransform(oldTransform);
        }

        encounterSetView.paintEncounterNumbers(paintContext, CardFaceOrientation.Landscape);
        encounterSetView.paintEncounterPortrait(paintContext, ENCOUNTER_PORTRAIT_DRAW_REGION);

        paintScenarioIndex(paintContext);

        // do the body back of the card that has multiple sections with different layout requirements
        // use the multi-section renderer to handle the dynamic scaling
        MultiSectionRenderer multiSectionRenderer = new MultiSectionRenderer(paintContext, BODY_DRAW_REGION);
        buildSections(paintContext, multiSectionRenderer, section1View, section2View, section3View);

        String victory = getModel().getCommonCardFieldsModel().getVictory();

        if (!StringUtils.isEmpty(victory)) {
            multiSectionRenderer.getSections().add(
                    new MultiSectionRenderer.TextSection(getModel().getCommonCardFieldsModel().getVictory(),
                            TextStyleUtils.getVictoryTextStyle(), MarkupRenderer.LAYOUT_CENTER, paintContext.getRenderingDpi()));
        }

        multiSectionRenderer.draw();
    }

    private void paintScenarioIndex(PaintContext paintContext) {
        // if shadowing the front then look for an Agenda on the other face. if missing do nothing
        if (getModel().isShadowFront()) {
            Optional<Agenda> otherFaceAgenda = getOtherFaceView().filter(o -> o instanceof AgendaView).map(o -> ((AgendaView)o).getModel());

            if (otherFaceAgenda.isPresent()) {
                Agenda agenda = otherFaceAgenda.get();

                // copy the number from the front and for the deck id take the first character and increment it by 1, e.g. 'a' -> 'b'
                // deck id should only be one character in most cases but preserve the rest of the string if there is anything
                String newDeckId = (char)(agenda.getDeckId().charAt(0) + 1) + StringUtils.substring(agenda.getDeckId(), 1);
                PaintUtils.paintScenarioIndexBack(paintContext, SCENARIO_INDEX_DRAW_REGION, Language.gstring(GameConstants.LABEL_AGENDA), agenda.getAgendaNumber(), newDeckId);
            }

            return;
        }

        PaintUtils.paintScenarioIndexBack(paintContext, SCENARIO_INDEX_DRAW_REGION, Language.gstring(GameConstants.LABEL_AGENDA), getModel().getAgendaNumber(), getModel().getDeckId());
    }

    // build the header/story/rules sections with appropriate gaps/lines between them
    private void buildSections(PaintContext paintContext, MultiSectionRenderer renderer, ActAgendaCommonFieldsView ... sectionViews) {
        boolean needSeparator = false;

        for (ActAgendaCommonFieldsView sectionView : sectionViews) {
            if (sectionView.getModel().isNull())
                continue;

            if (needSeparator) {
                renderer.getSections().add(new MultiSectionRenderer.VerticalSpacerSection(10));
                renderer.getSections().add(new MultiSectionRenderer.HorizontalLineSection(2));
                renderer.getSections().add(new MultiSectionRenderer.VerticalSpacerSection(10));
            }

            paintSection(paintContext, renderer, sectionView);

            needSeparator = true;
        }
    }

    private void paintSection(PaintContext paintContext, MultiSectionRenderer renderer, ActAgendaCommonFieldsView sectionView) {
        if (sectionView.getModel().isNull())
            return;

        String header = sectionView.getModel().getHeader();
        String story = sectionView.getModel().getStory();
        String rules = sectionView.getModel().getRules();

        boolean needSeparator = false;

        if (!StringUtils.isEmpty(header)) {
            renderer.getSections().add(new MultiSectionRenderer.TextSection(header, TextStyleUtils.getHeaderTextStyle(),
                    MarkupRenderer.LAYOUT_LEFT, paintContext.getRenderingDpi()));

            needSeparator = true;
        }

        if (!StringUtils.isEmpty(story)) {
            if (needSeparator)
                renderer.getSections().add(new MultiSectionRenderer.VerticalSpacerSection(10));

            renderer.getSections().add(new MultiSectionRenderer.StoryTextSection(story, TextStyleUtils.getStoryTextStyle(),
                    MarkupRenderer.LAYOUT_LEFT, paintContext.getRenderingDpi(), 20));

            needSeparator = true;
        }

        if (!StringUtils.isEmpty(rules)) {
            if (needSeparator)
                renderer.getSections().add(new MultiSectionRenderer.VerticalSpacerSection(10));

            renderer.getSections().add(new MultiSectionRenderer.TextSection(rules, TextStyleUtils.getBodyTextStyle(),
                    MarkupRenderer.LAYOUT_LEFT, paintContext.getRenderingDpi()));
        }
    }
}
