package com.mickeytheq.strangeeons.ahlcg4j.cardfaces.asset;

import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.*;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.PaintContext;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.common.PlayerCardClass;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.common.PlayerCardSkillIcon;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.common.PlayerCardType;
import com.mickeytheq.strangeeons.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.strangeeons.ahlcg4j.ui.component.StatisticComponent;
import com.mickeytheq.strangeeons.ahlcg4j.util.EditorUtils;
import com.mickeytheq.strangeeons.ahlcg4j.util.ImageUtils;
import com.mickeytheq.strangeeons.ahlcg4j.util.MigLayoutUtils;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.stream.IntStream;

public class AssetView extends BaseCardFaceView<Asset> {
    private JComboBox<PlayerCardType> assetTypeEditor;
    private JComboBox<PlayerCardClass> playerCardClass1Editor;
    private JComboBox<PlayerCardClass> playerCardClass2Editor;
    private JComboBox<PlayerCardClass> playerCardClass3Editor;
    private JTextField costEditor;
    private JComboBox<Integer> playerCardLevelEditor;
    private JComboBox<Asset.AssetSlot> assetSlot1Editor;
    private JComboBox<Asset.AssetSlot> assetSlot2Editor;
    private StatisticComponent healthEditor;
    private StatisticComponent sanityEditor;
    private JComboBox<PlayerCardSkillIcon> skillIcon1Editor;
    private JComboBox<PlayerCardSkillIcon> skillIcon2Editor;
    private JComboBox<PlayerCardSkillIcon> skillIcon3Editor;
    private JComboBox<PlayerCardSkillIcon> skillIcon4Editor;
    private JComboBox<PlayerCardSkillIcon> skillIcon5Editor;

    private CommonCardFieldsView commonCardFieldsView;
    private NumberingView numberingView;

    private static final Rectangle ART_PORTRAIT_DRAW_REGION = new Rectangle(10, 40, 358, 258);
    private static final Rectangle ENCOUNTER_PORTRAIT_DRAW_REGION = new Rectangle(329, 10, 30, 30);
    private static final Rectangle COLLECTION_PORTRAIT_DRAW_REGION = new Rectangle(320, 508, 13, 13);

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel(), getViewContext(), ART_PORTRAIT_DRAW_REGION);
        numberingView = new NumberingView(getModel().getNumberingModel(), getViewContext(), COLLECTION_PORTRAIT_DRAW_REGION, ENCOUNTER_PORTRAIT_DRAW_REGION);
    }

    @Override
    public BufferedImage loadTemplateImage() {
        String templateResource;

        if (getModel().getPlayerCardType() == PlayerCardType.Standard) {
            templateResource = "/templates/asset/AHLCG-Asset-" + getModel().getPlayerCardClass1().name() + ".jp2";
        }
        else {
            templateResource = "/templates/asset/AHLCG-Asset-" + PlayerCardClass.Guardian.name() + ".jp2";
        }

        return ImageUtils.loadImage(getClass().getResource(templateResource));
    }

    @Override
    protected void paint(PaintContext paintContext) {
        // draw the template
        paintContext.getGraphics().drawImage(loadTemplateImage(), 0, 0, null);
    }

    @Override
    public void createEditors(JTabbedPane tabbedPane) {
        createTitleAndStatisticsEditors(tabbedPane);

        createRulesAndPortraitTab(tabbedPane);

        tabbedPane.addTab("Collection / encounter", numberingView.createStandardCollectionEncounterPanel());
    }

    private void createTitleAndStatisticsEditors(JTabbedPane tabbedPane) {
        // title
        // TODO: sub-title
        JPanel titlePanel = MigLayoutUtils.createPanel(Language.string(InterfaceConstants.TITLE));
        commonCardFieldsView.addTitleEditorToPanel(titlePanel);

        // stats/settings for the asset
        assetTypeEditor = EditorUtils.createEnumComboBox(PlayerCardType.class);
        EditorUtils.bindComboBox(assetTypeEditor, getViewContext().wrapConsumerWithMarkedChanged(s -> getModel().setPlayerCardType((PlayerCardType) assetTypeEditor.getSelectedItem())));

        // first one is not-nullable as when classes are being selected, a minimum of one must always be specified
        playerCardClass1Editor = EditorUtils.createEnumComboBox(PlayerCardClass.class);
        playerCardClass2Editor = EditorUtils.createEnumComboBoxNullable(PlayerCardClass.class);
        playerCardClass3Editor = EditorUtils.createEnumComboBoxNullable(PlayerCardClass.class);

        assetTypeEditor.addItemListener(e -> {
            PlayerCardType playerCardType = (PlayerCardType) assetTypeEditor.getSelectedItem();

            boolean enableClassEditors = playerCardType == PlayerCardType.Standard;

            playerCardClass1Editor.setEnabled(enableClassEditors);
            playerCardClass2Editor.setEnabled(enableClassEditors);
            playerCardClass3Editor.setEnabled(enableClassEditors);
        });

        costEditor = EditorUtils.createTextField(30);
        playerCardLevelEditor = EditorUtils.createNullableComboBox();
        IntStream.rangeClosed(0, 5).forEach(value -> playerCardLevelEditor.addItem(value));

        assetSlot1Editor = EditorUtils.createEnumComboBoxNullable(Asset.AssetSlot.class);
        assetSlot2Editor = EditorUtils.createEnumComboBoxNullable(Asset.AssetSlot.class);

        healthEditor = new StatisticComponent();
        sanityEditor = new StatisticComponent();

        skillIcon1Editor = EditorUtils.createEnumComboBoxNullable(PlayerCardSkillIcon.class);
        skillIcon2Editor = EditorUtils.createEnumComboBoxNullable(PlayerCardSkillIcon.class);
        skillIcon3Editor = EditorUtils.createEnumComboBoxNullable(PlayerCardSkillIcon.class);
        skillIcon4Editor = EditorUtils.createEnumComboBoxNullable(PlayerCardSkillIcon.class);
        skillIcon5Editor = EditorUtils.createEnumComboBoxNullable(PlayerCardSkillIcon.class);

        // layout
        //
        // use vertical flow layout to aid readability
        // each column of controls will be added/built one after another instead of row by row
        // we'll use 'newline' constraint on the component at the beginning of a new column
        MigLayout migLayout = new MigLayout(new LC().flowY());

        // this constraint
        // - makes the 2 control columns grow/fill all available space
        // - adds a small visual gap between the two sets of vertical labels/controls
        // - ensures the last column has a reasonable minimum size
        migLayout.setColumnConstraints("[][grow, fill]10[][grow, fill, :200:]");

        JPanel statsPanel = new JPanel(migLayout);
        statsPanel.setBorder(BorderFactory.createTitledBorder("Stats")); // TODO: i18n

        MigLayoutUtils.addLabel(statsPanel, "Asset type");
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.CLASS1));
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.CLASS2));
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.CLASS3));
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.COST));
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.LEVEL));
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.SLOT1));
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.SLOT2));
        MigLayoutUtils.addLabel(statsPanel, "Health"); // TODO: i18n
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.SANITY));

        statsPanel.add(assetTypeEditor, "newline");
        statsPanel.add(playerCardClass1Editor);
        statsPanel.add(playerCardClass2Editor);
        statsPanel.add(playerCardClass3Editor);
        statsPanel.add(costEditor);
        statsPanel.add(playerCardLevelEditor);
        statsPanel.add(assetSlot1Editor);
        statsPanel.add(assetSlot2Editor);
        statsPanel.add(healthEditor);
        statsPanel.add(sanityEditor);

        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.ICON) + " 1", "newline");
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.ICON) + " 2");
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.ICON) + " 3");
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.ICON) + " 4");
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.ICON) + " 5");

        statsPanel.add(skillIcon1Editor, "newline");
        statsPanel.add(skillIcon2Editor);
        statsPanel.add(skillIcon3Editor);
        statsPanel.add(skillIcon4Editor);
        statsPanel.add(skillIcon5Editor);

        JPanel mainPanel = MigLayoutUtils.createPanel();
        mainPanel.add(titlePanel, "wrap, growx, pushx");
        mainPanel.add(statsPanel, "wrap, growx, pushx");

        tabbedPane.addTab(Language.string(InterfaceConstants.ASSET) + " - " + "Stats", mainPanel); // TODO: i18n

        ViewContext viewContext = getViewContext();

        // bindings
        EditorUtils.bindComboBox(assetTypeEditor, viewContext.wrapConsumerWithMarkedChanged(value -> getModel().setPlayerCardType(value)));
        EditorUtils.bindComboBox(playerCardClass1Editor, viewContext.wrapConsumerWithMarkedChanged(value -> getModel().setPlayerCardClass1(value)));
        EditorUtils.bindComboBox(playerCardClass2Editor, viewContext.wrapConsumerWithMarkedChanged(value -> getModel().setPlayerCardClass2(value)));
        EditorUtils.bindComboBox(playerCardClass3Editor, viewContext.wrapConsumerWithMarkedChanged(value -> getModel().setPlayerCardClass3(value)));
        EditorUtils.bindTextComponent(costEditor, viewContext.wrapConsumerWithMarkedChanged(value -> getModel().setCost(value)));
        EditorUtils.bindComboBox(assetSlot1Editor, viewContext.wrapConsumerWithMarkedChanged(value -> getModel().setAssetSlot1(value)));
        EditorUtils.bindComboBox(assetSlot2Editor, viewContext.wrapConsumerWithMarkedChanged(value -> getModel().setAssetSlot2(value)));
        EditorUtils.bindStatisticComponent(healthEditor, viewContext.wrapConsumerWithMarkedChanged(value -> getModel().setHealth(value)));
        EditorUtils.bindStatisticComponent(sanityEditor, viewContext.wrapConsumerWithMarkedChanged(value -> getModel().setSanity(value)));

        EditorUtils.bindComboBox(skillIcon1Editor, viewContext.wrapConsumerWithMarkedChanged(value -> getModel().setSkillIcon1(value)));
        EditorUtils.bindComboBox(skillIcon2Editor, viewContext.wrapConsumerWithMarkedChanged(value -> getModel().setSkillIcon2(value)));
        EditorUtils.bindComboBox(skillIcon3Editor, viewContext.wrapConsumerWithMarkedChanged(value -> getModel().setSkillIcon3(value)));
        EditorUtils.bindComboBox(skillIcon4Editor, viewContext.wrapConsumerWithMarkedChanged(value -> getModel().setSkillIcon4(value)));
        EditorUtils.bindComboBox(skillIcon5Editor, viewContext.wrapConsumerWithMarkedChanged(value -> getModel().setSkillIcon5(value)));

        // initialise values
        assetTypeEditor.setSelectedItem(getModel().getPlayerCardType());
        playerCardClass1Editor.setSelectedItem(getModel().getPlayerCardClass1());
        playerCardClass2Editor.setSelectedItem(getModel().getPlayerCardClass2());
        playerCardClass3Editor.setSelectedItem(getModel().getPlayerCardClass3());
        costEditor.setText(getModel().getCost());
        playerCardLevelEditor.setSelectedItem(getModel().getLevel());
        assetSlot1Editor.setSelectedItem(getModel().getAssetSlot1());
        assetSlot2Editor.setSelectedItem(getModel().getAssetSlot2());
        healthEditor.setStatistic(getModel().getHealth());
        sanityEditor.setStatistic(getModel().getSanity());

        skillIcon1Editor.setSelectedItem(getModel().getSkillIcon1());
        skillIcon2Editor.setSelectedItem(getModel().getSkillIcon2());
        skillIcon3Editor.setSelectedItem(getModel().getSkillIcon3());
        skillIcon4Editor.setSelectedItem(getModel().getSkillIcon4());
        skillIcon5Editor.setSelectedItem(getModel().getSkillIcon5());
    }

    private void createRulesAndPortraitTab(JTabbedPane tabbedPane) {
        JPanel generalPanel = MigLayoutUtils.createPanel("General"); // TODO: i18n
        commonCardFieldsView.addNonTitleEditorsToPanel(generalPanel);

        JPanel mainPanel = new JPanel(new MigLayout());

        mainPanel.add(generalPanel, "wrap, pushx, growx");
        mainPanel.add(commonCardFieldsView.createStandardArtPanel(), "wrap, pushx, growx");

        // add the panel to the main tab control
        tabbedPane.addTab("Rules / portrait", mainPanel); // TODO: i18n
    }
}
