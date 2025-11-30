package com.mickeytheq.ahlcg4j.core.view.cardfaces;

import com.google.common.collect.Lists;
import com.mickeytheq.ahlcg4j.core.view.EditorContext;
import com.mickeytheq.ahlcg4j.core.view.PaintContext;
import com.mickeytheq.ahlcg4j.core.model.common.PlayerCardClass;
import com.mickeytheq.ahlcg4j.core.model.common.PlayerCardSkillIcon;
import com.mickeytheq.ahlcg4j.core.model.common.PlayerCardType;
import com.mickeytheq.ahlcg4j.codegenerated.GameConstants;
import com.mickeytheq.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.Asset;
import com.mickeytheq.ahlcg4j.core.view.BaseCardFaceView;
import com.mickeytheq.ahlcg4j.core.view.View;
import com.mickeytheq.ahlcg4j.core.view.common.CommonCardFieldsView;
import com.mickeytheq.ahlcg4j.core.view.common.NumberingView;
import com.mickeytheq.ahlcg4j.core.view.common.PlayerCardFieldsView;
import com.mickeytheq.ahlcg4j.core.view.component.StatisticComponent;
import com.mickeytheq.ahlcg4j.core.view.utils.*;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

@View(interfaceLanguageKey = InterfaceConstants.ASSET)
public class AssetView extends BaseCardFaceView<Asset> {
    private JComboBox<Asset.AssetSlot> assetSlot1Editor;
    private JComboBox<Asset.AssetSlot> assetSlot2Editor;
    private StatisticComponent healthEditor;
    private StatisticComponent sanityEditor;

    private CommonCardFieldsView commonCardFieldsView;
    private NumberingView numberingView;
    private PlayerCardFieldsView playerCardFieldsView;

    private static final Rectangle ART_PORTRAIT_DRAW_REGION = new Rectangle(20, 80, 716, 516);
    private static final Rectangle ENCOUNTER_PORTRAIT_DRAW_REGION = new Rectangle(658, 20, 60, 60);
    private static final Rectangle COLLECTION_PORTRAIT_DRAW_REGION = new Rectangle(640, 1016, 26, 26);

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel());
        numberingView = new NumberingView(getModel().getNumberingModel());
        playerCardFieldsView = new PlayerCardFieldsView(getModel().getPlayerCardFieldsModel());
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
        if (getModel().getPlayerCardFieldsModel().getPlayerCardType() == PlayerCardType.Standard) {
            List<PlayerCardClass> cardClasses = getModel().getPlayerCardFieldsModel().getPlayerCardClasses();

            if (cardClasses.size() > 1)
                return "multi";
            else
                return cardClasses.get(0).name().toLowerCase();
        }

        switch (getModel().getPlayerCardFieldsModel().getPlayerCardType()) {
            case Neutral:
                return "neutral";

            case Specialist:
                return "specialist";

            case Story:
                return "story";

            case StoryWeakness:
                return "story_weakness";

            case Weakness:
            case BasicWeakness:
                return "weakness";

            default:
                throw new RuntimeException("Unsupported player card type " + getModel().getPlayerCardFieldsModel().getPlayerCardType().name());
        }
    }

    private boolean canHaveSubtitleTemplate() {
        PlayerCardType playerCardType = getModel().getPlayerCardFieldsModel().getPlayerCardType();

        if (playerCardType == PlayerCardType.StoryWeakness || playerCardType == PlayerCardType.Specialist)
            return false;

        return true;
    }

    @Override
    public void createEditors(EditorContext editorContext) {
        createTitleAndStatisticsEditors(editorContext);

        createRulesAndPortraitTab(editorContext);

        numberingView.createEditors(editorContext, COLLECTION_PORTRAIT_DRAW_REGION, ENCOUNTER_PORTRAIT_DRAW_REGION);
        editorContext.getTabbedPane().addTab("Collection / encounter", numberingView.createStandardCollectionEncounterPanel());
    }

    private void createTitleAndStatisticsEditors(EditorContext editorContext) {
        commonCardFieldsView.createEditors(editorContext, ART_PORTRAIT_DRAW_REGION);

        // title
        JPanel titlePanel = MigLayoutUtils.createPanel(Language.string(InterfaceConstants.TITLE));
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, true, true);

        playerCardFieldsView.createEditors(editorContext);

        assetSlot1Editor = EditorUtils.createEnumComboBoxNullable(Asset.AssetSlot.class);
        assetSlot2Editor = EditorUtils.createEnumComboBoxNullable(Asset.AssetSlot.class);

        healthEditor = new StatisticComponent();
        sanityEditor = new StatisticComponent();

        JPanel statsPanel = new JPanel(playerCardFieldsView.createTwoColumnLayout());
        statsPanel.setBorder(BorderFactory.createTitledBorder("Stats")); // TODO: i18n

        JPanel mainPanel = MigLayoutUtils.createPanel();
        mainPanel.add(titlePanel, "wrap, growx, pushx");
        mainPanel.add(statsPanel, "wrap, growx, pushx");

        editorContext.getTabbedPane().addTab(Language.string(InterfaceConstants.ASSET) + " - " + "Stats", mainPanel); // TODO: i18n

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
    }

    private void createRulesAndPortraitTab(EditorContext editorContext) {
        commonCardFieldsView.createEditors(editorContext, ART_PORTRAIT_DRAW_REGION);

        JPanel generalPanel = MigLayoutUtils.createPanel("General"); // TODO: i18n
        commonCardFieldsView.addNonTitleEditorsToPanel(generalPanel);

        JPanel mainPanel = new JPanel(new MigLayout());

        mainPanel.add(generalPanel, "wrap, pushx, growx");
        mainPanel.add(commonCardFieldsView.createStandardArtPanel(), "wrap, pushx, growx");

        // add the panel to the main tab control
        editorContext.getTabbedPane().addTab("Rules / portrait", mainPanel); // TODO: i18n
    }

    private static final Rectangle LABEL_DRAW_REGION = new Rectangle(38, 128, 76, 28);
    private static final Rectangle TITLE_DRAW_REGION = new Rectangle(136, 28, 476, 58);
    private static final Rectangle SUBTITLE_DRAW_REGION = new Rectangle(136, 88, 476, 44);
    private static final Rectangle BODY_DRAW_REGION = new Rectangle(40, 640, 672, 280);
    private static final Rectangle WEAKNESS_LABEL_DRAW_REGION = new Rectangle(172, 602, 406, 30);
    private static final Rectangle BASIC_WEAKNESS_ICON_DRAW_REGION = new Rectangle(658, 602, 406, 30);


    @Override
    public void paint(PaintContext paintContext) {
        // paint the main/art portrait first as it sits behind the card template
        commonCardFieldsView.paintArtPortrait(paintContext);

        // draw the template
        paintContext.getGraphics().drawImage(getTemplateImage(), 0, 0, null);

        // label
        PaintUtils.paintLabel(paintContext, LABEL_DRAW_REGION, Language.gstring(GameConstants.LABEL_ASSET).toUpperCase());

        // title
        // TODO: for multi-class cards the title position may need to be shifted left somewhat - see Bruiser as an example
        commonCardFieldsView.paintTitles(paintContext, TITLE_DRAW_REGION, SUBTITLE_DRAW_REGION);

        commonCardFieldsView.paintBodyCopyrightArtist(paintContext, BODY_DRAW_REGION);

        if (getModel().getPlayerCardFieldsModel().getPlayerCardType().isHasEncounterDetails()) {
            numberingView.paintEncounterNumbers(paintContext);
            numberingView.paintEncounterPortrait(paintContext);
        }

        numberingView.paintCollectionPortrait(paintContext, true);
        numberingView.paintCollectionNumber(paintContext);

        // player card icons
        paintClassSymbols(paintContext);

        // weakness labels
        paintWeaknessContent(paintContext);

        playerCardFieldsView.paintLevel(paintContext);

        playerCardFieldsView.paintCost(paintContext);

        playerCardFieldsView.paintSkillIcons(paintContext);

        paintSlots(paintContext);

        paintHealth(paintContext);
        paintSanity(paintContext);
    }

    // regions are from right to left
    private static final List<Rectangle> CLASS_SYMBOL_REGIONS = Lists.newArrayList(
            new Rectangle(458, 4, 104, 104),
            new Rectangle(548, 4, 104, 104),
            new Rectangle(638, 4, 104, 104)
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

            Rectangle rectangle = CLASS_SYMBOL_REGIONS.get(i + skipSymbolRegionsCount);

            PaintUtils.paintBufferedImage(paintContext.getGraphics(), classSymbol, rectangle);
        }
    }

    private void paintWeaknessContent(PaintContext paintContext) {
        PlayerCardType playerCardType = getModel().getPlayerCardFieldsModel().getPlayerCardType();

        if (playerCardType == PlayerCardType.Weakness || playerCardType == PlayerCardType.StoryWeakness) {
            PaintUtils.paintLabel(paintContext, WEAKNESS_LABEL_DRAW_REGION, Language.gstring(GameConstants.LABEL_WEAKNESS).toUpperCase());
        } else if (playerCardType == PlayerCardType.BasicWeakness) {
            PaintUtils.paintLabel(paintContext, WEAKNESS_LABEL_DRAW_REGION, Language.gstring(GameConstants.LABEL_BASICWEAKNESS).toUpperCase());
            ImageUtils.drawImage(paintContext.getGraphics(), ImageUtils.loadImage(ImageUtils.BASIC_WEAKNESS_ICON_RESOURCE), BASIC_WEAKNESS_ICON_DRAW_REGION);
        }
    }

    private static final List<Rectangle> SLOT_DRAW_REGIONS = Lists.newArrayList(
            new Rectangle(618, 908, 102, 104),
            new Rectangle(510, 908, 102, 104)
    );

    private void paintSlots(PaintContext paintContext) {
        for (int i = 0; i < getModel().getAssetSlots().size(); i++) {
            Asset.AssetSlot assetSlot = getModel().getAssetSlots().get(i);

            PaintUtils.paintBufferedImage(
                    paintContext.getGraphics(),
                    ImageUtils.loadImage(getClass().getResource("/overlays/slot_" + getSlotName(assetSlot) + ".png")),
                    SLOT_DRAW_REGIONS.get(i)
            );
        }
    }

    private String getSlotName(Asset.AssetSlot assetSlot) {
        return assetSlot.name().toLowerCase();
    }

    private static final Rectangle HEALTH_BASE_DRAW_REGION = new Rectangle(292, 936, 62, 78);
    private static final Rectangle SANITY_BASE_DRAW_REGION = new Rectangle(394, 942, 86, 72);

    private static final Font STAT_FONT = new Font("Bolton", Font.PLAIN, 24);
    private static final Font PER_INVESTIGATOR_FONT = new Font(TextStyleUtils.AHLCG_SYMBOL_FONT, Font.PLAIN, 6).deriveFont(6.5f);

    private static final Color HEALTH_TEXT_COLOUR = new Color(0.996f, 0.945f, 0.859f);
    private static final Color HEALTH_TEXT_OUTLINE_COLOUR = new Color(0.68f, 0.12f, 0.22f);
    private static final Color SANITY_TEXT_COLOUR = HEALTH_TEXT_COLOUR;
    private static final Color SANITY_TEXT_OUTLINE_COLOUR = new Color(0.25f, 0.33f, 0.44f);

    // TODO: review this painting code in different scenarios particularly the per investigator scenarios
    // TODO: ideally how it should work is draw the value text somewhere, and the per investigator text if present, work out
    // TODO: the total width of those two elements and then place them centred on the health/sanity overlay/icon
    private void paintHealth(PaintContext paintContext) {
        String value = getModel().getHealth().getValue();

        if (StringUtils.isEmpty(value))
            return;

        PaintUtils.paintBufferedImage(paintContext.getGraphics(), ImageUtils.loadImage(getClass().getResource("/overlays/health_base.png")), HEALTH_BASE_DRAW_REGION);

        if (getModel().getHealth().isPerInvestigator()) {
            Rectangle healthStatDrawRegion = new Rectangle(HEALTH_BASE_DRAW_REGION);
            healthStatDrawRegion.translate(-15, -5);

            PaintUtils.drawOutlinedTitle(paintContext.getGraphics(), paintContext.getRenderingDpi(),
                    value,
                    healthStatDrawRegion,
                    STAT_FONT, STAT_FONT.getSize(), 3.0f,
                    HEALTH_TEXT_COLOUR,
                    HEALTH_TEXT_OUTLINE_COLOUR,
                    0, true);

            Rectangle perInvestigatorDrawRegion = new Rectangle(healthStatDrawRegion);
            perInvestigatorDrawRegion.translate(30, 0);

            PaintUtils.drawOutlinedTitle(paintContext.getGraphics(), paintContext.getRenderingDpi(),
                    "p",
                    perInvestigatorDrawRegion,
                    PER_INVESTIGATOR_FONT, PER_INVESTIGATOR_FONT.getSize(), 3.0f,
                    HEALTH_TEXT_COLOUR,
                    HEALTH_TEXT_OUTLINE_COLOUR,
                    0, true);
        }
        else {
            Rectangle healthStatDrawRegion = new Rectangle(HEALTH_BASE_DRAW_REGION);
            healthStatDrawRegion.translate(-2, -5);

            PaintUtils.drawOutlinedTitle(paintContext.getGraphics(), paintContext.getRenderingDpi(),
                    value,
                    healthStatDrawRegion,
                    STAT_FONT, STAT_FONT.getSize(), 3.0f,
                    HEALTH_TEXT_COLOUR,
                    HEALTH_TEXT_OUTLINE_COLOUR,
                    0, true);
        }
    }

    private void paintSanity(PaintContext paintContext) {
        String value = getModel().getSanity().getValue();

        if (StringUtils.isEmpty(value))
            return;

        PaintUtils.paintBufferedImage(paintContext.getGraphics(), ImageUtils.loadImage(getClass().getResource("/overlays/sanity_base.png")), SANITY_BASE_DRAW_REGION);

        if (getModel().getSanity().isPerInvestigator()) {
            Rectangle statDrawRegion = new Rectangle(SANITY_BASE_DRAW_REGION);
            statDrawRegion.translate(-8, -5);

            PaintUtils.drawOutlinedTitle(paintContext.getGraphics(), paintContext.getRenderingDpi(),
                    value,
                    statDrawRegion,
                    STAT_FONT, STAT_FONT.getSize(), 3.0f,
                    SANITY_TEXT_COLOUR,
                    SANITY_TEXT_OUTLINE_COLOUR,
                    0, true);

            Rectangle perInvestigatorDrawRegion = new Rectangle(statDrawRegion);
            perInvestigatorDrawRegion.translate(30, 0);

            PaintUtils.drawOutlinedTitle(paintContext.getGraphics(), paintContext.getRenderingDpi(),
                    "p",
                    perInvestigatorDrawRegion,
                    PER_INVESTIGATOR_FONT, PER_INVESTIGATOR_FONT.getSize(), 3.0f,
                    SANITY_TEXT_COLOUR,
                    SANITY_TEXT_OUTLINE_COLOUR,
                    0, true);
        }
        else {
            Rectangle statDrawRegion = new Rectangle(SANITY_BASE_DRAW_REGION);
            statDrawRegion.translate(-2, -5);

            PaintUtils.drawOutlinedTitle(paintContext.getGraphics(), paintContext.getRenderingDpi(),
                    value,
                    statDrawRegion,
                    STAT_FONT, STAT_FONT.getSize(), 3.0f,
                    SANITY_TEXT_COLOUR,
                    SANITY_TEXT_OUTLINE_COLOUR,
                    0, true);
        }
    }
}
