package com.mickeytheq.hades.strangeeons.ahlcg.migration;

import ca.cgjennings.apps.arkham.component.DefaultPortrait;
import ca.cgjennings.apps.arkham.diy.DIY;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.project.CollectionInfo;
import com.mickeytheq.hades.core.project.EncounterSetInfo;
import com.mickeytheq.hades.core.project.ProjectConfiguration;
import com.mickeytheq.hades.core.view.CardFaceSide;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class MigrationUtils {
    private static final double PORTRAIT_SCALE_ADJUST_FACTOR = 2.0;

    public static Statistic parseStatistic(SettingsAccessor settingsAccessor, String valueSetting, String perInvestigatorSetting) {
        String value = settingsAccessor.getString(valueSetting);

        if (value == null)
            return Statistic.empty();

        String perInvestigator;
        if (perInvestigatorSetting != null) {
            perInvestigator = settingsAccessor.getString(perInvestigatorSetting);
        }
        else {
            perInvestigator = "0";
        }

        return new Statistic(value, "1".equals(perInvestigator));
    }

    public static void populateCommonCardFields(CardFaceMigrationContext context, CommonCardFieldsModel commonCardFieldsModel) {
        DIY diy = context.getDIY();
        CardFaceSide cardFaceSide = context.getCardFaceSide();
        SettingsAccessor settingsAccessor = context.getSettingsAccessor();

        if (cardFaceSide == CardFaceSide.Front)
            commonCardFieldsModel.setTitle(diy.getName());
        else
            commonCardFieldsModel.setTitle(settingsAccessor.getString(SettingsFieldNames.TITLE_BACK));

        commonCardFieldsModel.setSubtitle(settingsAccessor.getString(SettingsFieldNames.SUBTITLE));
        commonCardFieldsModel.setTraits(settingsAccessor.getString(SettingsFieldNames.TRAITS));
        commonCardFieldsModel.setAfterTraitsSpace(settingsAccessor.getSpacingValue(SettingsFieldNames.TRAITS));
        commonCardFieldsModel.setKeywords(settingsAccessor.getString(SettingsFieldNames.KEYWORDS));
        commonCardFieldsModel.setAfterKeywordsSpace(settingsAccessor.getSpacingValue(SettingsFieldNames.KEYWORDS));
        commonCardFieldsModel.setRules(settingsAccessor.getString(SettingsFieldNames.GAME_TEXT));
        commonCardFieldsModel.setAfterRulesSpace(settingsAccessor.getSpacingValue(SettingsFieldNames.GAME_TEXT));
        commonCardFieldsModel.setFlavourText(settingsAccessor.getString(SettingsFieldNames.FLAVOR));
        commonCardFieldsModel.setAfterFlavourTextSpace(settingsAccessor.getSpacingValue(SettingsFieldNames.FLAVOR));
        commonCardFieldsModel.setVictory(settingsAccessor.getString(SettingsFieldNames.VICTORY));
        commonCardFieldsModel.setCopyright(settingsAccessor.getString("Copyright"));
    }

    public static void populatePlayerCardFields(CardFaceMigrationContext context, PlayerCardFieldsModel playerCardFieldsModel) {
        SettingsAccessor settingsAccessor = context.getSettingsAccessor();

        populatePlayerCardTypeAndClasses(settingsAccessor, playerCardFieldsModel);

        playerCardFieldsModel.setCost(settingsAccessor.getString("ResourceCost"));
        playerCardFieldsModel.setLevel(settingsAccessor.getIntegerAllowInvalid("Level"));

        playerCardFieldsModel.setSkillIcon1(parseSkillIcon(settingsAccessor.getString("Skill1")));
        playerCardFieldsModel.setSkillIcon2(parseSkillIcon(settingsAccessor.getString("Skill2")));
        playerCardFieldsModel.setSkillIcon3(parseSkillIcon(settingsAccessor.getString("Skill3")));
        playerCardFieldsModel.setSkillIcon4(parseSkillIcon(settingsAccessor.getString("Skill4")));
        playerCardFieldsModel.setSkillIcon5(parseSkillIcon(settingsAccessor.getString("Skill5")));
        playerCardFieldsModel.setSkillIcon6(parseSkillIcon(settingsAccessor.getString("Skill6")));
    }

    public static void populatePlayerCardTypeAndClasses(SettingsAccessor settingsAccessor, PlayerCardFieldsModel playerCardFieldsModel) {
        String cardClass = settingsAccessor.getString("CardClass");

        PlayerCardType playerCardType = settingsAccessor.getEnumAllowInvalid("CardClass", PlayerCardType.class);

        if (playerCardType == null)
            playerCardType = PlayerCardType.Standard;

        playerCardFieldsModel.setPlayerCardType(playerCardType);

        // for non-standard types no need to do anything else
        if (playerCardType != PlayerCardType.Standard)
            return;

        playerCardFieldsModel.setPlayerCardClass1(PlayerCardClass.valueOf(cardClass));

        // look for multi-class
        playerCardFieldsModel.setPlayerCardClass2(settingsAccessor.getEnumAllowInvalid("CardClass2", PlayerCardClass.class));
        playerCardFieldsModel.setPlayerCardClass3(settingsAccessor.getEnumAllowInvalid("CardClass3", PlayerCardClass.class));
    }

    private static PlayerCardSkillIcon parseSkillIcon(String skill) {
        if (StringUtils.isEmpty(skill))
            return null;

        try {
            return PlayerCardSkillIcon.valueOf(skill);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static void populatingNumbering(CardFaceMigrationContext context, NumberingModel numberingModel) {
        SettingsAccessor settingsAccessor = context.getSettingsAccessor();

        numberingModel.setCollectionNumber(settingsAccessor.getString(SettingsFieldNames.COLLECTION_NUMBER));
        numberingModel.setEncounterNumber(settingsAccessor.getString(SettingsFieldNames.ENCOUNTER_NUMBER));
        numberingModel.setEncounterTotal(settingsAccessor.getString(SettingsFieldNames.ENCOUNTER_TOTAL));

        CollectionInfo collectionInfo = findOrCreateCollection(context);
        numberingModel.setCollection(collectionInfo);

        EncounterSetInfo encounterSetInfo = findOrCreateEncounterSet(context);
        numberingModel.setEncounterSet(encounterSetInfo);
    }

    private static CollectionInfo findOrCreateCollection(CardFaceMigrationContext context) {
        SettingsAccessor settingsAccessor = context.getSettingsAccessor();
        ProjectConfiguration projectConfiguration = context.getProjectConfiguration();

        Integer collectionType = settingsAccessor.getIntegerAllowInvalid(SettingsFieldNames.COLLECTION_TYPE);

        // shouldn't be null but if it is then we can't derive anything
        if (collectionType == null)
            return null;

        if (collectionType == -1) {
            // custom image embedded on the card
            DefaultPortrait portrait = PortraitUtils.getCollectionPortrait(context.getDIY());

            // use the filename of the source string to create a display and tag
            String displayName = FilenameUtils.getBaseName(portrait.getSource());
            String tag = StringUtils.deleteWhitespace(displayName);

            if (StringUtils.isEmpty(tag) || StringUtils.isEmpty(displayName))
                return null;

            Optional<CollectionInfo> collectionInfoOpt = projectConfiguration.getCollectionConfiguration().findCollectionInfo(tag);

            if (collectionInfoOpt.isPresent()) {
                return collectionInfoOpt.get();
            }

            CollectionInfo collectionInfo = new CollectionInfo();
            collectionInfo.setTag(tag);
            collectionInfo.setDisplayName(displayName);
            collectionInfo.setImage(portrait.getImage());

            projectConfiguration.getCollectionConfiguration().getCollectionInfos().add(collectionInfo);
            projectConfiguration.save();

            return collectionInfo;
        }
        else if (collectionType == 0) {
            // one of the pre-installed collections (e.g. from the official game
            // TODO: do we want to support these?
            return null;
        }
        else {
            // a user collection
            String tag = settingsAccessor.getString(SettingsFieldNames.USER_COLLECTION_TAG_PREFIX + collectionType);
            String displayName = settingsAccessor.getString(SettingsFieldNames.USER_COLLECTION_NAME_PREFIX + collectionType);

            if (StringUtils.isEmpty(tag) || StringUtils.isEmpty(displayName))
                return null;

            // the portrait always contains the image
            DefaultPortrait portrait = PortraitUtils.getCollectionPortrait(context.getDIY());

            CollectionInfo collectionInfo = new CollectionInfo();
            collectionInfo.setTag(tag);
            collectionInfo.setDisplayName(displayName);
            collectionInfo.setImage(portrait.getImage());

            projectConfiguration.getCollectionConfiguration().getCollectionInfos().add(collectionInfo);
            projectConfiguration.save();

            return collectionInfo;
        }
    }

    private static EncounterSetInfo findOrCreateEncounterSet(CardFaceMigrationContext context) {
        SettingsAccessor settingsAccessor = context.getSettingsAccessor();
        ProjectConfiguration projectConfiguration = context.getProjectConfiguration();

        Integer type = settingsAccessor.getIntegerAllowInvalid(SettingsFieldNames.ENCOUNTER_SET_TYPE);

        // shouldn't be null but if it is then we can't derive anything
        if (type == null)
            return null;

        if (type == -1) {
            // custom image embedded on the card
            DefaultPortrait portrait = PortraitUtils.getEncounterSetPortrait(context.getDIY());

            // use the filename of the source string to create a display and tag
            String displayName = FilenameUtils.getBaseName(portrait.getSource());
            String tag = StringUtils.deleteWhitespace(displayName);

            Optional<EncounterSetInfo> optional = projectConfiguration.getEncounterSetConfiguration().findEncounterSetInfo(tag);

            if (optional.isPresent()) {
                return optional.get();
            }

            EncounterSetInfo encounterSetInfo = new EncounterSetInfo();
            encounterSetInfo.setTag(tag);
            encounterSetInfo.setDisplayName(displayName);
            encounterSetInfo.setImage(portrait.getImage());

            projectConfiguration.getEncounterSetConfiguration().getEncounterSetInfos().add(encounterSetInfo);
            projectConfiguration.save();

            return encounterSetInfo;
        }
        else if (type == 0) {
            // one of the pre-installed encounter sets (e.g. from the official game)
            // TODO: do we want to support these?
            return null;
        }
        else {
            // a user encounter set
            String tag = settingsAccessor.getString(SettingsFieldNames.USER_ENCOUNTER_SET_TAG_PREFIX + type);
            String displayName = settingsAccessor.getString(SettingsFieldNames.USER_ENCOUNTER_SET_NAME_PREFIX + type);

            if (StringUtils.isEmpty(tag) || StringUtils.isEmpty(displayName))
                return null;

            // the portrait always contains the image
            DefaultPortrait portrait = PortraitUtils.getEncounterSetPortrait(context.getDIY());

            EncounterSetInfo encounterSetInfo = new EncounterSetInfo();
            encounterSetInfo.setTag(tag);
            encounterSetInfo.setDisplayName(displayName);
            encounterSetInfo.setImage(portrait.getImage());

            projectConfiguration.getEncounterSetConfiguration().getEncounterSetInfos().add(encounterSetInfo);
            projectConfiguration.save();

            return encounterSetInfo;
        }
    }

    public static void populateArt(CardFaceMigrationContext context, PortraitWithArtistModel artPortraitWithArtistModel) {
        SettingsAccessor settingsAccessor = context.getSettingsAccessor();

        artPortraitWithArtistModel.setArtist(settingsAccessor.getString(SettingsFieldNames.ARTIST));

        DefaultPortrait defaultPortrait = PortraitUtils.getArtPortrait(context.getDIY(), context.getCardFaceSide());

        if (defaultPortrait != null) {
            PortraitModel portraitModel = artPortraitWithArtistModel.getPortraitModel();

            // an empty source means its a default image (like a 1x1 placeholder) stored in the file which we don't want to persist in ours
            if (StringUtils.isEmpty(defaultPortrait.getSource()))
                portraitModel.setImage(null);
            else
                portraitModel.setImage(defaultPortrait.getImage());
            
            portraitModel.setPanX(defaultPortrait.getPanX());
            portraitModel.setPanY(defaultPortrait.getPanY());
            portraitModel.setRotation(defaultPortrait.getRotation());
            portraitModel.setScale(defaultPortrait.getScale() * PORTRAIT_SCALE_ADJUST_FACTOR);
        }
    }
}
