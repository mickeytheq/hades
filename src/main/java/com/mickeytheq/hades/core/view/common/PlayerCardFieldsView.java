package com.mickeytheq.hades.core.view.common;

import com.google.common.collect.Lists;
import com.mickeytheq.hades.core.view.EditorContext;
import com.mickeytheq.hades.core.model.common.PlayerCardClass;
import com.mickeytheq.hades.core.model.common.PlayerCardFieldsModel;
import com.mickeytheq.hades.core.model.common.PlayerCardSkillIcon;
import com.mickeytheq.hades.core.model.common.PlayerCardType;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.utils.EditorUtils;
import com.mickeytheq.hades.core.view.utils.ImageUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.core.view.utils.PaintUtils;
import com.mickeytheq.hades.util.shape.RectangleEx;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.IntStream;

public class PlayerCardFieldsView {
    private final PlayerCardFieldsModel playerCardFieldsModel;

    private final boolean showCost;

    private JComboBox<PlayerCardType> typeEditor;
    private JComboBox<PlayerCardClass> playerCardClass1Editor;
    private JComboBox<PlayerCardClass> playerCardClass2Editor;
    private JComboBox<PlayerCardClass> playerCardClass3Editor;
    private JTextField costEditor;
    private JComboBox<Integer> playerCardLevelEditor;
    private JComboBox<PlayerCardSkillIcon> skillIcon1Editor;
    private JComboBox<PlayerCardSkillIcon> skillIcon2Editor;
    private JComboBox<PlayerCardSkillIcon> skillIcon3Editor;
    private JComboBox<PlayerCardSkillIcon> skillIcon4Editor;
    private JComboBox<PlayerCardSkillIcon> skillIcon5Editor;

    public PlayerCardFieldsView(PlayerCardFieldsModel playerCardFieldsModel, boolean showCost) {
        this.playerCardFieldsModel = playerCardFieldsModel;
        this.showCost = showCost;
    }

    public String getTemplateName() {
        PlayerCardType playerCardType = getModel().getPlayerCardType();
        List<PlayerCardClass> cardClasses = getModel().getPlayerCardClasses();

        if (playerCardType == PlayerCardType.Standard) {
            if (cardClasses.size() > 1)
                return "multi";
            else
                return cardClasses.get(0).name().toLowerCase();
        }

        switch (playerCardType) {
            case Neutral:
                return "neutral";

            case Specialist:
                return "specialist";

            case Story:
                return "story";

            case StoryWeakness:
            case Weakness:
            case BasicWeakness:
                return "weakness";

            default:
                throw new RuntimeException("Unsupported player card type " + playerCardType.name());
        }
    }

    public PlayerCardFieldsModel getModel() {
        return playerCardFieldsModel;
    }

    public void createEditors(EditorContext editorContext) {
        // stats/settings for the asset
        typeEditor = EditorUtils.createEnumComboBox(PlayerCardType.class);
        EditorUtils.bindComboBox(typeEditor, editorContext.wrapConsumerWithMarkedChanged(s -> getModel().setPlayerCardType((PlayerCardType) typeEditor.getSelectedItem())));

        // first one is not-nullable as when classes are being selected, a minimum of one must always be specified
        playerCardClass1Editor = EditorUtils.createEnumComboBox(PlayerCardClass.class);
        playerCardClass2Editor = EditorUtils.createEnumComboBoxNullable(PlayerCardClass.class);
        playerCardClass3Editor = EditorUtils.createEnumComboBoxNullable(PlayerCardClass.class);

        typeEditor.addItemListener(e -> {
            PlayerCardType playerCardType = (PlayerCardType) typeEditor.getSelectedItem();

            boolean enableClassEditors = playerCardType == PlayerCardType.Standard;

            playerCardClass1Editor.setEnabled(enableClassEditors);
            playerCardClass2Editor.setEnabled(enableClassEditors);
            playerCardClass3Editor.setEnabled(enableClassEditors);
        });

        if (showCost)
            costEditor = EditorUtils.createTextField(30);

        playerCardLevelEditor = EditorUtils.createNullableComboBox();
        IntStream.rangeClosed(0, 5).forEach(value -> playerCardLevelEditor.addItem(value));

        // TODO: there are max 6 skill icons for skills
        skillIcon1Editor = EditorUtils.createEnumComboBoxNullable(PlayerCardSkillIcon.class);
        skillIcon2Editor = EditorUtils.createEnumComboBoxNullable(PlayerCardSkillIcon.class);
        skillIcon3Editor = EditorUtils.createEnumComboBoxNullable(PlayerCardSkillIcon.class);
        skillIcon4Editor = EditorUtils.createEnumComboBoxNullable(PlayerCardSkillIcon.class);
        skillIcon5Editor = EditorUtils.createEnumComboBoxNullable(PlayerCardSkillIcon.class);

        // bindings
        EditorUtils.bindComboBox(typeEditor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setPlayerCardType(value)));
        EditorUtils.bindComboBox(playerCardClass1Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setPlayerCardClass1(value)));
        EditorUtils.bindComboBox(playerCardClass2Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setPlayerCardClass2(value)));
        EditorUtils.bindComboBox(playerCardClass3Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setPlayerCardClass3(value)));
        EditorUtils.bindComboBox(playerCardLevelEditor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setLevel(value)));

        if (showCost)
            EditorUtils.bindTextComponent(costEditor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setCost(value)));

        EditorUtils.bindComboBox(skillIcon1Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setSkillIcon1(value)));
        EditorUtils.bindComboBox(skillIcon2Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setSkillIcon2(value)));
        EditorUtils.bindComboBox(skillIcon3Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setSkillIcon3(value)));
        EditorUtils.bindComboBox(skillIcon4Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setSkillIcon4(value)));
        EditorUtils.bindComboBox(skillIcon5Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setSkillIcon5(value)));

        // initialise values
        typeEditor.setSelectedItem(getModel().getPlayerCardType());
        playerCardClass1Editor.setSelectedItem(getModel().getPlayerCardClass1());
        playerCardClass2Editor.setSelectedItem(getModel().getPlayerCardClass2());
        playerCardClass3Editor.setSelectedItem(getModel().getPlayerCardClass3());

        if (showCost)
            costEditor.setText(getModel().getCost());

        playerCardLevelEditor.setSelectedItem(getModel().getLevel());
        skillIcon1Editor.setSelectedItem(getModel().getSkillIcon1());
        skillIcon2Editor.setSelectedItem(getModel().getSkillIcon2());
        skillIcon3Editor.setSelectedItem(getModel().getSkillIcon3());
        skillIcon4Editor.setSelectedItem(getModel().getSkillIcon4());
        skillIcon5Editor.setSelectedItem(getModel().getSkillIcon5());
    }

    public void layoutFirstColumnLabels(JPanel panel) {
        MigLayoutUtils.addLabel(panel, "Type"); // TODO: i18n
        MigLayoutUtils.addLabel(panel, Language.string(InterfaceConstants.CLASS1));
        MigLayoutUtils.addLabel(panel, Language.string(InterfaceConstants.CLASS2));
        MigLayoutUtils.addLabel(panel, Language.string(InterfaceConstants.CLASS3));

        if (showCost)
            MigLayoutUtils.addLabel(panel, Language.string(InterfaceConstants.COST));

        MigLayoutUtils.addLabel(panel, Language.string(InterfaceConstants.LEVEL));
    }

    public void layoutSecondColumnEditors(JPanel panel) {
        panel.add(typeEditor, "newline");
        panel.add(playerCardClass1Editor);
        panel.add(playerCardClass2Editor);
        panel.add(playerCardClass3Editor);

        if (showCost)
            panel.add(costEditor);

        panel.add(playerCardLevelEditor);
    }

    public void layoutThirdColumnLabels(JPanel panel) {
        MigLayoutUtils.addLabel(panel, Language.string(InterfaceConstants.ICON) + " 1", "newline");
        MigLayoutUtils.addLabel(panel, Language.string(InterfaceConstants.ICON) + " 2");
        MigLayoutUtils.addLabel(panel, Language.string(InterfaceConstants.ICON) + " 3");
        MigLayoutUtils.addLabel(panel, Language.string(InterfaceConstants.ICON) + " 4");
        MigLayoutUtils.addLabel(panel, Language.string(InterfaceConstants.ICON) + " 5");
    }

    public void layoutFourthColumnEditors(JPanel panel) {
        panel.add(skillIcon1Editor, "newline");
        panel.add(skillIcon2Editor);
        panel.add(skillIcon3Editor);
        panel.add(skillIcon4Editor);
        panel.add(skillIcon5Editor);
    }

    private static final RectangleEx LEVEL_DRAW_REGION = RectangleEx.millimeters(2.54, 6.43, 7.79, 3.73);
    private static final RectangleEx NO_LEVEL_DRAW_REGION = RectangleEx.millimeters(1.35, 0.85, 10.16, 9.82);

    public void paintLevel(PaintContext paintContext) {
        PlayerCardType playerCardType = getModel().getPlayerCardType();

        if (playerCardType == PlayerCardType.Standard || playerCardType == PlayerCardType.Story || playerCardType == PlayerCardType.Neutral || playerCardType == PlayerCardType.Specialist) {
            Integer level = getModel().getLevel();
            if (level == null) {
                ImageUtils.drawImage(paintContext.getGraphics(),
                        ImageUtils.loadImage(getClass().getResource("/overlays/no_level.png")),
                        paintContext.toPixelRect(NO_LEVEL_DRAW_REGION));
            } else if (level == 0) {
                // do nothing for level 0
            } else {
                ImageUtils.drawImage(paintContext.getGraphics(),
                        ImageUtils.loadImage(getClass().getResource("/overlays/level_" + getModel().getLevel() + ".png")),
                        paintContext.toPixelRect(LEVEL_DRAW_REGION));
            }
        }
    }

    private static final Font COST_FONT = new Font("Arkhamic", Font.PLAIN, 15);
    private static final RectangleEx COST_DRAW_REGION = RectangleEx.millimeters(3.05, 2.29, 6.77, 6.43);

    public void paintCost(PaintContext paintContext) {
        PaintUtils.drawOutlinedTitle(paintContext.getGraphics(), paintContext.getRenderingDpi(),
                getModel().getCost(),
                paintContext.toPixelRect(COST_DRAW_REGION),
                COST_FONT,
                15.0f, 1.6f,
                Color.WHITE,
                Color.BLACK,
                0,
                true);
    }


    private static final List<RectangleEx> SKILL_BOX_DRAW_REGIONS = Lists.newArrayList(
            RectangleEx.millimeters(0, 14.22, 8.47, 6.43),
            RectangleEx.millimeters(0, 21.34, 8.47, 6.43),
            RectangleEx.millimeters(0, 28.45, 8.47, 6.43),
            RectangleEx.millimeters(0, 35.56, 8.47, 6.43),
            RectangleEx.millimeters(0, 42.67, 8.47, 6.43),
            RectangleEx.millimeters(0, 49.78, 8.47, 6.43)
    );

    private static final List<RectangleEx> SKILL_ICON_DRAW_REGIONS = Lists.newArrayList(
            RectangleEx.millimeters(1.78, 15.07, 4.23, 4.4),
            RectangleEx.millimeters(1.78, 22.18, 4.23, 4.4),
            RectangleEx.millimeters(1.78, 29.29, 4.23, 4.4),
            RectangleEx.millimeters(1.78, 36.41, 4.23, 4.4),
            RectangleEx.millimeters(1.78, 43.52, 4.23, 4.4),
            RectangleEx.millimeters(1.78, 50.63, 4.23, 4.4)
    );

    // TODO: at the moment there's a list of box regions and icon regions
    // which when tweaked is easy to mess up the consistent box spacing and icon positioning within a box
    // instead could an algorithm that paints based on a smaller number of configs
    // - specify the position of the first box
    // - specify a gap between boxes
    // - specify a size of each box
    // - specify the relative position of an icon within a box
    // - specify the size of an icon within a box
    public void paintSkillIcons(PaintContext paintContext) {
        for (int i = 0; i < getModel().getSkillIcons().size(); i++) {
            PlayerCardSkillIcon skillIcon = getModel().getSkillIcons().get(i);

            // paint the skill box
            PaintUtils.paintBufferedImage(
                    paintContext.getGraphics(),
                    ImageUtils.loadImage(getClass().getResource("/overlays/skill_box_" + getSkillBoxName() + ".png")),
                    paintContext.toPixelRect(SKILL_BOX_DRAW_REGIONS.get(i))
            );

            // paint the skill icon
            PaintUtils.paintBufferedImage(
                    paintContext.getGraphics(),
                    ImageUtils.loadImage(getClass().getResource(getSkillIconResource(skillIcon))),
                    paintContext.toPixelRect(SKILL_ICON_DRAW_REGIONS.get(i))
            );
        }
    }

    private String getSkillIconResource(PlayerCardSkillIcon skillIcon) {
        PlayerCardType playerCardType = getModel().getPlayerCardType();
        String resource = "/overlays/skill_icon_" + skillIcon.name().toLowerCase();

        if (playerCardType == PlayerCardType.BasicWeakness || playerCardType == PlayerCardType.Weakness || playerCardType == PlayerCardType.StoryWeakness)
            resource = resource + "_weakness";

        resource = resource + ".png";

        return resource;
    }

    private String getSkillBoxName() {
        PlayerCardType playerCardType = getModel().getPlayerCardType();

        if (getModel().getPlayerCardClasses().size() > 2)
            return "multi";

        if (playerCardType == PlayerCardType.Standard)
            return getModel().getPlayerCardClasses().get(0).name().toLowerCase();

        if (playerCardType == PlayerCardType.BasicWeakness || playerCardType == PlayerCardType.Weakness || playerCardType == PlayerCardType.StoryWeakness)
            return "weakness";

        return playerCardType.name().toLowerCase();
    }
}
