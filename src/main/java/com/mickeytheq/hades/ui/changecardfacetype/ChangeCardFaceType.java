package com.mickeytheq.hades.ui.changecardfacetype;

import ca.cgjennings.apps.arkham.StrangeEons;
import com.mickeytheq.hades.core.CardFaceTypeRegister;
import com.mickeytheq.hades.core.CardFaces;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.model.CardFaceModel;
import com.mickeytheq.hades.core.model.entity.AnnotatedEntityMetadataBuilder;
import com.mickeytheq.hades.core.model.entity.EntityMetadata;
import com.mickeytheq.hades.core.model.entity.PropertyMetadata;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.StandardProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.core.view.utils.ImageUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.serialise.CardIO;
import com.mickeytheq.hades.ui.DialogWithButtons;
import com.mickeytheq.hades.ui.LoggingLevel;
import com.mickeytheq.hades.ui.ProgressDialog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.Optional;

public class ChangeCardFaceType {
    private static final Logger logger = LogManager.getLogger(ChangeCardFaceType.class);

    public void change(Path cardPath) {
        ProjectContext projectContext = StandardProjectContext.getContextForContentPath(cardPath);
        Card sourceCard = CardIO.readCard(cardPath, projectContext);

        ChangeCardFaceTypeDialog dialog = new ChangeCardFaceTypeDialog(StrangeEons.getWindow(), sourceCard);
        if (dialog.showDialog() != DialogWithButtons.OK_OPTION)
            return;

        new ProgressDialog(LoggingLevel.Normal).runWithinProgressDialog(() -> {
            CardFaceSide cardFaceSide = dialog.getCardFaceSide();
            CardFaceModel sourceCardFaceModel = dialog.getSourceCardFaceModel();
            CardFaceTypeRegister.CardFaceInfo targetCardFaceInfo = dialog.getTargetCardFaceInfo();

            CardFaceModel targetCardFaceModel = CardFaces.createFaceModelForTypeCode(targetCardFaceInfo.getTypeCode(), projectContext);

            copy(sourceCardFaceModel, targetCardFaceModel);

            sourceCard.setCardFaceModel(cardFaceSide, targetCardFaceModel);

            sourceCard.setCardFaceModel(cardFaceSide, targetCardFaceModel);
            CardIO.writeCard(cardPath, sourceCard, projectContext);

            logger.info("Update card file at '" + cardPath + "' with new values");
            return null;
        });
    }

    private void copy(CardFaceModel sourceCardFaceModel, CardFaceModel targetFaceModel) {
        new CardCopier(sourceCardFaceModel, targetFaceModel).copy();
    }

    static class CardCopier {
        private final CardFaceModel sourceModel;
        private final CardFaceModel targetModel;

        public CardCopier(CardFaceModel sourceModel, CardFaceModel targetModel) {
            this.sourceModel = sourceModel;
            this.targetModel = targetModel;
        }

        public void copy() {
            EntityMetadata sourceMetadata = AnnotatedEntityMetadataBuilder.build(sourceModel.getClass());
            EntityMetadata targetMetadata = AnnotatedEntityMetadataBuilder.build(targetModel.getClass());

            copyEntity(sourceMetadata, sourceModel, targetMetadata, targetModel);
        }

        private void copyEntity(EntityMetadata fromEntityMetadata, Object fromEntity, EntityMetadata toEntityMetadata, Object toEntity) {
            for (PropertyMetadata property : fromEntityMetadata.getProperties()) {
                Optional<PropertyMetadata> targetPropertyOpt = toEntityMetadata.findProperty(property.getName());

                if (targetPropertyOpt.isPresent())
                    copyProperty(property, fromEntity, targetPropertyOpt.get(), toEntity);
                else
                    logger.info("Skipped property '" + property.getName() + "' on entity type '" + fromEntity.getClass() + " as there was no equivalent on the target class '" + toEntityMetadata.getEntityClass() + "'");
            }
        }

        private void copyProperty(PropertyMetadata fromProperty, Object fromEntity, PropertyMetadata toProperty, Object toEntity) {
            Object fromPropertyValue = fromProperty.getPropertyValue(fromEntity);

            if (!fromProperty.getPropertyClass().equals(toProperty.getPropertyClass())) {
                logger.info("Skipped property '" + fromProperty.getName() + "' on entity type '" + fromEntity.getClass() + "' with value '" + fromPropertyValue + "' although there was a property with a matching name the classes were different");
                return;
            }

            // value -> value, just do a straight reference copy as
            // value types are immutable
            if (fromProperty.isValue() && toProperty.isValue()) {
                toProperty.setPropertyValue(toEntity, fromPropertyValue);
                logger.info("Copied property '" + fromProperty.getName() + "' on entity type '" + fromEntity.getClass() + "' with value '" + fromPropertyValue + "'");
                return;
            }

            if (fromProperty.isEntity() && toProperty.isEntity()) {
                Object toPropertyValue = toProperty.getPropertyValue(toEntity);

                copyEntity(fromProperty.asEntity(), fromPropertyValue, toProperty.asEntity(), toPropertyValue);
                return;
            }

            logger.info("Skipped property '" + fromProperty.getName() + "' on entity type '" + fromEntity.getClass() + "' with value '" + fromPropertyValue + "' although there was a property with a matching name they were different fundamental types (e.g. entity vs value");
        }
    }

    static class ChangeCardFaceTypeDialog extends DialogWithButtons {
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
