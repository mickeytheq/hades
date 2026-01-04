package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.layout.MarkupRenderer;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.cardfaces.ScenarioReference;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.common.*;
import com.mickeytheq.hades.core.view.utils.*;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

@View(interfaceLanguageKey = InterfaceConstants.ENEMY)
public class ScenarioReferenceView extends BaseCardFaceView<ScenarioReference> implements HasEncounterSetView, HasCollectionView {
    private CommonCardFieldsView commonCardFieldsView;
    private EncounterSetView encounterSetView;
    private CollectionView collectionView;
    private SymbolChaosTokenInfoView skullView;
    private SymbolChaosTokenInfoView cultistView;
    private SymbolChaosTokenInfoView tabletView;
    private SymbolChaosTokenInfoView elderThingView;
    private JTextField trackingBoxEditor;

    @Override
    public void initialiseView() {
        super.initialiseView();

        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel());
        encounterSetView = new EncounterSetView(getModel().getEncounterSetModel(), this);
        collectionView = new CollectionView(getModel().getCollectionModel(), this);
        skullView = new SymbolChaosTokenInfoView(ScenarioReference.SymbolChaosToken.Skull, getModel().getSkull());
        cultistView = new SymbolChaosTokenInfoView(ScenarioReference.SymbolChaosToken.Cultist, getModel().getCultist());
        tabletView = new SymbolChaosTokenInfoView(ScenarioReference.SymbolChaosToken.Tablet, getModel().getTablet());
        elderThingView = new SymbolChaosTokenInfoView(ScenarioReference.SymbolChaosToken.ElderThing, getModel().getElderThing());
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
        return ImageUtils.loadImage("/templates/scenario/scenario_reference.png");
    }

    @Override
    public String getTitle() {
        return StringUtils.defaultIfEmpty(getModel().getCommonCardFieldsModel().getTitle(), null);
    }

    @Override
    public void createEditors(EditorContext editorContext) {
        commonCardFieldsView.createEditors(editorContext);

        // title
        JPanel titlePanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.TITLE));
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, false, false);

        // rules/chaos token effects
        JPanel rulesPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.RULES));
        skullView.populatePanel(rulesPanel, editorContext);
        cultistView.populatePanel(rulesPanel, editorContext);
        tabletView.populatePanel(rulesPanel, editorContext);
        elderThingView.populatePanel(rulesPanel, editorContext);

        // tracking
        JPanel trackingPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.TRACKERBOX));
        trackingBoxEditor = EditorUtils.createTextField(30);
        EditorUtils.bindTextComponent(trackingBoxEditor, editorContext.wrapConsumerWithMarkedChanged(getModel()::setTrackingBox));
        getModel().setTrackingBox(trackingBoxEditor.getText());

        MigLayoutUtils.addLabelledComponentWrapGrowPush(trackingPanel, Language.string(InterfaceConstants.TRACKERBOX), trackingBoxEditor);

        // copyright
        JPanel copyrightPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.COPYRIGHT));
        commonCardFieldsView.addCopyrightEditorToPanel(copyrightPanel);

        // main panel
        JPanel mainPanel = MigLayoutUtils.createVerticalFlowOrganiserPanel(titlePanel, rulesPanel, trackingPanel, copyrightPanel);
        editorContext.addDisplayComponent(Language.string(InterfaceConstants.GENERAL), mainPanel);

        encounterSetView.createEditors(editorContext);
        collectionView.createEditors(editorContext);
        CardFaceViewUtils.createEncounterSetCollectionTab(editorContext, encounterSetView, collectionView);
    }

    private static final Rectangle TITLE_DRAW_REGION = new Rectangle(56, 180, 638, 150);
    private static final Rectangle DIFFICULTY_DRAW_REGION = new Rectangle(258, 254, 228, 28);
    private static final Rectangle BODY_DRAW_REGION = new Rectangle(190, 312, 480, 610);
    private static final Rectangle ENCOUNTER_PORTRAIT_DRAW_REGION = new Rectangle(345, 124, 64, 64);
    private static final Rectangle TRACKING_BOX_DRAW_REGION = new Rectangle(64, 742, 612, 184);
    private static final Rectangle TRACKING_TITLE_DRAW_REGION = new Rectangle(88, 750, 560, 40);

    @Override
    public void paint(PaintContext paintContext) {
        paintContext.getGraphics().drawImage(getTemplateImage(), 0, 0, null);

        // title needs special handling as it is allowed to spill onto a second line
        // set the alignment to the top so a short title will stay on one line
        double titleEndedAtYPosition = PaintUtils.paintTitle(paintContext, TITLE_DRAW_REGION, getModel().getCommonCardFieldsModel().getTitle(), false,
                MarkupRenderer.LAYOUT_CENTER | MarkupRenderer.LAYOUT_TOP, true);

        // if the title overspilled then move everything else down
        int yDelta = Math.max(0, (int)Math.round(titleEndedAtYPosition - DIFFICULTY_DRAW_REGION.getY()));

        Rectangle difficultyDrawRegion = DIFFICULTY_DRAW_REGION.getBounds();
        difficultyDrawRegion.translate(0, yDelta);
        paintDifficulty(paintContext, difficultyDrawRegion);

        // calculate the body region
        // the Y starts just after the difficulty, which may have moved from its default location above so calculate
        // the Y ends just before the tracking box, if it exists, otherwise the default
        double bodyYStart = (difficultyDrawRegion.getY() + difficultyDrawRegion.getHeight());
        double bodyYEnd = BODY_DRAW_REGION.getMaxY();

        if (!StringUtils.isEmpty(getModel().getTrackingBox())) {
            bodyYEnd = TRACKING_BOX_DRAW_REGION.getY();
        }

        Rectangle bodyDrawRegion = new Rectangle((int)BODY_DRAW_REGION.getX(), (int)bodyYStart, (int)BODY_DRAW_REGION.getWidth(), (int)(bodyYEnd - bodyYStart));

        paintBody(paintContext, bodyDrawRegion);

        paintTrackingBox(paintContext);

        encounterSetView.paintEncounterPortrait(paintContext, ENCOUNTER_PORTRAIT_DRAW_REGION);
        encounterSetView.paintEncounterNumbers(paintContext, CardFaceOrientation.Portrait);
        collectionView.paintCollectionImage(paintContext, CardFaceOrientation.Portrait, true);
        collectionView.paintCollectionNumber(paintContext, CardFaceOrientation.Portrait);

        commonCardFieldsView.paintCopyright(paintContext);
    }

    private Map<ScenarioReference.SymbolChaosTokenInfo, List<ScenarioReference.SymbolChaosToken>> combineTokens() {
        Map<ScenarioReference.SymbolChaosTokenInfo, List<ScenarioReference.SymbolChaosToken>> combinedMap = new LinkedHashMap<>();

        Set<ScenarioReference.SymbolChaosToken> handled = new HashSet<>();

        Map<ScenarioReference.SymbolChaosToken, ScenarioReference.SymbolChaosTokenInfo> tokenMap = new LinkedHashMap<>();
        tokenMap.put(ScenarioReference.SymbolChaosToken.Skull, getModel().getSkull());
        tokenMap.put(ScenarioReference.SymbolChaosToken.Cultist, getModel().getCultist());
        tokenMap.put(ScenarioReference.SymbolChaosToken.Tablet, getModel().getTablet());
        tokenMap.put(ScenarioReference.SymbolChaosToken.ElderThing, getModel().getElderThing());

        // iterate the possible tokens/info and build a reverse map with combinations resolved
        for (Map.Entry<ScenarioReference.SymbolChaosToken, ScenarioReference.SymbolChaosTokenInfo> entry : tokenMap.entrySet()) {
            ScenarioReference.SymbolChaosToken token = entry.getKey();
            ScenarioReference.SymbolChaosTokenInfo tokenInfo = entry.getValue();

            if (!handled.contains(token)) {
                combinedMap.put(tokenInfo, Lists.newArrayList(token));

                ScenarioReference.SymbolChaosToken combineWith = tokenInfo.getCombineWith();
                if (combineWith != null) {
                    handled.add(combineWith);
                    combinedMap.get(tokenInfo).add(combineWith);
                }
            }
        }

        return combinedMap;
    }

    private static class SymbolChaosTokenInfoView {
        private final ScenarioReference.SymbolChaosToken chaosToken;
        private final ScenarioReference.SymbolChaosTokenInfo symbolChaosTokenInfo;

        public SymbolChaosTokenInfoView(ScenarioReference.SymbolChaosToken chaosToken, ScenarioReference.SymbolChaosTokenInfo symbolChaosTokenInfo) {
            this.chaosToken = chaosToken;
            this.symbolChaosTokenInfo = symbolChaosTokenInfo;
        }

        private void populatePanel(JPanel panel, EditorContext editorContext) {
            // rules text
            JTextArea rulesEditor = EditorUtils.createTextArea(8, 30);
            EditorUtils.bindTextComponent(rulesEditor, editorContext.wrapConsumerWithMarkedChanged(symbolChaosTokenInfo::setRules));
            rulesEditor.setText(symbolChaosTokenInfo.getRules());
            MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, chaosToken.toString(), rulesEditor);

            // elder thing token doesn't have the remaining editors
            if (chaosToken == ScenarioReference.SymbolChaosToken.ElderThing)
                return;

            JComboBox<ScenarioReference.SymbolChaosToken> combineWithEditor = EditorUtils.createNullableComboBox(EditorUtils.DEFAULT_NULL_COMBO_BOX_DISPLAY);

            // deliberate fall-through to build up a list of those tokens below this one
            switch (chaosToken) {
                case Skull:
                    combineWithEditor.addItem(ScenarioReference.SymbolChaosToken.Cultist);
                case Cultist:
                    combineWithEditor.addItem(ScenarioReference.SymbolChaosToken.Tablet);
                case Tablet:
                    combineWithEditor.addItem(ScenarioReference.SymbolChaosToken.ElderThing);
            }

            EditorUtils.bindComboBox(combineWithEditor, editorContext.wrapConsumerWithMarkedChanged(symbolChaosTokenInfo::setCombineWith));
            combineWithEditor.setSelectedItem(symbolChaosTokenInfo.getCombineWith());
            MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, Language.string(InterfaceConstants.COMBINEWITH), combineWithEditor);

            // spacing
            JSpinner afterSpacingEditor = EditorUtils.createSpinnerNonNegative(Integer.MAX_VALUE);
            EditorUtils.bindSpinner(afterSpacingEditor, editorContext.wrapConsumerWithMarkedChanged(symbolChaosTokenInfo::setAfterSpacing));
            afterSpacingEditor.setValue(symbolChaosTokenInfo.getAfterSpacing());
            MigLayoutUtils.addLabelledComponent(panel, Language.string(InterfaceConstants.SPACING), afterSpacingEditor, MigLayoutUtils.SPACING_EDITOR_CONSTRAINTS);
        }
    }

    private void paintDifficulty(PaintContext paintContext, Rectangle drawRegion) {
        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getScenarioReferenceDifficultyTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_CENTER | MarkupRenderer.LAYOUT_TOP);
        markupRenderer.setMarkupText(getModel().getDifficulty().toString());
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), drawRegion);
    }

    private void paintBody(PaintContext paintContext, Rectangle drawRegion) {
        // figure out which tokens are grouped together, if any
        // generate an ordered list of text to render with one or more tokens against each
        // some tokens may be missing if they have no text
        Map<ScenarioReference.SymbolChaosTokenInfo, List<ScenarioReference.SymbolChaosToken>> combinedTokens = combineTokens();

        // chaos tokens = 85 pixels wide/high
        // 90 pixels X position
        MultiSectionRenderer renderer = new MultiSectionRenderer(paintContext, drawRegion);

        for (Map.Entry<ScenarioReference.SymbolChaosTokenInfo, List<ScenarioReference.SymbolChaosToken>> entry : combinedTokens.entrySet()) {
            ScenarioReference.SymbolChaosTokenInfo chaosTokenInfo = entry.getKey();
            List<ScenarioReference.SymbolChaosToken> chaosTokens = entry.getValue();

            if (!renderer.getSections().isEmpty())
                renderer.getSections().add(new MultiSectionRenderer.VerticalSpacerSection(10));

            // chaos tokens are ~7mm wide on the cards
            // and allow a small gap between each token and above and below the total
            int chaosTokenPixelHeight = chaosTokens.size() * paintContext.metricToPixels(7)
                    + paintContext.metricToPixels(1.5) * chaosTokens.size() + 1;

            if (chaosTokens.size() > 1) {
                renderer.getSections().add(new MultiSectionRenderer.DoubleLineInsetTextSection(chaosTokenInfo.getRules(),
                        TextStyleUtils.getBodyTextStyle(), MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_LEFT,
                        paintContext.getRenderingDpi(), chaosTokenPixelHeight, chaosTokenPixelHeight, 10));
            }
            else {
                renderer.getSections().add(new MultiSectionRenderer.TextSection(chaosTokenInfo.getRules(),
                        TextStyleUtils.getBodyTextStyle(), MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_LEFT,
                        paintContext.getRenderingDpi(), chaosTokenPixelHeight, Integer.MAX_VALUE));
            }
        }

        renderer.draw();
    }

    private void paintTrackingBox(PaintContext paintContext) {
        if (StringUtils.isEmpty(getModel().getTrackingBox()))
            return;

        // background image
        PaintUtils.paintBufferedImage(paintContext.getGraphics(), ImageUtils.loadImage("/overlays/scenario_reference/scenario_reference_tracker_box.png"), TRACKING_BOX_DRAW_REGION);

        // title
        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getScenarioReferenceTrackerBoxTitleTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_CENTER | MarkupRenderer.LAYOUT_TOP);
        markupRenderer.setMarkupText(getModel().getTrackingBox());
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), TRACKING_TITLE_DRAW_REGION);
    }
}
