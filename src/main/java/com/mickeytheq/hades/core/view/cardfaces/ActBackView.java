package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.layout.MarkupRenderer;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.codegenerated.GameConstants;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.cardfaces.Act;
import com.mickeytheq.hades.core.model.cardfaces.ActBack;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.view.common.*;
import com.mickeytheq.hades.core.view.utils.*;
import com.mickeytheq.hades.util.shape.RectangleEx;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@View(interfaceLanguageKey = InterfaceConstants.ACT_BACK)
public class ActBackView extends BaseCardFaceView<ActBack> implements HasEncounterSetView {
    private CommonCardFieldsView commonCardFieldsView;
    private StorySectionView section1View;
    private StorySectionView section2View;
    private StorySectionView section3View;
    private EncounterSetView encounterSetView;

    private JTextField actNumberEditor;
    private JTextField deckIdEditor;
    private JCheckBox copyOtherFaceEditor;

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel(), this);
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
    protected List<TemplateInfo> getAvailableTemplateInfos() {
        return Lists.newArrayList(TemplateInfos.createStandard300("/templates/act_agenda/act_back.png", CardFaceOrientation.Landscape));
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

        actNumberEditor = EditorUtils.createTextField(20);
        deckIdEditor = EditorUtils.createTextField(20);
        copyOtherFaceEditor = EditorUtils.createCheckBox();

        EditorUtils.bindTextComponent(actNumberEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getActFieldsModel()::setNumber));
        EditorUtils.bindTextComponent(deckIdEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getActFieldsModel()::setDeckId));
        EditorUtils.bindToggleButton(copyOtherFaceEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getActFieldsModel()::setCopyOtherFace));

        actNumberEditor.setText(getModel().getActFieldsModel().getNumber());
        deckIdEditor.setText(getModel().getActFieldsModel().getDeckId());
        copyOtherFaceEditor.setSelected(getModel().getActFieldsModel().isCopyOtherFace());

        copyOtherFaceEditor.addChangeListener(e -> {
            boolean shadowing = copyOtherFaceEditor.isSelected();

            actNumberEditor.setEnabled(!shadowing);
            deckIdEditor.setEnabled(!shadowing);
        });

        layoutMainTab(editorContext);

        CardFaceViewUtils.createEncounterSetCollectionTab(editorContext, encounterSetView, null);
    }

    private void layoutMainTab(EditorContext editorContext) {
        JPanel titlePanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.TITLE));
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, false, false, false);

        JPanel statsPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.ACT));
        MigLayoutUtils.addLabelledComponentWrapGrowPush(statsPanel, Language.string(InterfaceConstants.SHADOW_FRONT), copyOtherFaceEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(statsPanel, Language.string(InterfaceConstants.ACTNUMBER), actNumberEditor);
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

    private static final RectangleEx SCENARIO_INDEX_DRAW_REGION = RectangleEx.millimetres(4.91, 5.50, 6.94, 2.71);
    private static final RectangleEx TITLE_DRAW_REGION = RectangleEx.millimetres(5.00, 17.78, 6.77, 37.42);
    private static final RectangleEx BODY_DRAW_REGION = RectangleEx.millimetres(18.63, 6.60, 62.65, 52.49);
    private static final RectangleEx ENCOUNTER_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(5.55, 9.65, 5.5, 5.5);

    @Override
    public void paint(PaintContext paintContext) {
        // draw the template
        paintContext.paintTemplate();

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
        // if shadowing the front then look for an Act on the other face. if missing do nothing
        if (getModel().getActFieldsModel().isCopyOtherFace()) {
            Optional<Act> otherFaceModel = getOtherFaceView().filter(o -> o instanceof ActView).map(o -> ((ActView)o).getModel());

            if (otherFaceModel.isPresent()) {
                Act act = otherFaceModel.get();

                // copy the number from the front and for the deck id take the first character and increment it by 1, e.g. 'a' -> 'b'
                // deck id should only be one character in most cases but preserve the rest of the string if there is anything
                String newDeckId = (char)(act.getActFieldsModel().getDeckId().charAt(0) + 1) + StringUtils.substring(act.getActFieldsModel().getDeckId(), 1);
                PaintUtils.paintScenarioIndexBack(paintContext, paintContext.toPixelRect(SCENARIO_INDEX_DRAW_REGION), Language.gstring(GameConstants.LABEL_ACT), act.getActFieldsModel().getNumber(), newDeckId);
            }

            return;
        }

        PaintUtils.paintScenarioIndexBack(paintContext, paintContext.toPixelRect(SCENARIO_INDEX_DRAW_REGION), Language.gstring(GameConstants.LABEL_ACT), getModel().getActFieldsModel().getNumber(), getModel().getActFieldsModel().getDeckId());
    }
}
