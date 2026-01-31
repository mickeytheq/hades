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
import com.mickeytheq.hades.util.shape.RectangleEx;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
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

    private static final RectangleEx ART_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(1.69, 6.77, 60.62, 43.69);
    private static final RectangleEx ENCOUNTER_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(55.71, 1.69, 5.08, 5.08);

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel());
        collectionView = new CollectionView(getModel().getCollectionModel(), this);
        encounterSetView = new EncounterSetView(getModel().getEncounterSetModel(), this);
        playerCardFieldsView = new PlayerCardFieldsView(getModel().getPlayerCardFieldsModel(), true);
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
    public String getTitle() {
        return StringUtils.defaultIfEmpty(commonCardFieldsView.getModel().getTitle(), null);
    }

    @Override
    public BufferedImage getTemplateImage() {
        return ImageUtils.loadImage(getClass().getResource(getTemplateResource()));
    }

    private String getTemplateResource() {
        String templateResource = "/templates/asset/asset_" + getTemplateName();

        // assets templates have the subtitle overlay already built in so switch template when a subtitle is present
        if (canHaveSubtitleTemplate() && !StringUtils.isEmpty(getModel().getCommonCardFieldsModel().getSubtitle()))
            templateResource = templateResource + "_subtitle";

        templateResource = templateResource + ".png";

        return templateResource;
    }

    private String getTemplateName() {
        PlayerCardType playerCardType = getModel().getPlayerCardFieldsModel().getPlayerCardType();

        // special template for story weakness instead of the regular weakness
        if (playerCardType == PlayerCardType.StoryWeakness)
            return "story_weakness";

        return playerCardFieldsView.getTemplateName();
    }

    private boolean canHaveSubtitleTemplate() {
        PlayerCardType playerCardType = getModel().getPlayerCardFieldsModel().getPlayerCardType();

        if (playerCardType == PlayerCardType.StoryWeakness || playerCardType == PlayerCardType.Specialist)
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
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, true, true);

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
        EditorUtils.bindComboBox(assetSlot1Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setAssetSlot1(value)));
        EditorUtils.bindComboBox(assetSlot2Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setAssetSlot2(value)));
        EditorUtils.bindStatisticComponent(healthEditor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setHealth(value)));
        EditorUtils.bindStatisticComponent(sanityEditor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setSanity(value)));

        // intialise values
        assetSlot1Editor.setSelectedItem(getModel().getAssetSlot1());
        assetSlot2Editor.setSelectedItem(getModel().getAssetSlot2());
        healthEditor.setStatistic(getModel().getHealth());
        sanityEditor.setStatistic(getModel().getSanity());

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

    private static final RectangleEx LABEL_DRAW_REGION = RectangleEx.millimetres(3.22, 10.84, 6.43, 2.37);
    private static final RectangleEx TITLE_DRAW_REGION = RectangleEx.millimetres(11.51, 2.87, 40.30, 4.91);
    private static final RectangleEx SUBTITLE_DRAW_REGION = RectangleEx.millimetres(11.51, 7.35, 40.30, 3.73);
    private static final RectangleEx BODY_DRAW_REGION = RectangleEx.millimetres(3.39, 54.19, 56.90, 22.71);
    private static final RectangleEx WEAKNESS_LABEL_DRAW_REGION = RectangleEx.millimetres(14.56, 50.97, 34.37, 2.54);
    private static final RectangleEx BASIC_WEAKNESS_ICON_DRAW_REGION = RectangleEx.millimetres(55.71, 50.97, 34.37, 2.54);

    @Override
    public void paint(PaintContext paintContext) {
        // paint the main/art portrait first as it sits behind the card template
        portraitView.paintArtPortrait(paintContext, paintContext.toPixelRect(ART_PORTRAIT_DRAW_REGION));

        // draw the template
        paintContext.getGraphics().drawImage(getTemplateImage(), 0, 0, null);

        // label
        PaintUtils.paintLabel(paintContext, paintContext.toPixelRect(LABEL_DRAW_REGION), Language.gstring(GameConstants.LABEL_ASSET).toUpperCase());

        // title
        // TODO: for multi-class cards the title position may need to be shifted left somewhat - see Bruiser as an example
        commonCardFieldsView.paintTitles(paintContext, paintContext.toPixelRect(TITLE_DRAW_REGION), paintContext.toPixelRect(SUBTITLE_DRAW_REGION));

        commonCardFieldsView.paintBodyAndCopyright(paintContext, paintContext.toPixelRect(BODY_DRAW_REGION));

        if (getModel().getPlayerCardFieldsModel().getPlayerCardType().isHasEncounterDetails()) {
            encounterSetView.paintEncounterNumbers(paintContext, CardFaceOrientation.Portrait);
            encounterSetView.paintEncounterPortrait(paintContext, paintContext.toPixelRect(ENCOUNTER_PORTRAIT_DRAW_REGION));
        }

        portraitView.paintArtist(paintContext);

        collectionView.paintCollectionImage(paintContext, CardFaceOrientation.Portrait, true);
        collectionView.paintCollectionNumber(paintContext, CardFaceOrientation.Portrait);

        paintClassSymbols(paintContext);

        paintWeaknessContent(paintContext);

        playerCardFieldsView.paintLevel(paintContext);

        playerCardFieldsView.paintCost(paintContext);

        playerCardFieldsView.paintSkillIcons(paintContext);

        paintSlots(paintContext);

        if (!getModel().getHealth().isEmpty()) {
            PaintUtils.paintBufferedImage(paintContext.getGraphics(), ImageUtils.loadImage("/overlays/health_base.png"), paintContext.toPixelRect(HEALTH_SYMBOL_DRAW_REGION));
            PaintUtils.paintStatistic(paintContext, paintContext.toPixelRect(HEALTH_STATISTIC_DRAW_REGION), getModel().getHealth(), PaintUtils.HEALTH_TEXT_OUTLINE_COLOUR, PaintUtils.STATISTIC_LIGHT_TEXT_COLOUR);
        }

        if (!getModel().getSanity().isEmpty()) {
            PaintUtils.paintBufferedImage(paintContext.getGraphics(), ImageUtils.loadImage("/overlays/sanity_base.png"), paintContext.toPixelRect(SANITY_SYMBOL_DRAW_REGION));
            PaintUtils.paintStatistic(paintContext, paintContext.toPixelRect(SANITY_STATISTIC_DRAW_REGION), getModel().getSanity(), PaintUtils.SANITY_TEXT_OUTLINE_COLOUR, PaintUtils.STATISTIC_LIGHT_TEXT_COLOUR);
        }
    }

    // regions are from right to left
    private static final List<RectangleEx> CLASS_SYMBOL_REGIONS = Lists.newArrayList(
            RectangleEx.millimetres(38.78, 0.34, 8.81, 8.81),
            RectangleEx.millimetres(46.4, 0.34, 8.81, 8.81),
            RectangleEx.millimetres(54.02, 0.34, 8.81, 8.81)
    );

    private void paintClassSymbols(PaintContext paintContext) {
        List<PlayerCardClass> playerCardClasses = getModel().getPlayerCardFieldsModel().getPlayerCardClasses();

        // no class symbols if no classes
        if (playerCardClasses.isEmpty())
            return;

        // for a single class the symbol is integrated into the template
        if (playerCardClasses.size() == 1)
            return;

        // we want the symbols right justified on the card but in the correct order as specified by the individual fields
        // so we calculate a number of symbols to skip which should be 0 or 1
        int skipSymbolRegionsCount = 3 - playerCardClasses.size();

        for (int i = 0; i < playerCardClasses.size(); i++) {
            PlayerCardClass playerCardClass = playerCardClasses.get(i);

            BufferedImage classSymbol = ImageUtils.loadImage(getClass().getResource("/overlays/class_symbol_" + playerCardClass.name().toLowerCase() + ".png"));

            Rectangle rectangle = paintContext.toPixelRect(CLASS_SYMBOL_REGIONS.get(i + skipSymbolRegionsCount));

            PaintUtils.paintBufferedImage(paintContext.getGraphics(), classSymbol, rectangle);
        }
    }

    private void paintWeaknessContent(PaintContext paintContext) {
        PlayerCardType playerCardType = getModel().getPlayerCardFieldsModel().getPlayerCardType();

        if (playerCardType == PlayerCardType.Weakness || playerCardType == PlayerCardType.StoryWeakness) {
            PaintUtils.paintLabel(paintContext, paintContext.toPixelRect(WEAKNESS_LABEL_DRAW_REGION), Language.gstring(GameConstants.LABEL_WEAKNESS).toUpperCase());
        } else if (playerCardType == PlayerCardType.BasicWeakness) {
            PaintUtils.paintLabel(paintContext, paintContext.toPixelRect(WEAKNESS_LABEL_DRAW_REGION), Language.gstring(GameConstants.LABEL_BASICWEAKNESS).toUpperCase());
            ImageUtils.drawImage(paintContext.getGraphics(), ImageUtils.loadImage(ImageUtils.BASIC_WEAKNESS_ICON_RESOURCE), paintContext.toPixelRect(BASIC_WEAKNESS_ICON_DRAW_REGION));
        }
    }

    private static final List<RectangleEx> SLOT_DRAW_REGIONS = Lists.newArrayList(
            RectangleEx.millimetres(43.18, 76.88, 8.64, 8.81),
            RectangleEx.millimetres(52.32, 76.88, 8.64, 8.81)
    );

    private void paintSlots(PaintContext paintContext) {
        List<Asset.AssetSlot> assetSlots = getModel().getAssetSlots();

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
                ImageUtils.loadImage(getClass().getResource("/overlays/slot_" + getSlotName(assetSlot) + ".png")),
                paintContext.toPixelRect(drawRegion)
        );
    }

    private String getSlotName(Asset.AssetSlot assetSlot) {
        return assetSlot.name().toLowerCase();
    }

    private static final RectangleEx HEALTH_SYMBOL_DRAW_REGION = RectangleEx.millimetres(24.72, 79.25, 5.25, 6.60);
    private static final RectangleEx HEALTH_STATISTIC_DRAW_REGION = RectangleEx.millimetres(27.09, 80.69, 0.00, 2.96);
    private static final RectangleEx SANITY_SYMBOL_DRAW_REGION = RectangleEx.millimetres(33.36, 79.76, 7.28, 6.10);
    private static final RectangleEx SANITY_STATISTIC_DRAW_REGION = RectangleEx.millimetres(37.08, 80.69, 0.00, 2.96);
}
