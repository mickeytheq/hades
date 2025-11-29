package com.mickeytheq.strangeeons.ahlcg4j.cardfaces.asset;

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

    private static final Rectangle ART_PORTRAIT_DRAW_REGION = new Rectangle(10, 40, 358, 258);
    private static final Rectangle ENCOUNTER_PORTRAIT_DRAW_REGION = new Rectangle(329, 10, 30, 30);
    private static final Rectangle COLLECTION_PORTRAIT_DRAW_REGION = new Rectangle(320, 508, 13, 13);

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
        if (getModel().getPlayerCardFieldsModel().getPlayerCardType() == PlayerCardType.Standard) {
            List<PlayerCardClass> cardClasses = Stream.of(
                            getModel().getPlayerCardFieldsModel().getPlayerCardClass1(),
                            getModel().getPlayerCardFieldsModel().getPlayerCardClass2(),
                            getModel().getPlayerCardFieldsModel().getPlayerCardClass3())
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());

            if (cardClasses.size() > 1)
                return "/templates/asset/AHLCG-Asset-MultiClass.jp2";
            else
                return "/templates/asset/AHLCG-Asset-" + cardClasses.get(0) + ".jp2";
        }

        switch (getModel().getPlayerCardFieldsModel().getPlayerCardType()) {
            case Neutral:
                return "/templates/asset/AHLCG-Asset-Neutral.jp2";

            case Specialist:
                return "/templates/asset/AHLCG-Asset-Specialist.jp2";

            case Story:
                return "/templates/asset/AHLCG-Asset-Story.jp2";

            case StoryWeakness:
                return "/templates/asset/AHLCG-Asset-StoryWeakness.jp2";

            case Weakness:
            case BasicWeakness:
                return "/templates/asset/AHLCG-Asset-Weakness.jp2";

            default:
                throw new RuntimeException("Unsupported player card type " + getModel().getPlayerCardFieldsModel().getPlayerCardType().name());
        }

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

    private static final Rectangle LABEL_DRAW_REGION = new Rectangle(20, 62, 38, 14);
    private static final Rectangle TITLE_DRAW_REGION = new Rectangle(68, 14, 238, 29);
    private static final Rectangle BODY_DRAW_REGION = new Rectangle(20, 320, 336, 140);


    @Override
    public void paint(PaintContext paintContext) {
        // paint the main/art portrait first as it sits behind the card template
        commonCardFieldsView.paintArtPortrait(paintContext);

        // draw the template
        paintContext.getGraphics().drawImage(getTemplateImage(), 0, 0, null);

        // label
        PaintUtils.paintLabel(paintContext, LABEL_DRAW_REGION, Language.gstring(GameConstants.LABEL_ASSET).toUpperCase());

        // title
        commonCardFieldsView.paintTitle(paintContext, TITLE_DRAW_REGION);

        commonCardFieldsView.paintBodyCopyrightArtist(paintContext, BODY_DRAW_REGION);

        numberingView.paintCollectionPortrait(paintContext, true);
        numberingView.paintCollectionNumber(paintContext);
    }

}
