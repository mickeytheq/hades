package com.mickeytheq.strangeeons.ahlcg4j.cardfaces.asset;

import com.google.common.collect.Lists;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.*;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.PaintContext;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.common.PlayerCardClass;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.common.PlayerCardSkillIcon;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.common.PlayerCardType;
import com.mickeytheq.strangeeons.ahlcg4j.codegenerated.GameConstants;
import com.mickeytheq.strangeeons.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.strangeeons.ahlcg4j.ui.component.StatisticComponent;
import com.mickeytheq.strangeeons.ahlcg4j.util.EditorUtils;
import com.mickeytheq.strangeeons.ahlcg4j.util.ImageUtils;
import com.mickeytheq.strangeeons.ahlcg4j.util.MigLayoutUtils;
import com.mickeytheq.strangeeons.ahlcg4j.util.PaintUtils;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        String templateResource = getTemplateResource();

        return ImageUtils.loadImage(getClass().getResource(templateResource));
    }

    private String getTemplateResource() {
        String templateResource = "/templates/asset/asset_" + getTemplateName();

        if (canHaveSubtitleTemplate() && StringUtils.isEmpty(getModel().getCommonCardFieldsModel().getSubtitle()))
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

    // TODO: look at factoring out some/most of these player card fields into a component that can be shared between Asset/Event/Skill
    // TODO: only the health, sanity and slots are asset specific
    private void createTitleAndStatisticsEditors(EditorContext editorContext) {
        commonCardFieldsView.createEditors(editorContext, ART_PORTRAIT_DRAW_REGION);

        // title
        // TODO: sub-title
        JPanel titlePanel = MigLayoutUtils.createPanel(Language.string(InterfaceConstants.TITLE));
        commonCardFieldsView.addTitleEditorToPanel(titlePanel);

        playerCardFieldsView.createEditors(editorContext);

        assetSlot1Editor = EditorUtils.createEnumComboBoxNullable(Asset.AssetSlot.class);
        assetSlot2Editor = EditorUtils.createEnumComboBoxNullable(Asset.AssetSlot.class);

        healthEditor = new StatisticComponent();
        sanityEditor = new StatisticComponent();

        // layout
        //
        // use vertical flow layout to aid readability
        // each column of controls will be added/built one after another instead of row by row
        // we'll use 'newline' constraint on the component at the beginning of a new column
        MigLayout migLayout = new MigLayout(new LC().flowY());

        // this constraint
        // - makes the 2 control columns grow/fill all available space
        // - adds a small visual gap between the two sets of vertical labels/controls
        // - configure the last column to have a reasonable size
        migLayout.setColumnConstraints("[][grow, fill]10[][grow, fill, :200:]");

        JPanel statsPanel = new JPanel(migLayout);
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
        commonCardFieldsView.paintTitle(paintContext, TITLE_DRAW_REGION);

        commonCardFieldsView.paintBodyCopyrightArtist(paintContext, BODY_DRAW_REGION);

        numberingView.paintCollectionPortrait(paintContext, true);
        numberingView.paintCollectionNumber(paintContext);

        // player card icons
        paintClassSymbols(paintContext);

        // weakness labels
        paintWeaknessContent(paintContext);

        paintLevel(paintContext);

        paintCost(paintContext);

        paintSkillIcons(paintContext);

        paintSlots(paintContext);
//
//        paintStats(paintContext);
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

    private static final Rectangle LEVEL_DRAW_REGION = new Rectangle(30, 76, 92, 44);
    private static final Rectangle NO_LEVEL_DRAW_REGION = new Rectangle(16, 10, 120, 116);

    private void paintLevel(PaintContext paintContext) {
        PlayerCardType playerCardType = getModel().getPlayerCardFieldsModel().getPlayerCardType();

        if (playerCardType == PlayerCardType.Standard || playerCardType == PlayerCardType.Story || playerCardType == PlayerCardType.Neutral || playerCardType == PlayerCardType.Specialist) {
            Integer level = getModel().getPlayerCardFieldsModel().getLevel();
            if (level == null) {
                ImageUtils.drawImage(paintContext.getGraphics(),
                        ImageUtils.loadImage(getClass().getResource("/overlays/no_level.png")),
                        NO_LEVEL_DRAW_REGION);
            } else if (level == 0) {
                // do nothing for level 0
            } else {
                ImageUtils.drawImage(paintContext.getGraphics(),
                        ImageUtils.loadImage(getClass().getResource("/overlays/level_" + getModel().getPlayerCardFieldsModel().getLevel() + ".png")),
                        LEVEL_DRAW_REGION);
            }
        }
    }

    private static final Font COST_FONT = new Font("Arkhamic", Font.PLAIN, 30);
    private static final Rectangle COST_DRAW_REGION = new Rectangle(36, 27, 80, 76);

    private void paintCost(PaintContext paintContext) {
        PaintUtils.drawOutlinedTitle(paintContext.getGraphics(), paintContext.getRenderingDpi(),
                getModel().getPlayerCardFieldsModel().getCost(),
                COST_DRAW_REGION,
                COST_FONT,
                30.0f, 1.6f,
                Color.WHITE,
                Color.BLACK,
                0,
                true);
    }


    private static final List<Rectangle> SKILL_BOX_DRAW_REGIONS = Lists.newArrayList(
            new Rectangle(0, 168, 100, 76),
            new Rectangle(0, 252, 100, 76),
            new Rectangle(0, 336, 100, 76),
            new Rectangle(0, 420, 100, 76),
            new Rectangle(0, 504, 100, 76),
            new Rectangle(0, 588, 100, 76)
    );

    private static final List<Rectangle> SKILL_ICON_DRAW_REGIONS = Lists.newArrayList(
            new Rectangle(21, 178, 50, 52),
            new Rectangle(21, 262, 50, 52),
            new Rectangle(21, 346, 50, 52),
            new Rectangle(21, 430, 50, 52),
            new Rectangle(21, 514, 50, 52),
            new Rectangle(21, 598, 50, 52)
    );

    private void paintSkillIcons(PaintContext paintContext) {
        for (int i = 0; i < getModel().getPlayerCardFieldsModel().getSkillIcons().size(); i++) {
            PlayerCardSkillIcon skillIcon = getModel().getPlayerCardFieldsModel().getSkillIcons().get(i);

            // paint the skill box
            PaintUtils.paintBufferedImage(
                    paintContext.getGraphics(),
                    ImageUtils.loadImage(getClass().getResource("/overlays/skill_box_" + getSkillBoxName() + ".png")),
                    SKILL_BOX_DRAW_REGIONS.get(i)
            );

            // paint the skill icon
            PaintUtils.paintBufferedImage(
                    paintContext.getGraphics(),
                    ImageUtils.loadImage(getClass().getResource(getSkillIconResource(skillIcon))),
                    SKILL_ICON_DRAW_REGIONS.get(i)
            );
        }
    }

    private String getSkillIconResource(PlayerCardSkillIcon skillIcon) {
        PlayerCardType playerCardType = getModel().getPlayerCardFieldsModel().getPlayerCardType();
        String resource = "/overlays/skill_icon_" + skillIcon.name().toLowerCase();

        if (playerCardType == PlayerCardType.BasicWeakness || playerCardType == PlayerCardType.Weakness || playerCardType == PlayerCardType.StoryWeakness)
            resource = resource + "_weakness";

        resource = resource + ".png";

        return resource;
    }

    private String getSkillBoxName() {
        PlayerCardType playerCardType = getModel().getPlayerCardFieldsModel().getPlayerCardType();

        if (getModel().getPlayerCardFieldsModel().getPlayerCardClasses().size() > 2)
            return "multi";

        if (playerCardType == PlayerCardType.Standard)
            return getModel().getPlayerCardFieldsModel().getPlayerCardClasses().get(0).name().toLowerCase();

        if (playerCardType == PlayerCardType.BasicWeakness || playerCardType == PlayerCardType.Weakness || playerCardType == PlayerCardType.StoryWeakness)
            return "weakness";

        return playerCardType.name().toLowerCase();
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
}
