package com.mickeytheq.hades.strangeeons.ahlcg.migration;

import ca.cgjennings.apps.arkham.component.DefaultPortrait;
import ca.cgjennings.apps.arkham.diy.DIY;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.project.configuration.CollectionConfiguration;
import com.mickeytheq.hades.core.project.configuration.EncounterSetConfiguration;
import com.mickeytheq.hades.core.project.configuration.ProjectConfiguration;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.core.view.common.PortraitView;
import com.mickeytheq.hades.util.shape.Unit;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

import java.util.Optional;

public class MigrationUtils {
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
            commonCardFieldsModel.setTitle(settingsAccessor.getString(SettingsFieldNames.TITLE));

        commonCardFieldsModel.setSubtitle(settingsAccessor.getString(SettingsFieldNames.SUBTITLE));
        commonCardFieldsModel.setTraits(settingsAccessor.getString(SettingsFieldNames.TRAITS));
        commonCardFieldsModel.setAfterTraitsSpacing(new Distance(settingsAccessor.getSpacingValue(SettingsFieldNames.TRAITS), Unit.Point));
        commonCardFieldsModel.setKeywords(settingsAccessor.getString(SettingsFieldNames.KEYWORDS));
        commonCardFieldsModel.setAfterKeywordsSpacing(new Distance(settingsAccessor.getSpacingValue(SettingsFieldNames.KEYWORDS), Unit.Point));
        commonCardFieldsModel.setRules(settingsAccessor.getString(SettingsFieldNames.GAME_TEXT));
        commonCardFieldsModel.setAfterRulesSpacing(new Distance(settingsAccessor.getSpacingValue(SettingsFieldNames.GAME_TEXT), Unit.Point));
        commonCardFieldsModel.setFlavourText(settingsAccessor.getString(SettingsFieldNames.FLAVOR));
        commonCardFieldsModel.setAfterFlavourTextSpacing(new Distance(settingsAccessor.getSpacingValue(SettingsFieldNames.FLAVOR), Unit.Point));
        commonCardFieldsModel.setVictory(settingsAccessor.getString(SettingsFieldNames.VICTORY));
        commonCardFieldsModel.setCopyright(settingsAccessor.getString("Copyright"));
    }

    public static void populatePlayerCardFields(CardFaceMigrationContext context, PlayerCardFieldsModel playerCardFieldsModel) {
        SettingsAccessor settingsAccessor = context.getSettingsAccessor();

        populatePlayerCardTypeAndClasses(context, playerCardFieldsModel);

        playerCardFieldsModel.setCost(settingsAccessor.getString("ResourceCost"));
        playerCardFieldsModel.setLevel(settingsAccessor.getIntegerAllowInvalid("Level"));

        playerCardFieldsModel.setSkillIcon1(parseSkillIcon(settingsAccessor.getString("Skill1")));
        playerCardFieldsModel.setSkillIcon2(parseSkillIcon(settingsAccessor.getString("Skill2")));
        playerCardFieldsModel.setSkillIcon3(parseSkillIcon(settingsAccessor.getString("Skill3")));
        playerCardFieldsModel.setSkillIcon4(parseSkillIcon(settingsAccessor.getString("Skill4")));
        playerCardFieldsModel.setSkillIcon5(parseSkillIcon(settingsAccessor.getString("Skill5")));
        playerCardFieldsModel.setSkillIcon6(parseSkillIcon(settingsAccessor.getString("Skill6")));
    }

    public static void populatePlayerCardTypeAndClasses(CardFaceMigrationContext context, PlayerCardFieldsModel playerCardFieldsModel) {
        SettingsAccessor settingsAccessor = context.getSettingsAccessor();

        // special case for Story as this are distinguished by template in AHLCG plugin rather than CardClass
        if (context.getTemplateKey().contains("Story")) {
            playerCardFieldsModel.setCardType(PlayerCardType.Story);
            return;
        }

        String cardClass = settingsAccessor.getString("CardClass");

        PlayerCardType playerCardType = settingsAccessor.getEnumAllowInvalid("CardClass", PlayerCardType.class);

        if (playerCardType == null)
            playerCardType = PlayerCardType.Standard;

        playerCardFieldsModel.setCardType(playerCardType);

        // for non-standard types no need to do anything else
        if (playerCardType != PlayerCardType.Standard)
            return;

        playerCardFieldsModel.setCardClass1(PlayerCardClass.valueOf(cardClass));

        // look for multi-class
        playerCardFieldsModel.setCardClass2(settingsAccessor.getEnumAllowInvalid("CardClass2", PlayerCardClass.class));
        playerCardFieldsModel.setCardClass3(settingsAccessor.getEnumAllowInvalid("CardClass3", PlayerCardClass.class));
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

    public static void populateCollection(CardFaceMigrationContext context, CollectionModel model) {
        // existing plugin doesn't allow different numbers on front/back so always copy the front face on the back
        if (context.getCardFaceSide() == CardFaceSide.Back) {
            model.setCopyOtherFace(true);
            return;
        }

        SettingsAccessor settingsAccessor = context.getSettingsAccessor();

        model.setNumber(settingsAccessor.getString(SettingsFieldNames.COLLECTION_NUMBER));

        CollectionConfiguration collectionConfiguration = findOrCreateCollection(context);
        model.setCollectionConfiguration(collectionConfiguration);
    }

    public static void populateEncounterSet(CardFaceMigrationContext context, EncounterSetModel model) {
        // existing plugin doesn't allow different numbers on front/back so always copy the front face on the back
        if (context.getCardFaceSide() == CardFaceSide.Back) {
            model.setCopyOtherFace(true);
            return;
        }

        SettingsAccessor settingsAccessor = context.getSettingsAccessor();

        model.setNumber(settingsAccessor.getString(SettingsFieldNames.ENCOUNTER_NUMBER));
        model.setTotal(settingsAccessor.getString(SettingsFieldNames.ENCOUNTER_TOTAL));

        EncounterSetConfiguration encounterSetConfiguration = findOrCreateEncounterSet(context);
        model.setEncounterSetConfiguration(encounterSetConfiguration);
    }

    private static CollectionConfiguration findOrCreateCollection(CardFaceMigrationContext context) {
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

            Optional<CollectionConfiguration> collectionInfoOpt = projectConfiguration.getCollectionConfigurations().stream().filter(o -> o.getDisplayName().equals(displayName)).findAny();

            if (collectionInfoOpt.isPresent()) {
                return collectionInfoOpt.get();
            }

            CollectionConfiguration collectionInfo = new CollectionConfiguration();
            collectionInfo.setTag(tag);
            collectionInfo.setDisplayName(displayName);
            collectionInfo.getImage().set(portrait.getImage());

            projectConfiguration.getCollectionConfigurations().add(collectionInfo);
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

            CollectionConfiguration collectionInfo = new CollectionConfiguration();
            collectionInfo.setTag(tag);
            collectionInfo.setDisplayName(displayName);
            collectionInfo.getImage().set(portrait.getImage());

            projectConfiguration.getCollectionConfigurations().add(collectionInfo);
            projectConfiguration.save();

            return collectionInfo;
        }
    }

    private static EncounterSetConfiguration findOrCreateEncounterSet(CardFaceMigrationContext context) {
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

            Optional<EncounterSetConfiguration> optional = projectConfiguration.getEncounterSetConfigurations().stream().filter(o -> o.getDisplayName().equals(displayName)).findAny();

            if (optional.isPresent()) {
                return optional.get();
            }

            EncounterSetConfiguration encounterSetInfo = new EncounterSetConfiguration();
            encounterSetInfo.setTag(tag);
            encounterSetInfo.setDisplayName(displayName);
            encounterSetInfo.getImage().set(portrait.getImage());

            projectConfiguration.getEncounterSetConfigurations().add(encounterSetInfo);
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

            EncounterSetConfiguration encounterSetInfo = new EncounterSetConfiguration();
            encounterSetInfo.setTag(tag);
            encounterSetInfo.setDisplayName(displayName);
            encounterSetInfo.getImage().set(portrait.getImage());

            projectConfiguration.getEncounterSetConfigurations().add(encounterSetInfo);
            projectConfiguration.save();

            return encounterSetInfo;
        }
    }

    // the scaling factor used to adjust the AHLCG portait settings to Hades
    // see the PortraitView doco for full details.
    private static final int AHLCG_PORTRAIT_PPI = 150;
    private static final int PORTRAIT_VALUE_SCALING_FACTOR = PortraitView.ASSUMED_PERSISTENCE_PPI / AHLCG_PORTRAIT_PPI;

    public static void populateArt(CardFaceMigrationContext context, PortraitModel portraitModel) {
        SettingsAccessor settingsAccessor = context.getSettingsAccessor();

        portraitModel.setArtist(settingsAccessor.getString(SettingsFieldNames.ARTIST));

        DefaultPortrait defaultPortrait = PortraitUtils.getArtPortrait(context.getDIY(), context.getCardFaceSide());

        if (defaultPortrait != null) {
            // an empty source means it's a default image (like a 1x1 placeholder) stored in the file which we don't want to persist in ours
            if (StringUtils.isEmpty(defaultPortrait.getSource()))
                portraitModel.getImage().set(null);
            else
                portraitModel.getImage().set(defaultPortrait.getImage());

            portraitModel.setPanX(defaultPortrait.getPanX() * PORTRAIT_VALUE_SCALING_FACTOR);
            portraitModel.setPanY(defaultPortrait.getPanY() * PORTRAIT_VALUE_SCALING_FACTOR);
            portraitModel.setRotation(defaultPortrait.getRotation());
            portraitModel.setScale(defaultPortrait.getScale() * PORTRAIT_VALUE_SCALING_FACTOR);
        }
    }

    public static WeaknessType getWeaknessType(String subType) {
        if (StringUtils.isEmpty(subType))
            return null;

        if (subType.equals("Weakness"))
            return WeaknessType.Investigator;

        String weaknessType = Strings.CS.removeEnd(subType, "Weakness");

        return WeaknessType.valueOf(weaknessType);
    }

    public static void populateStorySectionModel(SettingsAccessor settingsAccessor, String settingsCode, StorySectionModel model) {
        String headerKey = SettingsFieldNames.STORY_SECTION_HEADER_PREFIX + settingsCode;

        model.setHeader(settingsAccessor.getString(headerKey));
        model.setAfterHeaderSpacing(new Distance(settingsAccessor.getSpacingValue(headerKey), Unit.Point));

        String storyKey = SettingsFieldNames.STORY_SECTION_STORY_PREFIX + settingsCode;
        model.setStory(settingsAccessor.getString(storyKey));
        model.setAfterStorySpacing(new Distance(settingsAccessor.getSpacingValue(storyKey), Unit.Point));

        String rulesKey = SettingsFieldNames.GAME_TEXT + settingsCode;
        model.setRules(settingsAccessor.getString(rulesKey));
    }

    public static void populateLocationFields(CardFaceMigrationContext context, LocationFieldsModel model) {
        SettingsAccessor settingsAccessor = context.getSettingsAccessor();

        model.setClues(MigrationUtils.parseStatistic(settingsAccessor, SettingsFieldNames.CLUES, SettingsFieldNames.PER_INVESTIGATOR));
        model.setShroud(MigrationUtils.parseStatistic(settingsAccessor, SettingsFieldNames.SHROUD, SettingsFieldNames.SHROUD + SettingsFieldNames.PER_INVESTIGATOR));
        model.setLocationIcon(MigrationUtils.parseLocationIcon(settingsAccessor, SettingsFieldNames.LOCATION_ICON));
        model.setConnectionIcon1(MigrationUtils.parseLocationIcon(settingsAccessor, SettingsFieldNames.CONNECTION_ICON_PREFIX + "1" + SettingsFieldNames.CONNECTION_ICON_SUFFIX));
        model.setConnectionIcon2(MigrationUtils.parseLocationIcon(settingsAccessor, SettingsFieldNames.CONNECTION_ICON_PREFIX + "2" + SettingsFieldNames.CONNECTION_ICON_SUFFIX));
        model.setConnectionIcon3(MigrationUtils.parseLocationIcon(settingsAccessor, SettingsFieldNames.CONNECTION_ICON_PREFIX + "3" + SettingsFieldNames.CONNECTION_ICON_SUFFIX));
        model.setConnectionIcon4(MigrationUtils.parseLocationIcon(settingsAccessor, SettingsFieldNames.CONNECTION_ICON_PREFIX + "4" + SettingsFieldNames.CONNECTION_ICON_SUFFIX));
        model.setConnectionIcon5(MigrationUtils.parseLocationIcon(settingsAccessor, SettingsFieldNames.CONNECTION_ICON_PREFIX + "5" + SettingsFieldNames.CONNECTION_ICON_SUFFIX));
        model.setConnectionIcon6(MigrationUtils.parseLocationIcon(settingsAccessor, SettingsFieldNames.CONNECTION_ICON_PREFIX + "6" + SettingsFieldNames.CONNECTION_ICON_SUFFIX));
    }

    public static String parseLocationIcon(SettingsAccessor settingsAccessor, String settingsKey) {
        String value = settingsAccessor.getString(settingsKey);

        if (StringUtils.isEmpty(value))
            return null;

        if (value.equals(SettingsFieldNames.LOCATION_ICON_EMPTY_VALUE))
            return LocationFieldsModel.EMPTY_VALUE;

        if (value.equals(SettingsFieldNames.LOCATION_ICON_NONE_VALUE))
            return LocationFieldsModel.NONE_VALUE;

        if (value.equals(SettingsFieldNames.LOCATION_ICON_COPY_FRONT_VALUE))
            return LocationFieldsModel.COPY_OTHER_VALUE;

        return value;
    }
}
