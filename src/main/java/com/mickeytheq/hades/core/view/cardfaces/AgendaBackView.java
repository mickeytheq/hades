package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.hades.codegenerated.GameConstants;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.cardfaces.Agenda;
import com.mickeytheq.hades.core.model.cardfaces.AgendaBack;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.common.*;
import com.mickeytheq.hades.core.view.utils.*;
import com.mickeytheq.hades.util.shape.RectangleEx;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.function.Supplier;

@View(interfaceLanguageKey = InterfaceConstants.AGENDA_BACK)
public class AgendaBackView extends BaseCardFaceView<AgendaBack> implements HasEncounterSetView {
    private CommonCardFieldsView commonCardFieldsView;
    private StorySectionView section1View;
    private StorySectionView section2View;
    private StorySectionView section3View;
    private EncounterSetView encounterSetView;

    private JTextField agendaNumberEditor;
    private JTextField deckIdEditor;
    private JCheckBox shadowFrontEditor;

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel());
        section1View = new StorySectionView(getModel().getSection1());
        section2View = new StorySectionView(getModel().getSection2());
        section3View = new StorySectionView(getModel().getSection3());
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

    private static final RectangleEx SCENARIO_INDEX_DRAW_REGION = RectangleEx.millimeters(4.91, 5.50, 6.94, 2.71);
    private static final RectangleEx TITLE_DRAW_REGION = RectangleEx.millimeters(5.00, 17.78, 6.77, 37.42);
    private static final RectangleEx BODY_DRAW_REGION = RectangleEx.millimeters(18.63, 6.60, 62.65, 52.49);
    private static final RectangleEx ENCOUNTER_PORTRAIT_DRAW_REGION = RectangleEx.millimeters(5.25, 9.65, 6.10, 6.10);

    @Override
    public void paint(PaintContext paintContext) {
        // draw the template
        paintContext.getGraphics().drawImage(getTemplateImage(), 0, 0, null);

        // title - vertical orientation
        commonCardFieldsView.paintTitleMultilineRotated(paintContext, paintContext.toPixelRect(TITLE_DRAW_REGION));

        encounterSetView.paintEncounterNumbers(paintContext, CardFaceOrientation.Landscape);
        encounterSetView.paintEncounterPortrait(paintContext, paintContext.toPixelRect(ENCOUNTER_PORTRAIT_DRAW_REGION));

        paintScenarioIndex(paintContext);

        // do the body back of the card that has multiple sections with different layout requirements
        // use the multi-section renderer to handle the dynamic scaling
        MultiSectionRenderer multiSectionRenderer = new MultiSectionRenderer(paintContext, paintContext.toPixelRect(BODY_DRAW_REGION));
        CardFaceViewUtils.buildStorySections(paintContext, multiSectionRenderer, section1View, section2View, section3View);

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

    private void paintScenarioIndex(PaintContext paintContext) {
        // if shadowing the front then look for an Agenda on the other face. if missing do nothing
        if (getModel().isShadowFront()) {
            Optional<Agenda> otherFaceAgenda = getOtherFaceView().filter(o -> o instanceof AgendaView).map(o -> ((AgendaView)o).getModel());

            if (otherFaceAgenda.isPresent()) {
                Agenda agenda = otherFaceAgenda.get();

                // copy the number from the front and for the deck id take the first character and increment it by 1, e.g. 'a' -> 'b'
                // deck id should only be one character in most cases but preserve the rest of the string if there is anything
                String newDeckId = (char)(agenda.getDeckId().charAt(0) + 1) + StringUtils.substring(agenda.getDeckId(), 1);
                PaintUtils.paintScenarioIndexBack(paintContext, paintContext.toPixelRect(SCENARIO_INDEX_DRAW_REGION), Language.gstring(GameConstants.LABEL_AGENDA), agenda.getAgendaNumber(), newDeckId);
            }

            return;
        }

        PaintUtils.paintScenarioIndexBack(paintContext, paintContext.toPixelRect(SCENARIO_INDEX_DRAW_REGION), Language.gstring(GameConstants.LABEL_AGENDA), getModel().getAgendaNumber(), getModel().getDeckId());
    }
}
