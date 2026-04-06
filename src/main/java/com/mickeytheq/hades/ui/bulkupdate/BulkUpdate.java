package com.mickeytheq.hades.ui.bulkupdate;

import com.mickeytheq.hades.core.Cards;
import com.mickeytheq.hades.core.global.carddatabase.CardDatabase;
import com.mickeytheq.hades.core.global.carddatabase.CardDatabases;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.model.CardFaceModel;
import com.mickeytheq.hades.core.project.ProjectContexts;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.serialise.CardIO;
import com.mickeytheq.hades.ui.DialogEx;
import com.mickeytheq.hades.ui.Environment;
import com.mickeytheq.hades.ui.ProgressDialog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class BulkUpdate {
    private static final Logger logger = LogManager.getLogger(BulkUpdate.class);

    private BulkUpdateOptionsDialog bulkUpdateOptionsDialog;

    public void run(List<Card> cards) {
        bulkUpdateOptionsDialog = new BulkUpdateOptionsDialog(Environment.getTopLevelWindow(), true);
        if (bulkUpdateOptionsDialog.showDialog() != DialogEx.OK_OPTION)
            return;

        // force editors to be closed in non-dry run mode
        if (!bulkUpdateOptionsDialog.isDryRun()) {
            // TODO: decide this behaviour
        }

        List<CardFaceModel> models = cards.stream().flatMap(o -> o.getCardFaces().stream()).collect(Collectors.toList());

        new ProgressDialog(bulkUpdateOptionsDialog.getLoggingLevel()).runWithinProgressDialog(() -> {
            BulkUpdate.Updater updater = new BulkUpdate.Updater(bulkUpdateOptionsDialog.getBulkUpdateField(),
                    bulkUpdateOptionsDialog.getSelectedFieldEditor(),
                    bulkUpdateOptionsDialog.isDryRun(),
                    CardDatabases.getCardDatabase()
            );
            updater.updateCards(cards);
            return null;
        });
    }

    static class Updater {
        private final BulkUpdateField bulkUpdateField;
        private final JComponent selectedFieldEditor;
        private final boolean dryRun;
        private final CardDatabase cardDatabase;

        public Updater(BulkUpdateField bulkUpdateField, JComponent selectedFieldEditor, boolean dryRun, CardDatabase cardDatabase) {
            this.bulkUpdateField = bulkUpdateField;
            this.selectedFieldEditor = selectedFieldEditor;
            this.dryRun = dryRun;
            this.cardDatabase = cardDatabase;
        }

        public void updateCards(List<Card> cards) {
            for (Card card : cards) {
                updateCard(card);
            }
        }

        private void updateCard(Card card) {
            Path sourcePath = cardDatabase.getSourcePathForCard(card);

            boolean updated = updateCardFace(card.getFrontFaceModel(), CardFaceSide.Front);

            if (card.hasBack())
                updated = updated | updateCardFace(card.getBackFaceModel(), CardFaceSide.Back);

            if (updated && !dryRun) {
                // TODO: project context
                CardIO.writeCard(sourcePath, card, null);
            }
        }

        private boolean updateCardFace(CardFaceModel cardFaceModel, CardFaceSide cardFaceSide) {
            if (!bulkUpdateField.isApplicable(cardFaceModel)) {
                logger.debug("Skipping card face " + Cards.toDisplayString(cardFaceModel, cardFaceSide) + " face as it does not have information for field '" + bulkUpdateField.getDisplayName() + "'");
                return false;
            }

            BulkUpdateField.Updater updater = bulkUpdateField.createUpdater(selectedFieldEditor, cardFaceModel);

            if (!updater.isUpdateRequired()) {
                logger.debug("Skipping update of field '" + bulkUpdateField.getDisplayName() + "' on card " + Cards.toDisplayString(cardFaceModel, cardFaceSide) + " as the value would not change");
                return false;
            }

            String oldValueDisplay = updater.getOldValueDisplay();
            String newValueDisplay = updater.getNewValueDisplay();

            String valueLogString = "field '" + bulkUpdateField.getDisplayName() + "' on card " + Cards.toDisplayString(cardFaceModel, cardFaceSide) + " from value '" + oldValueDisplay + "' to new value '" + newValueDisplay + "'";

            logger.debug("Updating " + valueLogString);

            // if not a dry run, actually do the update
            if (!dryRun) {
                updater.update();
            }

            String updatedLogString = "Updated " + valueLogString;

            if (dryRun) {
                updatedLogString = "(Skipped - dry run enabled) " + updatedLogString;
            }

            logger.info(updatedLogString);

            return true;
        }
    }
}
