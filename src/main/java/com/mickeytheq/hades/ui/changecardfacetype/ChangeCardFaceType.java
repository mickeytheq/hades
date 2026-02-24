package com.mickeytheq.hades.ui.changecardfacetype;

import ca.cgjennings.apps.arkham.StrangeEons;
import com.mickeytheq.hades.core.CardFaceTypeRegister;
import com.mickeytheq.hades.core.Cards;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.model.CardFaceModel;
import com.mickeytheq.hades.core.model.entity.*;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.StandardProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.core.view.utils.ImageUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.serialise.CardIO;
import com.mickeytheq.hades.ui.DialogEx;
import com.mickeytheq.hades.ui.LoggingLevel;
import com.mickeytheq.hades.ui.ProgressDialog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public class ChangeCardFaceType {
    private static final Logger logger = LogManager.getLogger(ChangeCardFaceType.class);

    public void change(Path cardPath) {
        ProjectContext projectContext = StandardProjectContext.getContextForContentPath(cardPath);
        Card sourceCard = CardIO.readCard(cardPath, projectContext);

        ChangeCardFaceTypeDialog dialog = new ChangeCardFaceTypeDialog(StrangeEons.getWindow(), sourceCard);
        if (dialog.showDialog() != DialogEx.OK_OPTION)
            return;

        new ProgressDialog(LoggingLevel.Normal).runWithinProgressDialog(() -> {
            CardFaceSide cardFaceSide = dialog.getCardFaceSide();
            CardFaceModel sourceCardFaceModel = dialog.getSourceCardFaceModel();
            CardFaceTypeRegister.CardFaceInfo targetCardFaceInfo = dialog.getTargetCardFaceInfo();

            CardFaceModel targetCardFaceModel = Cards.createFaceModelForTypeCode(targetCardFaceInfo.getTypeCode(), projectContext);

            EntityUtils.copyEntity(sourceCardFaceModel, targetCardFaceModel);

            sourceCard.setCardFaceModel(cardFaceSide, targetCardFaceModel);

            sourceCard.setCardFaceModel(cardFaceSide, targetCardFaceModel);
            CardIO.writeCard(cardPath, sourceCard, projectContext);

            logger.info("Update card file at '" + cardPath + "' with new values");
            return null;
        });
    }

    static class ChangeCardFaceTypeDialog extends DialogEx {
        private final Card sourceCard;
        private final JComboBox<CardFaceSide> cardFaceSideEditor;
        private final JComboBox<CardFaceTypeRegister.CardFaceInfo> targetFaceTypeEditor;

        public ChangeCardFaceTypeDialog(Frame frame, Card sourceCard) {
            super(frame, false);

            this.sourceCard = sourceCard;

            setTitle("Card face type change options");
            setIconImage(ImageUtils.HADES_PURPLE_H_IMAGE);

            CardFaceTypeRegister cardFaceTypeRegister = CardFaceTypeRegister.get();

            targetFaceTypeEditor = new JComboBox<>();
            for (CardFaceTypeRegister.CardFaceInfo cardFaceInfo : cardFaceTypeRegister.getAllCardInformation()) {
                targetFaceTypeEditor.addItem(cardFaceInfo);
            }

            JTextField currentFaceTypeEditor = new JTextField();
            currentFaceTypeEditor.setEnabled(false);

            cardFaceSideEditor = new JComboBox<>();
            cardFaceSideEditor.addItemListener(e -> {
                CardFaceModel cardFaceModel = getSourceCardFaceModel();
                currentFaceTypeEditor.setText(cardFaceTypeRegister.getInfoForCardFaceModelClass(cardFaceModel.getClass()).toString());
            });

            cardFaceSideEditor.addItem(CardFaceSide.Front);

            if (sourceCard.hasBack())
                cardFaceSideEditor.addItem(CardFaceSide.Back);

            JPanel panel = MigLayoutUtils.createDialogPanel();
            MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, "Card side: ", cardFaceSideEditor);
            MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, "Current face type: ", currentFaceTypeEditor);
            MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, "Target face type: ", targetFaceTypeEditor);

            setContentComponent(panel);

            addOkCancelButtons();
        }

        public CardFaceSide getCardFaceSide() {
            return (CardFaceSide)cardFaceSideEditor.getSelectedItem();
        }

        public CardFaceModel getSourceCardFaceModel() {
            return sourceCard.getCardFaceModel(getCardFaceSide());
        }

        public CardFaceTypeRegister.CardFaceInfo getTargetCardFaceInfo() {
            return (CardFaceTypeRegister.CardFaceInfo) targetFaceTypeEditor.getSelectedItem();
        }
    }
}
