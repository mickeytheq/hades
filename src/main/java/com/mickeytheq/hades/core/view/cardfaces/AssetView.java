package com.mickeytheq.hades.core.view.cardfaces;

import com.google.common.collect.Lists;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.model.common.PlayerCardClass;
import com.mickeytheq.hades.core.model.common.PlayerCardType;
import com.mickeytheq.hades.codegenerated.GameConstants;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.cardfaces.Asset;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.common.*;
import com.mickeytheq.hades.core.view.component.StatisticComponent;
import com.mickeytheq.hades.core.view.utils.*;
import com.mickeytheq.hades.util.shape.DimensionEx;
import com.mickeytheq.hades.util.shape.RectangleEx;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

@View(interfaceLanguageKey = InterfaceConstants.ASSET)
public class AssetView extends BaseCardFaceView<Asset> implements HasCollectionView, HasEncounterSetView {
    private JComboBox<Asset.AssetSlot> assetSlot1Editor;
    private JComboBox<Asset.AssetSlot> assetSlot2Editor;
    private StatisticComponent healthEditor;
    private StatisticComponent sanityEditor;

    private CommonCardFieldsView commonCardFieldsView;
    private EncounterSetView encounterSetView;
    private CollectionView collectionView;
    private PlayerCardFieldsView playerCardFieldsView;
    private PortraitView portraitView;

    private static final RectangleEx ART_PORTRAIT_DRAW_REGION = RectangleEx.millimetresHorizontallyCentred(6.77, 60.62, 43.69);

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel(), this);
        collectionView = new CollectionView(getModel().getCollectionModel(), this);
        encounterSetView = new EncounterSetView(getModel().getEncounterSetModel(), this);
        playerCardFieldsView = new PlayerCardFieldsView(getModel().getPlayerCardFieldsModel(), true);
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
    public String getTitle() {
        return StringUtils.defaultIfEmpty(commonCardFieldsView.getModel().getTitle(), null);
    }

    @Override
    protected List<TemplateInfo> getAvailableTemplateInfos() {
        return TemplateInfos.createStandard300And600(getTemplateResourcePrefix(), CardFaceOrientation.Portrait);
    }

    private String getTemplateResourcePrefix() {
        String templateResource = "/templates/asset/asset_" + getTemplateName();

        // assets templates have the subtitle overlay already built in so switch template when a subtitle is present
        if (canHaveSubtitle() && !StringUtils.isEmpty(getModel().getCommonCardFieldsModel().getSubtitle()))
            templateResource = templateResource + "_subtitle";

        return templateResource;
    }

    private String getTemplateName() {
        return playerCardFieldsView.getDefaultResourcePrefix();
    }

    private boolean canHaveSubtitle() {
        PlayerCardType playerCardType = getModel().getPlayerCardFieldsModel().getCardType();

        if (playerCardType == PlayerCardType.StoryWeakness)
            return false;

        return true;
    }

    @Override
    public void createEditors(EditorContext editorContext) {
        commonCardFieldsView.createEditors(editorContext);

        portraitView.createEditors(editorContext);

        createTitleAndStatisticsEditors(editorContext);
        createRulesAndPortraitTab(editorContext);

        collectionView.createEditors(editorContext);
        encounterSetView.createEditors(editorContext);

        CardFaceViewUtils.createEncounterSetCollectionTab(editorContext, encounterSetView, collectionView);
    }

    private void createTitleAndStatisticsEditors(EditorContext editorContext) {
        // title
        JPanel titlePanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.TITLE));
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, true, true, false);

        playerCardFieldsView.createEditors(editorContext);

        assetSlot1Editor = EditorUtils.createEnumComboBoxNullable(Asset.AssetSlot.class);
        assetSlot2Editor = EditorUtils.createEnumComboBoxNullable(Asset.AssetSlot.class);

        healthEditor = new StatisticComponent();
        sanityEditor = new StatisticComponent();

        JPanel statsPanel = new JPanel(MigLayoutUtils.createTwoColumnLayout());
        statsPanel.setBorder(BorderFactory.createTitledBorder("Stats")); // TODO: i18n

        // layout

        // first column with additional labels
        playerCardFieldsView.layoutFirstColumnLabels(statsPanel);
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.SLOT1));
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.SLOT2));
        MigLayoutUtils.addLabel(statsPanel, "Health"); // TODO: i18n
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.SANITY));

        // second column with additional editors
        playerCardFieldsView.layoutSecondColumnEditors(statsPanel);
        statsPanel.add(assetSlot1Editor);
        statsPanel.add(assetSlot2Editor);
        statsPanel.add(healthEditor);
        statsPanel.add(sanityEditor);

        playerCardFieldsView.layoutThirdColumnLabels(statsPanel);
        playerCardFieldsView.layoutFourthColumnEditors(statsPanel);

        // bindings
        EditorUtils.bindComboBox(assetSlot1Editor, editorContext.wrapConsumerWithMarkedChanged(getModel().getAssetFieldsModel()::setSlot1));
        EditorUtils.bindComboBox(assetSlot2Editor, editorContext.wrapConsumerWithMarkedChanged(getModel().getAssetFieldsModel()::setSlot2));
        EditorUtils.bindStatisticComponent(healthEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getAssetFieldsModel()::setHealth));
        EditorUtils.bindStatisticComponent(sanityEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getAssetFieldsModel()::setSanity));

        // intialise values
        assetSlot1Editor.setSelectedItem(getModel().getAssetFieldsModel().getSlot1());
        assetSlot2Editor.setSelectedItem(getModel().getAssetFieldsModel().getSlot2());
        healthEditor.setStatistic(getModel().getAssetFieldsModel().getHealth());
        sanityEditor.setStatistic(getModel().getAssetFieldsModel().getSanity());

        JPanel mainPanel = MigLayoutUtils.createOrganiserPanel();
        mainPanel.add(titlePanel, "wrap, growx, pushx");
        mainPanel.add(statsPanel, "wrap, growx, pushx");

        editorContext.addDisplayComponent("Stats", mainPanel); // TODO: i18n
    }

    private void createRulesAndPortraitTab(EditorContext editorContext) {
        JPanel generalPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.GENERAL));
        commonCardFieldsView.addNonTitleEditorsToPanel(generalPanel, false);

        JPanel mainPanel = MigLayoutUtils.createVerticalFlowOrganiserPanel(
                generalPanel,
                portraitView.createStandardArtPanel(editorContext)
        );

        // add the panel to the main tab control
        editorContext.addDisplayComponent("Rules / portrait", mainPanel); // TODO: i18n
    }

    private static final RectangleEx LABEL_DRAW_REGION = RectangleEx.millimetres(3.32, 10.54, 6.43, 2.37);
    private static final RectangleEx TITLE_DRAW_REGION = RectangleEx.millimetresHorizontallyCentred(2.37, 40.30, 4.91);
    private static final RectangleEx SUBTITLE_DRAW_REGION = RectangleEx.millimetresHorizontallyCentred(7.05, 40.30, 3.73);
    private static final RectangleEx BODY_DRAW_REGION = RectangleEx.millimetresHorizontallyCentred(54.19, 56.90, 22.71);

    @Override
    public void paint(PaintContext paintContext) {
        // paint the main/art portrait first as it sits behind the card template
        portraitView.paintArtPortrait(paintContext, paintContext.toPixelRect(ART_PORTRAIT_DRAW_REGION));

        // draw the template
        paintContext.paintTemplate();

        paintContext.setRenderingIncludeBleedRegion(false);

        // label
        PaintUtils.paintLabel(paintContext, paintContext.toPixelRect(LABEL_DRAW_REGION), Language.gstring(GameConstants.LABEL_ASSET).toUpperCase());

        // specialist cards don't support a subtitle
        if (canHaveSubtitle()) {
            commonCardFieldsView.paintSubtitle(paintContext, paintContext.toPixelRect(SUBTITLE_DRAW_REGION));
        }

        commonCardFieldsView.paintBodyAndCopyright(paintContext, paintContext.toPixelRect(BODY_DRAW_REGION));

        portraitView.paintArtist(paintContext);

        collectionView.paintCollectionImage(paintContext, CardFaceOrientation.Portrait, true);
        collectionView.paintCollectionNumber(paintContext, CardFaceOrientation.Portrait);

        // the multi class painting returns the left most X position occupied by a class symbol
        int leftMostClassSymbolXPosition = paintMultiClassSymbols(paintContext);

        Rectangle titleDrawRegion = paintContext.toPixelRect(TITLE_DRAW_REGION);

        // if needed - reduce the title draw region by an amount
        if (getModel().getPlayerCardFieldsModel().isMultiClass()) {
            // do this by intersecting the default draw region by a region that only goes up to where the class symbols were painted
            titleDrawRegion = titleDrawRegion.intersection(new Rectangle(0, 0, leftMostClassSymbolXPosition, Integer.MAX_VALUE));
        }

        commonCardFieldsView.paintTitle(paintContext, titleDrawRegion);

        playerCardFieldsView.paintEncounterSetIconCircle(paintContext, paintContext.toPixelRect(ENCOUNTER_SET_CIRCLE_WEAKNESS_DRAW_REGION));

        paintWeaknessContent(paintContext);

        if (getModel().getPlayerCardFieldsModel().getCardType().isHasEncounterDetails()) {
            encounterSetView.paintEncounterNumbers(paintContext, CardFaceOrientation.Portrait);
            encounterSetView.paintEncounterImage(paintContext, paintContext.toPixelRect(ENCOUNTER_PORTRAIT_DRAW_REGION));
        }

        playerCardFieldsView.paintLevelCircleAndPips(paintContext);
        playerCardFieldsView.paintCost(paintContext);

        playerCardFieldsView.paintSkillIcons(paintContext);

        paintSlots(paintContext);

        if (!getModel().getAssetFieldsModel().getHealth().isEmpty()) {
            PaintUtils.paintBufferedImage(paintContext.getGraphics(), ImageUtils.loadImageReadOnly("/overlays/stats/health.png"), paintContext.toPixelRect(HEALTH_SYMBOL_DRAW_REGION));
            PaintUtils.paintStatistic(paintContext, paintContext.toPixelRect(HEALTH_STATISTIC_DRAW_REGION), getModel().getAssetFieldsModel().getHealth(), PaintUtils.HEALTH_TEXT_OUTLINE_COLOUR, PaintUtils.STATISTIC_LIGHT_TEXT_COLOUR);
        }

        if (!getModel().getAssetFieldsModel().getSanity().isEmpty()) {
            PaintUtils.paintBufferedImage(paintContext.getGraphics(), ImageUtils.loadImageReadOnly("/overlays/stats/sanity.png"), paintContext.toPixelRect(SANITY_SYMBOL_DRAW_REGION));
            PaintUtils.paintStatistic(paintContext, paintContext.toPixelRect(SANITY_STATISTIC_DRAW_REGION), getModel().getAssetFieldsModel().getSanity(), PaintUtils.SANITY_TEXT_OUTLINE_COLOUR, PaintUtils.STATISTIC_LIGHT_TEXT_COLOUR);
        }
    }

    // regions are from right to left
    private static final List<RectangleEx> CLASS_SYMBOL_REGIONS = Lists.newArrayList(
            RectangleEx.millimetres(38.78, 0.34, PaintConstants.CLASS_SYMBOL_SIZE),
            RectangleEx.millimetres(46.4, 0.34, PaintConstants.CLASS_SYMBOL_SIZE),
            RectangleEx.millimetres(54.02, 0.34, PaintConstants.CLASS_SYMBOL_SIZE)
    );

    private int paintMultiClassSymbols(PaintContext paintContext) {
        if (!getModel().getPlayerCardFieldsModel().isMultiClass())
            return Integer.MAX_VALUE;

        List<PlayerCardClass> playerCardClasses = getModel().getPlayerCardFieldsModel().getPlayerCardClasses();

        // we want the symbols right justified on the card but in the correct order as specified by the individual fields
        // so we calculate a number of symbols to skip which should be 0 or 1
        int skipSymbolRegionsCount = 3 - playerCardClasses.size();

        int leftMostEdge = Integer.MAX_VALUE;

        for (int i = 0; i < playerCardClasses.size(); i++) {
            PlayerCardClass playerCardClass = playerCardClasses.get(i);

            BufferedImage classSymbol = ImageUtils.loadImageReadOnly("/overlays/class_symbols/" + playerCardClass.name().toLowerCase() + ".png");

            Rectangle rectangle = paintContext.toPixelRect(CLASS_SYMBOL_REGIONS.get(i + skipSymbolRegionsCount));

            leftMostEdge = Math.min((int)rectangle.getX(), leftMostEdge);

            PaintUtils.paintBufferedImage(paintContext.getGraphics(), classSymbol, rectangle);
        }

        // return the left most x position/edge of the painted class symbols which will be used to confine
        // the drawing space for the title
        return leftMostEdge;
    }

    private static final RectangleEx ENCOUNTER_SET_CIRCLE_WEAKNESS_DRAW_REGION = RectangleEx.millimetres(53.71, 0, PaintConstants.ENCOUNTER_SET_CIRCLE_OVERLAY_SIZE);
    private static final RectangleEx WEAKNESS_LABEL_DRAW_REGION = RectangleEx.millimetresHorizontallyCentred(50.87, 34.37, 2.54);
    private static final RectangleEx BASIC_WEAKNESS_ICON_DRAW_REGION = ENCOUNTER_SET_CIRCLE_WEAKNESS_DRAW_REGION.centreOn(PaintConstants.BASIC_WEAKNESS_ICON_SIZE);
    private static final RectangleEx ENCOUNTER_PORTRAIT_DRAW_REGION = ENCOUNTER_SET_CIRCLE_WEAKNESS_DRAW_REGION.centreOn(PaintConstants.ENCOUNTER_SET_ICON_SIZE).nudgeUp(0.4).nudgeLeft(0.2);

    private void paintWeaknessContent(PaintContext paintContext) {
        PlayerCardType playerCardType = getModel().getPlayerCardFieldsModel().getCardType();

        if (!playerCardType.isWeakness())
            return;

        if (playerCardType == PlayerCardType.Weakness || playerCardType == PlayerCardType.StoryWeakness) {
            PaintUtils.paintLabel(paintContext, paintContext.toPixelRect(WEAKNESS_LABEL_DRAW_REGION), Language.gstring(GameConstants.LABEL_WEAKNESS).toUpperCase());
        } else if (playerCardType == PlayerCardType.BasicWeakness) {
            PaintUtils.paintLabel(paintContext, paintContext.toPixelRect(WEAKNESS_LABEL_DRAW_REGION), Language.gstring(GameConstants.LABEL_BASICWEAKNESS).toUpperCase());
            PaintUtils.paintBufferedImage(paintContext.getGraphics(), ImageUtils.loadImageReadOnly(ImageUtils.BASIC_WEAKNESS_ICON_RESOURCE), paintContext.toPixelRect(BASIC_WEAKNESS_ICON_DRAW_REGION));
        }
    }

    private static final DimensionEx SLOT_IMAGE_SIZE = DimensionEx.millimetres(9.223, 9.223);

    private static final List<RectangleEx> SLOT_DRAW_REGIONS = Lists.newArrayList(
            RectangleEx.millimetres(43.18, 76.88, SLOT_IMAGE_SIZE),
            RectangleEx.millimetres(52.32, 76.88, SLOT_IMAGE_SIZE)
    );

    private void paintSlots(PaintContext paintContext) {
        List<Asset.AssetSlot> assetSlots = getModel().getAssetFieldsModel().getAssetSlots();

        if (assetSlots.isEmpty())
            return;

        // when there's a single asset slot we want it displayed on the far right position
        if (assetSlots.size() == 1) {
            paintSlot(paintContext, SLOT_DRAW_REGIONS.get(1), assetSlots.get(0));
            return;
        }

        // when there's a two asset slot we want them displayed left to right with the first
        // asset in the left-most position
        if (assetSlots.size() == 2) {
            paintSlot(paintContext, SLOT_DRAW_REGIONS.get(0), assetSlots.get(0));
            paintSlot(paintContext, SLOT_DRAW_REGIONS.get(1), assetSlots.get(1));
            return;
        }
    }

    private void paintSlot(PaintContext paintContext, RectangleEx drawRegion, Asset.AssetSlot assetSlot) {
        PaintUtils.paintBufferedImage(
                paintContext.getGraphics(),
                ImageUtils.loadImageReadOnly("/overlays/asset_slot/" + getSlotName(assetSlot) + ".png"),
                paintContext.toPixelRect(drawRegion)
        );
    }

    private String getSlotName(Asset.AssetSlot assetSlot) {
        return assetSlot.name().toLowerCase();
    }

    private static final RectangleEx HEALTH_SYMBOL_DRAW_REGION = RectangleEx.millimetres(24.32, 79.25, 5.927, 7.197);
    private static final RectangleEx HEALTH_STATISTIC_DRAW_REGION = RectangleEx.millimetres(27.09, 80.69, 0.00, 2.96);
    private static final RectangleEx SANITY_SYMBOL_DRAW_REGION = RectangleEx.millimetres(33.26, 79.76, 7.959, 6.731);
    private static final RectangleEx SANITY_STATISTIC_DRAW_REGION = RectangleEx.millimetres(37.08, 80.69, 0.00, 2.96);
}
