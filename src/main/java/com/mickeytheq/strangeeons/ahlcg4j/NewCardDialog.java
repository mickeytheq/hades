package com.mickeytheq.strangeeons.ahlcg4j;

import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.asset.Asset;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.backs.EncounterCardBack;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.backs.PlayerCardBack;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.backs.EncounterCardBackView;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.treachery.Treachery;
import com.mickeytheq.strangeeons.ahlcg4j.codegenerated.InterfaceConstants;
import net.miginfocom.swing.MigLayout;
import resources.Language;

import javax.swing.*;
import java.awt.*;

public class NewCardDialog extends JDialog {
    private final boolean canCancel;

    private boolean cancelled;

    private JComboBox<BothFacesOption> bothFacesOptionEditor;
    private JComboBox<CardFaceTypeRegister.CardFaceInfo> frontFaceOptionEditor;
    private JComboBox<CardFaceTypeRegister.CardFaceInfo> backFaceOptionEditor;

    // when this dialog is launched from the regular Strange Eons 'new game component' code path it cannot be cancelled
    public NewCardDialog(boolean canCancel) {
        super((Frame) null, "New card", true);

        this.canCancel = canCancel;

        initialise();
    }

    public boolean cancelled() {
        return cancelled;
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

        bothFacesOptionEditor.addItem(new BothFacesOption(Language.string(InterfaceConstants.ASSET), "Asset with player card back",
                cardFaceTypeRegister.getInfoForCardFaceModelClass(Asset.class),
                cardFaceTypeRegister.getInfoForCardFaceModelClass(PlayerCardBack.class)));
        bothFacesOptionEditor.addItem(new BothFacesOption(Language.string(InterfaceConstants.TREACHERY), "Treachery with encounter card back",
                cardFaceTypeRegister.getInfoForCardFaceModelClass(Treachery.class),
                cardFaceTypeRegister.getInfoForCardFaceModelClass(EncounterCardBack.class)));

        // buttons
        JButton createButton = new JButton("Create");
        createButton.addActionListener(e -> {
            // TODO: validate the front and back are the same size (mix of portrait and landscape should be allowed)
            cancelled = false;

            this.setVisible(false);
            this.dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            cancelled = true;
            this.setVisible(false);
            this.dispose();
        });


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

        JPanel buttonPanel = new JPanel(new MigLayout());
        buttonPanel.add(new JPanel(), "pushx, growx"); // spacer panel
        buttonPanel.add(createButton);

        if (canCancel)
            buttonPanel.add(cancelButton);

        JPanel mainPanel = new JPanel(new MigLayout());
        mainPanel.add(helpPanel, "wrap, pushx, growx");
        mainPanel.add(bothFacesPanel, "wrap, pushx, growx");
        mainPanel.add(customFacesPanel, "wrap, pushx, growx");
        mainPanel.add(buttonPanel, "wrap, pushx, growx");

        getContentPane().add(mainPanel);

        setResizable(false);

        pack();
    }

    public CardFaceTypeRegister.CardFaceInfo getSelectedFrontFace() {
        return (CardFaceTypeRegister.CardFaceInfo) frontFaceOptionEditor.getSelectedItem();
    }

    public CardFaceTypeRegister.CardFaceInfo getSelectedBackFace() {
        return (CardFaceTypeRegister.CardFaceInfo) backFaceOptionEditor.getSelectedItem();
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
