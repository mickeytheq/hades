package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.layout.MarkupRenderer;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.cardfaces.ScenarioReference;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.common.*;
import com.mickeytheq.hades.core.view.utils.*;
import com.mickeytheq.hades.util.shape.RectangleEx;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;

@View(interfaceLanguageKey = InterfaceConstants.SCENARIO_REFERENCE)
public class ScenarioReferenceView extends BaseCardFaceView<ScenarioReference> implements HasEncounterSetView, HasCollectionView {
    private CommonCardFieldsView commonCardFieldsView;
    private EncounterSetView encounterSetView;
    private CollectionView collectionView;
    private JComboBox<ScenarioReference.Difficulty> difficultyEditor;
    private SymbolChaosTokenInfoView skullView;
    private SymbolChaosTokenInfoView cultistView;
    private SymbolChaosTokenInfoView tabletView;
    private SymbolChaosTokenInfoView elderThingView;
    private JTextField trackingBoxEditor;

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel(), this);
        encounterSetView = new EncounterSetView(getModel().getEncounterSetModel(), this);
        collectionView = new CollectionView(getModel().getCollectionModel(), this);
        skullView = new SymbolChaosTokenInfoView(ScenarioReference.SymbolChaosToken.Skull, getModel().getScenarioReferenceFieldsModel().getSkull());
        cultistView = new SymbolChaosTokenInfoView(ScenarioReference.SymbolChaosToken.Cultist, getModel().getScenarioReferenceFieldsModel().getCultist());
        tabletView = new SymbolChaosTokenInfoView(ScenarioReference.SymbolChaosToken.Tablet, getModel().getScenarioReferenceFieldsModel().getTablet());
        elderThingView = new SymbolChaosTokenInfoView(ScenarioReference.SymbolChaosToken.ElderThing, getModel().getScenarioReferenceFieldsModel().getElderThing());
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
        return Lists.newArrayList(TemplateInfos.createStandard300("/templates/scenario/scenario_reference.png", CardFaceOrientation.Portrait));
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
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, false, false, getCardFaceSide() == CardFaceSide.Back);

        // difficulty
        difficultyEditor = EditorUtils.createEnumComboBox(ScenarioReference.Difficulty.class);
        EditorUtils.bindComboBox(difficultyEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getScenarioReferenceFieldsModel()::setDifficulty));
        difficultyEditor.setSelectedItem(getModel().getScenarioReferenceFieldsModel().getDifficulty());

        // rules/chaos token effects
        JPanel rulesPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.RULES));
        MigLayoutUtils.addLabelledComponentWrapGrowPush(rulesPanel, Language.string(InterfaceConstants.DIFFICULTY), difficultyEditor);
        skullView.populatePanel(rulesPanel, editorContext);
        cultistView.populatePanel(rulesPanel, editorContext);
        tabletView.populatePanel(rulesPanel, editorContext);
        elderThingView.populatePanel(rulesPanel, editorContext);

        // tracking
        JPanel trackingPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.TRACKERBOX));
        trackingBoxEditor = EditorUtils.createTextField(30);
        EditorUtils.bindTextComponent(trackingBoxEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getScenarioReferenceFieldsModel()::setTrackingBox));
        trackingBoxEditor.setText(getModel().getScenarioReferenceFieldsModel().getTrackingBox());

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

    private static final RectangleEx TITLE_DRAW_REGION = RectangleEx.millimetres(6.74, 15.66, 50.02, 8.47);
    private static final RectangleEx DIFFICULTY_DRAW_REGION = RectangleEx.millimetres(21.84, 21.51, 19.30, 2.37);
    private static final RectangleEx BODY_DRAW_REGION = RectangleEx.millimetres(16.51, 26.42, 40.64, 51.65);
    private static final RectangleEx ENCOUNTER_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(29.51, 10.50, 5.0, 5.0);
    private static final RectangleEx TRACKING_BOX_DRAW_REGION = RectangleEx.millimetres(5.42, 62.82, 51.82, 15.58);
    private static final RectangleEx TRACKING_TITLE_DRAW_REGION = RectangleEx.millimetres(7.45, 63.50, 47.41, 3.39);

    @Override
    public void paint(PaintContext paintContext) {
        paintContext.paintTemplate();

        // this painting logic is significantly different to other cards as instead of painting elements in specific
        // regions the content falls vertically down the page

        // title needs special handling as it is allowed to spill onto a second line
        // set the alignment to the top so a short title will stay on one line
        MarkupRenderer titleMarkupRenderer = paintContext.createMarkupRenderer();

        // similar but bigger font for this title
        titleMarkupRenderer.setDefaultStyle(TextStyleUtils.withAttribute(TextStyleUtils.getTitleTextStyle(), TextAttribute.SIZE, 15));
        titleMarkupRenderer.setAlignment(MarkupRenderer.LAYOUT_CENTER | MarkupRenderer.LAYOUT_TOP);
        titleMarkupRenderer.setTextFitting(MarkupRenderer.FIT_SCALE_TEXT);
        titleMarkupRenderer.setLineTightness(0.5f);
        MarkupUtils.applyTagMarkupConfiguration(titleMarkupRenderer);

        titleMarkupRenderer.setMarkupText(commonCardFieldsView.getTitleText());

        // the MarkupRenderer.draw() is supposed to return the 'next' y position
        // however when the input text has no whitespace and FIT_SCALE_TEXT is used it follows a different code path
        // and returns something else (which is 0)
        // so instead do measure() which returns the height of the text without drawing and use that to calculate
        // then draw afterwards
        Rectangle titleRectangle = paintContext.toPixelRect(TITLE_DRAW_REGION);

        double titleEndedAtYPosition = titleRectangle.getY() + titleMarkupRenderer.measure(paintContext.getGraphics(), titleRectangle);
        titleMarkupRenderer.draw(paintContext.getGraphics(), titleRectangle);

        // trim slightly to tighten the line vertically against the title
        titleEndedAtYPosition = titleEndedAtYPosition - paintContext.millimetersToPixels(0.42);

        // draw a double line below the title
        double titleLineWidth = MarkupUtils.getLastLineWidthInPixels(titleMarkupRenderer);
        double secondLineYPosition = drawTitleUnderlines(paintContext, titleLineWidth, titleEndedAtYPosition);

        // create some vertical space between the lines and the next element
        double doubleLinesEndedAt = secondLineYPosition + paintContext.millimetersToPixels(0.85);

        Rectangle difficultyRectangle = paintContext.toPixelRect(DIFFICULTY_DRAW_REGION);

        // if the title overspilled then move everything else down
        int yDelta = Math.max(0, (int)Math.round(doubleLinesEndedAt - difficultyRectangle.getY()));

        Rectangle difficultyDrawRegion = difficultyRectangle.getBounds();
        difficultyDrawRegion.translate(0, yDelta);
        paintDifficulty(paintContext, difficultyDrawRegion);

        Rectangle bodyRectangle = paintContext.toPixelRect(BODY_DRAW_REGION);
        Rectangle trackingBoxRectangle = paintContext.toPixelRect(TRACKING_BOX_DRAW_REGION);

        // calculate the body region
        // the Y starts just after the difficulty, which may have moved from its default location above so calculate
        // the Y ends just before the tracking box, if it exists, otherwise the default
        double bodyYStart = difficultyDrawRegion.getMaxY() + paintContext.millimetersToPixels(1.70);
        double bodyYEnd = bodyRectangle.getMaxY();

        if (!StringUtils.isEmpty(getModel().getScenarioReferenceFieldsModel().getTrackingBox())) {
            bodyYEnd = trackingBoxRectangle.getY();
        }

        Rectangle bodyDrawRegion = new Rectangle((int)bodyRectangle.getX(), (int)bodyYStart, (int)bodyRectangle.getWidth(), (int)(bodyYEnd - bodyYStart));

        paintBody(paintContext, bodyDrawRegion);

        paintTrackingBox(paintContext);

        encounterSetView.paintEncounterPortrait(paintContext, paintContext.toPixelRect(ENCOUNTER_PORTRAIT_DRAW_REGION));
        encounterSetView.paintEncounterNumbers(paintContext, CardFaceOrientation.Portrait);
        collectionView.paintCollectionImage(paintContext, CardFaceOrientation.Portrait, true);
        collectionView.paintCollectionNumber(paintContext, CardFaceOrientation.Portrait);

        commonCardFieldsView.paintCopyright(paintContext);
    }

    private double drawTitleUnderlines(PaintContext paintContext, double lineWidth, double titleEndedAtYPosition) {
        double lineXPosition = paintContext.toPixelRect(TITLE_DRAW_REGION).getCenterX() - lineWidth * 0.5;
        Graphics2D lineGraphics = (Graphics2D) paintContext.getGraphics().create();
        lineGraphics.setStroke(new BasicStroke(2.0f));
        lineGraphics.setPaint(Color.BLACK);
        lineGraphics.drawLine((int) lineXPosition, (int) titleEndedAtYPosition, (int) (lineXPosition + lineWidth), (int) titleEndedAtYPosition);
        double secondLineYPosition = titleEndedAtYPosition + paintContext.millimetersToPixels(0.42);
        lineGraphics.drawLine((int) lineXPosition, (int) secondLineYPosition, (int) (lineXPosition + lineWidth), (int) secondLineYPosition);

        return secondLineYPosition;
    }

    private Map<ScenarioReference.SymbolChaosTokenInfo, List<ScenarioReference.SymbolChaosToken>> combineTokens() {
        Map<ScenarioReference.SymbolChaosTokenInfo, List<ScenarioReference.SymbolChaosToken>> combinedMap = new LinkedHashMap<>();

        Map<ScenarioReference.SymbolChaosToken, ScenarioReference.SymbolChaosTokenInfo> tokenMap = new LinkedHashMap<>();
        tokenMap.put(ScenarioReference.SymbolChaosToken.Skull, getModel().getScenarioReferenceFieldsModel().getSkull());
        tokenMap.put(ScenarioReference.SymbolChaosToken.Cultist, getModel().getScenarioReferenceFieldsModel().getCultist());
        tokenMap.put(ScenarioReference.SymbolChaosToken.Tablet, getModel().getScenarioReferenceFieldsModel().getTablet());
        tokenMap.put(ScenarioReference.SymbolChaosToken.ElderThing, getModel().getScenarioReferenceFieldsModel().getElderThing());

        Map<ScenarioReference.SymbolChaosToken, ScenarioReference.SymbolChaosToken> combinedInto = new HashMap<>();

        // iterate the possible tokens/info and build a reverse map with combinations resolved
        for (Map.Entry<ScenarioReference.SymbolChaosToken, ScenarioReference.SymbolChaosTokenInfo> entry : tokenMap.entrySet()) {
            ScenarioReference.SymbolChaosToken token = entry.getKey();
            ScenarioReference.SymbolChaosTokenInfo tokenInfo = entry.getValue();

            // find out if this token has already been combined
            ScenarioReference.SymbolChaosToken alreadyCombinedInto = combinedInto.get(token);

            // if not already combined create an entry for it, otherwise add it to the list of the token it is combining with
            if (alreadyCombinedInto == null) {
                combinedMap.put(tokenInfo, Lists.newArrayList(token));
            }
            else {
                combinedMap.get(tokenMap.get(alreadyCombinedInto)).add(token);
            }

            // if this token is combining with another, token then record that and merge it in when that token is processed
            // note that the combineWith can effectively be transitive,
            // e.g. skull combine with cultist and cultist combine with tablet -> skull combined with tablet
            ScenarioReference.SymbolChaosToken combineWith = tokenInfo.getCombineWith();
            if (combineWith != null) {
                combinedInto.put(combineWith, alreadyCombinedInto != null ? alreadyCombinedInto : token);
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
        }
    }

    private void paintDifficulty(PaintContext paintContext, Rectangle drawRegion) {
        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getScenarioReferenceDifficultyTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_CENTER | MarkupRenderer.LAYOUT_TOP);
        markupRenderer.setMarkupText(getModel().getScenarioReferenceFieldsModel().getDifficulty().toString());
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

        Map<ScenarioReference.SymbolChaosTokenInfo, MultiSectionRenderer.Section> infoToSectionMap = new LinkedHashMap<>();

        int verticalSpaceBetweenChaosTokensInPixels = paintContext.millimetersToPixels(0.85);

        double totalVerticalSpacerHeight = (combinedTokens.size() - 1) * verticalSpaceBetweenChaosTokensInPixels;
        double totalHeightAvailable = drawRegion.getHeight() - totalVerticalSpacerHeight;
        double heightPerChaosToken = totalHeightAvailable / 4;

        for (Map.Entry<ScenarioReference.SymbolChaosTokenInfo, List<ScenarioReference.SymbolChaosToken>> entry : combinedTokens.entrySet()) {
            ScenarioReference.SymbolChaosTokenInfo chaosTokenInfo = entry.getKey();
            List<ScenarioReference.SymbolChaosToken> chaosTokens = entry.getValue();

            // add a spacer
            if (!renderer.getSections().isEmpty())
                renderer.getSections().add(new MultiSectionRenderer.VerticalSpacerSection(verticalSpaceBetweenChaosTokensInPixels));

            // allocate this info section a height in proportion to how many chaos tokens it has
            int sectionHeight = (int)heightPerChaosToken * chaosTokens.size();

            Supplier<MarkupRenderer> markupRendererSupplier = () -> {
                MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
                MarkupUtils.applyTagMarkupConfiguration(markupRenderer);
                markupRenderer.setDefaultStyle(TextStyleUtils.getBodyTextStyle());
                markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_LEFT);
                markupRenderer.setLineTightness(0.6f);
                markupRenderer.setMarkupText(chaosTokenInfo.getRules());
                return markupRenderer;
            };

            MultiSectionRenderer.Section section;
            if (chaosTokens.size() > 1) {
                section = new MultiSectionRenderer.DoubleLineInsetTextSection(markupRendererSupplier, sectionHeight, sectionHeight, paintContext.millimetersToPixels(1.27));
            }
            else {
                section = new MultiSectionRenderer.TextSection(markupRendererSupplier, sectionHeight, sectionHeight);
            }

            renderer.getSections().add(section);

            infoToSectionMap.put(chaosTokenInfo, section);
        }

        // draw the chaos token symbols next to the respective draw regions
        Map<MultiSectionRenderer.Section, Rectangle> sectionDrawRegions = renderer.draw();

        for (Map.Entry<ScenarioReference.SymbolChaosTokenInfo, MultiSectionRenderer.Section> entry : infoToSectionMap.entrySet()) {
            ScenarioReference.SymbolChaosTokenInfo info = entry.getKey();

            MultiSectionRenderer.Section section = entry.getValue();
            Rectangle sectionDrawRegion = sectionDrawRegions.get(section);

            List<ScenarioReference.SymbolChaosToken> tokens = combinedTokens.get(info);

            // chaos tokens are 7mm in diameter
            // for the case with multiple tokens in the same group, add a small vertical space between
            // calculate the y position of the first token in the group
            double yPosition = sectionDrawRegion.getCenterY();

            // the offset is the number of tokens - 1 multiplied by the half of the size of a vertical gap + token height
            // for example
            // - with tokens = 1 there is no adjustment
            // - with tokens = 3 then the first token needs to move a full height + a full spacer above
            // - with tokens = 2 then it is half of tokens = 2
            int tokenDiameterInPixels = paintContext.millimetersToPixels(7);
            int verticalGapInPixels = paintContext.millimetersToPixels(1);

            // this gives the centre position of the first token
            yPosition = yPosition - (tokens.size() - 1) * (0.5 * (tokenDiameterInPixels + verticalGapInPixels));

            // this gives the top position of the first token
            yPosition = yPosition - (0.5 * tokenDiameterInPixels);

            int chaosTokenXPosition = paintContext.millimetersToPixels(8.45);

            for (ScenarioReference.SymbolChaosToken token : tokens) {
                // draw the token image
                PaintUtils.paintBufferedImage(paintContext.getGraphics(),
                        ImageUtils.loadImageReadOnly("/overlays/chaos_tokens/chaos_" + getChaosTokenResourceName(token) + ".png"),
                        new Rectangle(chaosTokenXPosition, (int)yPosition, tokenDiameterInPixels, tokenDiameterInPixels));

                yPosition = yPosition + tokenDiameterInPixels + verticalGapInPixels;
            }
        }
    }

    private String getChaosTokenResourceName(ScenarioReference.SymbolChaosToken token) {
        switch (token) {
            case Skull:
                return "skull";
            case Cultist:
                return "cultist";
            case Tablet:
                return "tablet";
            case ElderThing:
                return "elder_thing";
            default:
                throw new RuntimeException("Invalid token type '" + token + "'");
        }
    }

    private void paintTrackingBox(PaintContext paintContext) {
        if (StringUtils.isEmpty(getModel().getScenarioReferenceFieldsModel().getTrackingBox()))
            return;

        // background image
        PaintUtils.paintBufferedImage(paintContext.getGraphics(), ImageUtils.loadImageReadOnly("/overlays/scenario_reference/scenario_reference_tracker_box.png"), paintContext.toPixelRect(TRACKING_BOX_DRAW_REGION));

        // title
        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getScenarioReferenceTrackerBoxTitleTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_CENTER | MarkupRenderer.LAYOUT_TOP);
        markupRenderer.setMarkupText(getModel().getScenarioReferenceFieldsModel().getTrackingBox());
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), paintContext.toPixelRect(TRACKING_TITLE_DRAW_REGION));
    }
}
