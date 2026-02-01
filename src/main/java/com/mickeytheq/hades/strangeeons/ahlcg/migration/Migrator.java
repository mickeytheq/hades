package com.mickeytheq.hades.strangeeons.ahlcg.migration;

import ca.cgjennings.apps.arkham.component.GameComponent;
import ca.cgjennings.apps.arkham.diy.DIY;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.model.CardFaceModel;
import com.mickeytheq.hades.core.model.cardfaces.EncounterCardBack;
import com.mickeytheq.hades.core.model.cardfaces.PlayerCardBack;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.ProjectContexts;
import com.mickeytheq.hades.core.project.configuration.ProjectConfiguration;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.serialise.CardIO;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import resources.ResourceKit;
import resources.Settings;

import java.nio.file.Path;

// migrates AHLCG plugin files to Hades
public class Migrator {
    private static final Logger logger = LogManager.getLogger(Migrator.class);

    private static final double PIXEL_MULTIPLIER = 2;

    private final ProjectContext projectContext;

    public Migrator(ProjectContext projectContext) {
        this.projectContext = projectContext;
    }

    public void migrateFile(Path sourceFile, Path targetFile) {
        logger.debug("Migrating '" + sourceFile + "' to '" + targetFile + "'...");

        if (!sourceFile.getFileName().toString().endsWith(".eon")) {
            logger.debug("Skipping '" + sourceFile + "' as it is not a .eon file");
            return;
        }

        GameComponent gameComponent = ResourceKit.getGameComponentFromFile(sourceFile.toFile());

        if (!(gameComponent instanceof DIY)) {
            logger.debug("Skipping '" + sourceFile + "' as it is not a DIY component");
            return;
        }

        DIY diy = (DIY)gameComponent;

        Card card = ProjectContexts.withContextReturn(projectContext, () -> migrateCard(diy));

        if (card == null) {
            logger.warn("Skipping '" + sourceFile + "' as that card face/type is not supported");
            return;
        }

        CardIO.writeCard(targetFile, card, projectContext);

        logger.info("Migrated '" + sourceFile + "' to '" + targetFile + "' successfully");
    }

    public Card migrateCard(DIY diy) {
        return new CardMigrator(diy).migrateCard();
    }

    class CardMigrator {
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
            
            CardFaceMigrationContext context = new CardFaceMigrationContextImpl(diy, settingsAccessor, cardFaceSide, projectContext.getProjectConfiguration());

            switch (cardFaceType) {
                case Asset:
                    return new AssetMigrator().build(context);
                case Event:
                    return new EventMigrator().build(context);
                case Skill:
                    return new SkillMigrator().build(context);
                case Investigator:
                    return new InvestigatorMigrator().build(context);
                case InvestigatorBack:
                    return new InvestigatorBackMigrator().build(context);
                case Location:
                    return new LocationMigrator().build(context);
                case LocationBack:
                    return new LocationBackMigrator().build(context);
                case Treachery:
                    return new TreacheryMigrator().build(context);
                case Enemy:
                    return new EnemyMigrator().build(context);
                case Act:
                    return new ActMigrator().build(context);
                case ActBack:
                    return new ActBackMigrator().build(context);
                case Agenda:
                    return new AgendaMigrator().build(context);
                case AgendaBack:
                    return new AgendaBackMigrator().build(context);
                case Chaos:
                    return new ScenarioReferenceMigrator().build(context);
                case Story:
                    return new StoryMigrator().build(context);
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

        public CardFaceType getCardFaceTypeForTemplateKey(String templateKey) {
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
            else if (templateKey.contains("LocationBack"))
                return CardFaceType.LocationBack;
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

    static class CardFaceMigrationContextImpl implements CardFaceMigrationContext {
        private final DIY diy;
        private final SettingsAccessor settingsAccessor;
        private final CardFaceSide cardFaceSide;
        private final ProjectConfiguration projectConfiguration;

        public CardFaceMigrationContextImpl(DIY diy, SettingsAccessor settingsAccessor, CardFaceSide cardFaceSide, ProjectConfiguration projectConfiguration) {
            this.diy = diy;
            this.settingsAccessor = settingsAccessor;
            this.cardFaceSide = cardFaceSide;
            this.projectConfiguration = projectConfiguration;
        }

        @Override
        public DIY getDIY() {
            return diy;
        }

        @Override
        public CardFaceSide getCardFaceSide() {
            return cardFaceSide;
        }

        @Override
        public ProjectConfiguration getProjectConfiguration() {
            return projectConfiguration;
        }

        @Override
        public SettingsAccessor getSettingsAccessor() {
            return settingsAccessor;
        }

        @Override
        public double convertPixelSize(double pixelSize) {
            // for now this is a simple doubling but may need to be more sophisticated if the ratio between
            // input and output changes or is dynamic, e.g. Hades/Strange Eons changing template size and/or supporting different resolutions
            return pixelSize * PIXEL_MULTIPLIER;
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
            String fullSettingsKey = getFullSettingsKey(settingsKey);

            String value = getRawSettingsValue(fullSettingsKey);

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

        @Override
        public String getFullSettingsKey(String settingsKey) {
            return settingsKey + settingsKeySuffix;
        }

        @Override
        public String getRawSettingsValue(String fullSettingsKey) {
            return settings.get(fullSettingsKey);
        }

        @Override
        public int getSpacingValue(String spacingPrefixKey) {
            String spacingSettingsKey = getFullSettingsKey(spacingPrefixKey) + "Spacing";

            String value = getRawSettingsValue(spacingSettingsKey);

            if (value == null)
                return 0;

            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
    }
}
