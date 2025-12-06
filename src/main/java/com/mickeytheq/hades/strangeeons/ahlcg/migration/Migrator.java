package com.mickeytheq.hades.strangeeons.ahlcg.migration;

import ca.cgjennings.apps.arkham.component.GameComponent;
import ca.cgjennings.apps.arkham.diy.DIY;
import com.mickeytheq.hades.core.CardFaces;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.model.CardFaceModel;
import com.mickeytheq.hades.core.model.cardfaces.EncounterCardBack;
import com.mickeytheq.hades.core.model.cardfaces.PlayerCardBack;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.core.view.CardView;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces.*;
import com.mickeytheq.hades.strangeeons.gamecomponent.CardGameComponent;
import org.apache.commons.lang3.StringUtils;
import resources.ResourceKit;
import resources.Settings;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

public class Migrator {
    private static final Logger logger = Logger.getLogger(Migrator.class.getName());

    public static void migrate(Path sourceFile, Path targetFile) {
        logger.fine("Migrating '" + sourceFile + "' to '" + targetFile + "'...");

        if (!sourceFile.getFileName().toString().endsWith(".eon")) {
            logger.fine("Skipping '" + sourceFile + "' as it is not a .eon file");
            return;
        }

        GameComponent gameComponent = ResourceKit.getGameComponentFromFile(sourceFile.toFile());

        if (!(gameComponent instanceof DIY)) {
            logger.fine("Skipping '" + sourceFile + "' as it is not a DIY component");
            return;
        }

        DIY diy = (DIY)gameComponent;

        Card card = migrateCard(diy);

        if (card == null) {
            logger.warning("Skipping '" + sourceFile + "' as that card face/type is not supported");
            return;
        }

        // wrap the Card in a CardGameComponent
        // we don't need a view as we're not doing anything visual but all this does is basic intialisation
        // and is required to complete the CardGameComponent
        CardView cardView = CardFaces.createCardView(card);
        CardGameComponent cardGameComponent = new CardGameComponent(cardView);

        // save the newly migrated CardGameComponent
        try {
            ResourceKit.writeGameComponentToFile(targetFile.toFile(), cardGameComponent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        logger.info("Migrated '" + sourceFile + "' to '" + targetFile + "' successfully");
    }

    public static Card migrateCard(DIY diy) {
        return new CardMigrator(diy).migrateCard();
    }

    static class CardMigrator {
        private final DIY diy;

        public CardMigrator(DIY diy) {
            this.diy = diy;
        }

        private Card migrateCard() {
            String frontTemplateKey = diy.getFrontTemplateKey();

            CardFaceModel frontFaceModel = migrateFace(CardFaceSide.Front, frontTemplateKey, new SettingsAccessorImpl(diy.getSettings(), ""));

            if (frontFaceModel == null)
                return null;

            CardFaceModel backFaceModel = null;

            if (diy.getFaceStyle() == DIY.FaceStyle.TWO_FACES) {
                // special handling to pick up the generic card backs
                backFaceModel = checkForGenericBack(diy.getSettings());

                if (backFaceModel == null)
                    backFaceModel = migrateFace(CardFaceSide.Back, diy.getBackTemplateKey(), new SettingsAccessorImpl(diy.getSettings(), "Back"));
            }

            // TODO: log out any settings that are not accessed to detect properties that are not picked up

            Card card = new Card();
            card.setFrontFaceModel(frontFaceModel);
            card.setBackFaceModel(backFaceModel);

            return card;
        }

        private CardFaceModel migrateFace(CardFaceSide cardFaceSide, String templateKey, SettingsAccessor settingsAccessor) {
            CardFaceType cardFaceType = getCardFaceTypeForTemplateKey(templateKey);

            if (cardFaceType == null)
                return null;

            switch (cardFaceType) {
                case Asset:
                    return new AssetMigrator().build(diy, cardFaceSide, settingsAccessor);
                case Event:
                    return new EventMigrator().build(diy, cardFaceSide, settingsAccessor);
                case Skill:
                    return new SkillMigrator().build(diy, cardFaceSide, settingsAccessor);
                case Investigator:
                    return new InvestigatorMigrator().build(diy, cardFaceSide, settingsAccessor);
                case InvestigatorBack:
                    return new InvestigatorBackMigrator().build(diy, cardFaceSide, settingsAccessor);
                case Treachery:
                    return new TreacheryMigrator().build(diy, cardFaceSide, settingsAccessor);
                default:
                    return null;
            }
        }

        private CardFaceModel checkForGenericBack(Settings settings) {
            String backTypeBack = settings.get(SettingsFieldNames.BACK_TYPE_BACK);

            CardFaceModel backFaceModel = createGenericBackFromBackType(backTypeBack);

            if (backFaceModel != null)
                return backFaceModel;

            // story cards have the option to set the back but this stores the value in TemplateBack instead of the
            // more common BackTypeBack
            String templateBack = settings.get("TemplateBack");

            backFaceModel = createGenericBackFromBackType(templateBack);

            return backFaceModel;
        }

        private CardFaceModel createGenericBackFromBackType(String backType) {
            if (backType == null)
                return null;

            if (backType.equals("ConcealedBack")) {
                // TODO: concealed back type needed
                return null;
            }

            if (backType.equals(SettingsFieldNames.BACK_TYPE_PLAYER))
                return new PlayerCardBack();

            if (backType.equals(SettingsFieldNames.BACK_TYPE_ENCOUNTER))
                return new EncounterCardBack();

            if (backType.equals(SettingsFieldNames.BACK_TYPE_PLAYER_PURPLE))
                // TODO: purple back type needed
                return new PlayerCardBack();

            return null;
        }

        public static CardFaceType getCardFaceTypeForTemplateKey(String templateKey) {
            // back side of cards
            if (templateKey.equals("AHLCG-Guide75-Default"))
                return CardFaceType.CampaignGuide_75_By_75;
            else if (templateKey.equals("AHLCG-GuideA4-Default"))
                return CardFaceType.CampaignGuideA4;
            else if (templateKey.equals("AHLCG-GuideLetter-Default"))
                return CardFaceType.CampaignGuide_85_By_11;
            else if (templateKey.contains("Concealed"))
                return CardFaceType.Concealed;
            else if (templateKey.contains("WeaknessTreachery")) // weakness treachery is treated as treachery with weaknessType property
                return CardFaceType.Treachery;
            else if (templateKey.contains("AssetStory")) // map story assets to Asset type
                return CardFaceType.Asset;
            else if (templateKey.contains("Treachery"))
                return CardFaceType.Treachery;
            else if (templateKey.contains("WeaknessEnemy")) // weakness enemy are treated as enemy with a weaknessType property
                return CardFaceType.Enemy;
            else if (templateKey.contains("EnemyLocation"))
                return CardFaceType.EnemyLocation;
            else if (templateKey.contains("Enemy"))
                return CardFaceType.Enemy;
            else if (templateKey.contains("Scenario"))
                return CardFaceType.Scenario;
            else if (templateKey.contains("Location"))
                return CardFaceType.Location;
            else if (templateKey.contains("AgendaFrontPortrait"))
                return CardFaceType.AgendaPortrait;
            else if (templateKey.contains("AgendaPortrait"))
                return CardFaceType.AgendaPortrait;
            else if (templateKey.contains("Portrait"))
                return CardFaceType.Portrait;
            else if (templateKey.contains("Act") && templateKey.contains("Back"))
                return CardFaceType.ActBack;
            else if (templateKey.contains("Act"))
                return CardFaceType.Act;
            else if (templateKey.contains("Agenda") && templateKey.contains("Back"))
                return CardFaceType.AgendaBack;
            else if (templateKey.contains("Agenda"))
                return CardFaceType.Agenda;
            else if (templateKey.contains("Story"))
                return CardFaceType.Story;
            else if (templateKey.contains("Chaos"))
                return CardFaceType.Chaos;
            else if (templateKey.contains("MiniInvestigator"))
                return CardFaceType.MiniInvestigator;
            else if (templateKey.contains("InvestigatorBack"))
                return CardFaceType.InvestigatorBack;
            else if (templateKey.contains("Investigator"))
                return CardFaceType.Investigator;
            else if (templateKey.contains("Asset"))
                return CardFaceType.Asset;
            else if (templateKey.contains("Event"))
                return CardFaceType.Event;
            else if (templateKey.contains("Skill"))
                return CardFaceType.Skill;
            else if (templateKey.contains("Customizable"))
                return CardFaceType.CustomizableUpgrade;
            else if (templateKey.contains("Key"))
                return CardFaceType.Key;

            return null;
        }
    }

    static class SettingsAccessorImpl implements SettingsAccessor {
        private final Settings settings;
        private final String settingsKeySuffix;

        public SettingsAccessorImpl(Settings settings, String settingsKeySuffix) {
            this.settings = settings;
            this.settingsKeySuffix = settingsKeySuffix;
        }

        @Override
        public String getString(String settingsKey) {
            String mappedSettingsKey = settingsKey + settingsKeySuffix;

            String value = settings.get(mappedSettingsKey);

            if (StringUtils.isEmpty(value))
                return null;

            if (value.equals("None"))
                return null;

            return value;
        }

        @Override
        public Integer getIntegerAllowInvalid(String settingsKey) {
            String value = getString(settingsKey);

            if (value == null)
                return null;

            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        @Override
        public <T extends Enum<T>> T getEnumAllowInvalid(String settingsKey, Class<T> enumClass) {
            String value = getString(settingsKey);

            if (value == null)
                return null;

            try {
                return Enum.valueOf(enumClass, value);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }
}
