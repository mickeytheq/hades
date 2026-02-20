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
import com.mickeytheq.hades.util.shape.DimensionEx;
import com.mickeytheq.hades.util.shape.RectangleEx;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
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

    public PlayerCardFieldsModel getModel() {
        return playerCardFieldsModel;
    }

    public void createEditors(EditorContext editorContext) {
        // stats/settings for the asset
        typeEditor = EditorUtils.createEnumComboBox(PlayerCardType.class);
        EditorUtils.bindComboBox(typeEditor, editorContext.wrapConsumerWithMarkedChanged(s -> getModel().setCardType((PlayerCardType) typeEditor.getSelectedItem())));

        // first one is not-nullable as when classes are being selected, a minimum of one must always be specified
        playerCardClass1Editor = EditorUtils.createEnumComboBox(PlayerCardClass.class);
        playerCardClass2Editor = EditorUtils.createEnumComboBoxNullable(PlayerCardClass.class);
        playerCardClass3Editor = EditorUtils.createEnumComboBoxNullable(PlayerCardClass.class);

        // for UI purposes neutral is handled by the type not the class but the enum contains it, as it as a valid class
        playerCardClass1Editor.removeItem(PlayerCardClass.Neutral);
        playerCardClass2Editor.removeItem(PlayerCardClass.Neutral);
        playerCardClass3Editor.removeItem(PlayerCardClass.Neutral);

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
        EditorUtils.bindComboBox(typeEditor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setCardType(value)));
        EditorUtils.bindComboBox(playerCardClass1Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setCardClass1(value)));
        EditorUtils.bindComboBox(playerCardClass2Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setCardClass2(value)));
        EditorUtils.bindComboBox(playerCardClass3Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setCardClass3(value)));
        EditorUtils.bindComboBox(playerCardLevelEditor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setLevel(value)));

        if (showCost)
            EditorUtils.bindTextComponent(costEditor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setCost(value)));

        EditorUtils.bindComboBox(skillIcon1Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setSkillIcon1(value)));
        EditorUtils.bindComboBox(skillIcon2Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setSkillIcon2(value)));
        EditorUtils.bindComboBox(skillIcon3Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setSkillIcon3(value)));
        EditorUtils.bindComboBox(skillIcon4Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setSkillIcon4(value)));
        EditorUtils.bindComboBox(skillIcon5Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setSkillIcon5(value)));

        // initialise values
        typeEditor.setSelectedItem(getModel().getCardType());
        playerCardClass1Editor.setSelectedItem(getModel().getCardClass1());
        playerCardClass2Editor.setSelectedItem(getModel().getCardClass2());
        playerCardClass3Editor.setSelectedItem(getModel().getCardClass3());

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

    private static final RectangleEx LEVEL_DRAW_REGION = RectangleEx.millimetres(2.54, 6.43, 7.79, 3.73);
    private static final RectangleEx LEVEL_CIRCLE_DRAW_REGION = RectangleEx.millimetres(1.75, 0.95, 10.25, 10.25);

    public void paintLevelCircleAndPips(PaintContext paintContext) {
        String resourceName = "/overlays/level_circle/" + getLevelCircleOverlayResourcePrefix();

        resourceName = resourceName + ".png";

        PaintUtils.paintBufferedImage(paintContext.getGraphics(),
                ImageUtils.loadImageReadOnly(resourceName),
                paintContext.toPixelRect(LEVEL_CIRCLE_DRAW_REGION));

        // draw the level pips
        paintLevelPips(paintContext);
    }

    public void paintLevelPips(PaintContext paintContext) {
        if (getModel().hasLevel()) {
            PaintUtils.paintBufferedImage(paintContext.getGraphics(),
                    ImageUtils.loadImageReadOnly("/overlays/level_" + getModel().getLevel() + ".png"),
                    paintContext.toPixelRect(LEVEL_DRAW_REGION));
        }
    }

    private String getLevelCircleOverlayResourcePrefix() {
        String prefix = getDefaultResourcePrefix();

        if (!getModel().hasLevel())
            prefix = prefix + "_unleveled";

        return prefix;
    }

    private static final Font COST_FONT = new Font("Arkhamic", Font.PLAIN, 15);
    private static final RectangleEx COST_DRAW_REGION = RectangleEx.millimetres(3.35, 2.09, 6.77, 6.43);

    public void paintCost(PaintContext paintContext) {
        PaintUtils.drawOutlinedTitle(paintContext.getGraphics(), paintContext.getResolutionInPixelsPerInch(),
                getModel().getCost(),
                paintContext.toPixelRect(COST_DRAW_REGION),
                COST_FONT,
                15.0f, 1.6f,
                Color.WHITE,
                Color.BLACK,
                0,
                true);
    }

    private static final DimensionEx SKILL_BOX_SIZE = DimensionEx.millimetres(8.47, 6.43);

    private static final List<RectangleEx> SKILL_BOX_DRAW_REGIONS = Lists.newArrayList(
            RectangleEx.millimetres(0, 14.22, SKILL_BOX_SIZE),
            RectangleEx.millimetres(0, 21.34, SKILL_BOX_SIZE),
            RectangleEx.millimetres(0, 28.45, SKILL_BOX_SIZE),
            RectangleEx.millimetres(0, 35.56, SKILL_BOX_SIZE),
            RectangleEx.millimetres(0, 42.67, SKILL_BOX_SIZE),
            RectangleEx.millimetres(0, 49.78, SKILL_BOX_SIZE)
    );

    private static final DimensionEx SKILL_ICON_SIZE = DimensionEx.millimetres(4.23, 4.4);

    private static final List<RectangleEx> SKILL_ICON_DRAW_REGIONS = Lists.newArrayList(
            RectangleEx.millimetres(1.78, 15.07, SKILL_ICON_SIZE),
            RectangleEx.millimetres(1.78, 22.18, SKILL_ICON_SIZE),
            RectangleEx.millimetres(1.78, 29.29, SKILL_ICON_SIZE),
            RectangleEx.millimetres(1.78, 36.41, SKILL_ICON_SIZE),
            RectangleEx.millimetres(1.78, 43.52, SKILL_ICON_SIZE),
            RectangleEx.millimetres(1.78, 50.63, SKILL_ICON_SIZE)
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
                    ImageUtils.loadImageReadOnly(getClass().getResource("/overlays/skill_box_" + getDefaultResourcePrefix() + ".png")),
                    paintContext.toPixelRect(SKILL_BOX_DRAW_REGIONS.get(i))
            );

            // paint the skill icon
            PaintUtils.paintBufferedImage(
                    paintContext.getGraphics(),
                    ImageUtils.loadImageReadOnly(getClass().getResource(getSkillIconResource(skillIcon))),
                    paintContext.toPixelRect(SKILL_ICON_DRAW_REGIONS.get(i))
            );
        }
    }

    private String getSkillIconResource(PlayerCardSkillIcon skillIcon) {
        PlayerCardType playerCardType = getModel().getCardType();
        String resource = "/icons/skill_icons/" + skillIcon.name().toLowerCase();

        if (playerCardType == PlayerCardType.BasicWeakness || playerCardType == PlayerCardType.Weakness || playerCardType == PlayerCardType.StoryWeakness)
            resource = resource + "_monochrome";

        resource = resource + ".png";

        return resource;
    }

    // gets a resource prefix typically used in the name of images to be loaded
    // for example the asset templates are named using this convention so the AssetView can call this to do most of
    // the work when locating the template
    public String getDefaultResourcePrefix() {
        PlayerCardType playerCardType = getModel().getCardType();

        if (playerCardType.isWeakness())
            return "weakness";

        // story cards render as neutral cards - story sometimes needs additional overlays, handled elsewhere
        if (playerCardType == PlayerCardType.Story)
            return "neutral";

        if (getModel().isMultiClass())
            return "multiclass";

        // single class - must be only one in the list
        if (playerCardType == PlayerCardType.Standard)
            return getModel().getPlayerCardClasses().get(0).name().toLowerCase();

        return playerCardType.name().toLowerCase();
    }

    // paints the circle to contain encounter set icon or basic weakness icon
    public void paintEncounterSetIconCircle(PaintContext paintContext, Rectangle drawRegion) {
        PlayerCardType playerCardType = getModel().getCardType();

        // for basic and story weaknesses an overlay is required for the encounter icon or basic weakness icon
        if (playerCardType == PlayerCardType.BasicWeakness || playerCardType == PlayerCardType.StoryWeakness) {
            BufferedImage overlay = ImageUtils.loadImageReadOnly("/overlays/encounter_set_container/weakness.png");
            PaintUtils.paintBufferedImage(paintContext.getGraphics(), overlay, drawRegion);
            return;
        }

        if (playerCardType == PlayerCardType.Story) {
            BufferedImage overlay = ImageUtils.loadImageReadOnly("/overlays/encounter_set_container/story.png");
            PaintUtils.paintBufferedImage(paintContext.getGraphics(), overlay, drawRegion);
            return;
        }
    }
}
