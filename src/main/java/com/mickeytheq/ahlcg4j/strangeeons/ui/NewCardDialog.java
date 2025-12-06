package com.mickeytheq.ahlcg4j.strangeeons.ui;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.ui.DocumentEventAdapter;
import com.mickeytheq.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.ahlcg4j.core.CardFaceTypeRegister;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.*;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.Event;
import com.mickeytheq.ahlcg4j.core.view.utils.MigLayoutUtils;
import com.mickeytheq.ahlcg4j.ui.DialogWithButtons;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;

public class NewCardDialog extends DialogWithButtons {
    private JComboBox<BothFacesOption> bothFacesOptionEditor;
    private JComboBox<CardFaceTypeRegister.CardFaceInfo> frontFaceOptionEditor;
    private JComboBox<CardFaceTypeRegister.CardFaceInfo> backFaceOptionEditor;

    private JTextField filenameEditor;

    // TODO: add option to specify title/filename when creating a new card
    // when this dialog is launched from the regular Strange Eons 'new game component' code path it cannot be cancelled
    public NewCardDialog() {
        super(StrangeEons.getWindow(), true);

        initialise();
    }

    private void initialise() {
        frontFaceOptionEditor = new JComboBox<>();
        backFaceOptionEditor = new JComboBox<>();

        CardFaceTypeRegister cardFaceTypeRegister = CardFaceTypeRegister.get();

        cardFaceTypeRegister.getAllCardInformation().forEach(o -> {
            frontFaceOptionEditor.addItem(o);
            backFaceOptionEditor.addItem(o);
        });

        // the combo box that selects general card types that drives selection of both faces
        bothFacesOptionEditor = new JComboBox<>();

        JLabel helpLabel = new JLabel("<html>To create a new card you can either: <br><br>" +
                "&nbsp;&nbsp;• Select a <b>Card type</b> below and the appropriate default faces will be generated. This is the appropriate option in most cases<br><br>" +
                "&nbsp;&nbsp;• Use the <b>Custom</b> settings to select both faces manually. Use this when you need a more unusual face combination<br></html>");

        JLabel description = new JLabel();

        bothFacesOptionEditor.addItemListener(e -> {
            BothFacesOption bothFacesOption = (BothFacesOption)bothFacesOptionEditor.getSelectedItem();

            frontFaceOptionEditor.setSelectedItem(bothFacesOption.getFrontFace());
            backFaceOptionEditor.setSelectedItem(bothFacesOption.getBackFace());

            description.setText(bothFacesOption.getDescription());
        });

        // TODO: i18n for descriptions
        bothFacesOptionEditor.addItem(new BothFacesOption(Language.string(InterfaceConstants.ASSET), "Asset with player card back",
                cardFaceTypeRegister.getInfoForCardFaceModelClass(Asset.class),
                cardFaceTypeRegister.getInfoForCardFaceModelClass(PlayerCardBack.class)));
        bothFacesOptionEditor.addItem(new BothFacesOption(Language.string(InterfaceConstants.EVENT), "Event with player card back",
                cardFaceTypeRegister.getInfoForCardFaceModelClass(Event.class),
                cardFaceTypeRegister.getInfoForCardFaceModelClass(PlayerCardBack.class)));
        bothFacesOptionEditor.addItem(new BothFacesOption(Language.string(InterfaceConstants.SKILL), "Skill with player card back",
                cardFaceTypeRegister.getInfoForCardFaceModelClass(Skill.class),
                cardFaceTypeRegister.getInfoForCardFaceModelClass(PlayerCardBack.class)));

        bothFacesOptionEditor.addItem(new BothFacesOption(Language.string(InterfaceConstants.INVESTIGATOR), "Investigator",
                cardFaceTypeRegister.getInfoForCardFaceModelClass(Investigator.class),
                cardFaceTypeRegister.getInfoForCardFaceModelClass(InvestigatorBack.class)));

        bothFacesOptionEditor.addItem(new BothFacesOption(Language.string(InterfaceConstants.TREACHERY), "Treachery with encounter card back",
                cardFaceTypeRegister.getInfoForCardFaceModelClass(Treachery.class),
                cardFaceTypeRegister.getInfoForCardFaceModelClass(EncounterCardBack.class)));

        filenameEditor = new JTextField(20);



        // layout
        JPanel helpPanel = new JPanel(new MigLayout());
        helpPanel.add(helpLabel);

        JPanel bothFacesPanel = new JPanel(new MigLayout());
        bothFacesPanel.setBorder(BorderFactory.createTitledBorder("Standard"));
        bothFacesPanel.add(new JLabel("Card type: "));
        bothFacesPanel.add(bothFacesOptionEditor);
        bothFacesPanel.add(description, "wrap, width 400:400:");

        JPanel customFacesPanel = new JPanel(new MigLayout());
        customFacesPanel.setBorder(BorderFactory.createTitledBorder("Custom"));
        customFacesPanel.add(new JLabel("Front: "));
        customFacesPanel.add(frontFaceOptionEditor, "wrap, pushx, growx");
        customFacesPanel.add(new JLabel("Back: "));
        customFacesPanel.add(backFaceOptionEditor, "wrap, pushx, growx");

        JPanel detailsPanel = MigLayoutUtils.createTitledPanel("Details");
        MigLayoutUtils.addLabel(detailsPanel, "Filename");
        detailsPanel.add(filenameEditor, "split, pushx, growx");
        detailsPanel.add(new JLabel(".eon"), "wrap");

        JPanel mainPanel = new JPanel(new MigLayout());
        mainPanel.add(helpPanel, "wrap, pushx, growx");
        mainPanel.add(bothFacesPanel, "wrap, pushx, growx");
        mainPanel.add(customFacesPanel, "wrap, pushx, growx");
        mainPanel.add(detailsPanel, "wrap, pushx, growx");

        setContent(mainPanel);

        addDialogClosingButton("Create", DialogWithButtons.OK_OPTION, () -> {
            if (StringUtils.isEmpty(filenameEditor.getText())) {
                JOptionPane.showMessageDialog(this, "Please specify a filename", "No filename", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
        });

        addDialogClosingButton("Cancel", DialogWithButtons.CANCEL_OPTION, () -> Boolean.TRUE);

        pack();
    }

    public CardFaceTypeRegister.CardFaceInfo getSelectedFrontFace() {
        return (CardFaceTypeRegister.CardFaceInfo) frontFaceOptionEditor.getSelectedItem();
    }

    public CardFaceTypeRegister.CardFaceInfo getSelectedBackFace() {
        return (CardFaceTypeRegister.CardFaceInfo) backFaceOptionEditor.getSelectedItem();
    }

    public String getFilename() {
        return filenameEditor.getText();
    }

    private static class BothFacesOption {
        private final String display;
        private final String description;
        private final CardFaceTypeRegister.CardFaceInfo frontFace;
        private final CardFaceTypeRegister.CardFaceInfo backFace;

        public BothFacesOption(String display, String description, CardFaceTypeRegister.CardFaceInfo frontFace, CardFaceTypeRegister.CardFaceInfo backFace) {
            this.display = display;
            this.description = description;
            this.frontFace = frontFace;
            this.backFace = backFace;
        }

        public String getDisplay() {
            return display;
        }

        public String getDescription() {
            return description;
        }

        public CardFaceTypeRegister.CardFaceInfo getFrontFace() {
            return frontFace;
        }

        public CardFaceTypeRegister.CardFaceInfo getBackFace() {
            return backFace;
        }

        @Override
        public String toString() {
            return getDisplay();
        }
    }
}
