package com.mickeytheq.ahlcg4j.core.view.common;

import com.mickeytheq.ahlcg4j.core.view.EditorContext;
import com.mickeytheq.ahlcg4j.core.model.common.PlayerCardClass;
import com.mickeytheq.ahlcg4j.core.model.common.PlayerCardFieldsModel;
import com.mickeytheq.ahlcg4j.core.model.common.PlayerCardSkillIcon;
import com.mickeytheq.ahlcg4j.core.model.common.PlayerCardType;
import com.mickeytheq.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.ahlcg4j.core.view.utils.EditorUtils;
import com.mickeytheq.ahlcg4j.core.view.utils.MigLayoutUtils;
import resources.Language;

import javax.swing.*;
import java.util.stream.IntStream;

public class PlayerCardFieldsView {
    private final PlayerCardFieldsModel playerCardFieldsModel;

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

    public PlayerCardFieldsView(PlayerCardFieldsModel playerCardFieldsModel) {
        this.playerCardFieldsModel = playerCardFieldsModel;
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
        MigLayoutUtils.addLabel(panel, Language.string(InterfaceConstants.COST));
        MigLayoutUtils.addLabel(panel, Language.string(InterfaceConstants.LEVEL));
    }

    public void layoutSecondColumnEditors(JPanel panel) {
        panel.add(typeEditor, "newline");
        panel.add(playerCardClass1Editor);
        panel.add(playerCardClass2Editor);
        panel.add(playerCardClass3Editor);
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
}
