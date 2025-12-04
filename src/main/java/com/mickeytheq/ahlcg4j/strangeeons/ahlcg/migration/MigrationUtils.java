package com.mickeytheq.ahlcg4j.strangeeons.ahlcg.migration;

import ca.cgjennings.apps.arkham.diy.DIY;
import com.mickeytheq.ahlcg4j.core.model.common.*;
import com.mickeytheq.ahlcg4j.core.view.CardFaceSide;
import org.apache.commons.lang3.StringUtils;

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

    public static void populateCommonCardFields(DIY diy, CardFaceSide cardFaceSide, SettingsAccessor settingsAccessor, CommonCardFieldsModel commonCardFieldsModel) {
        if (cardFaceSide == CardFaceSide.Front)
            commonCardFieldsModel.setTitle(diy.getName());
        else
            commonCardFieldsModel.setTitle(settingsAccessor.getString(SettingsFieldNames.TITLE_BACK));

        commonCardFieldsModel.setSubtitle(settingsAccessor.getString(SettingsFieldNames.SUBTITLE));
        commonCardFieldsModel.setTraits(settingsAccessor.getString(SettingsFieldNames.TRAITS));
        commonCardFieldsModel.setKeywords(settingsAccessor.getString(SettingsFieldNames.KEYWORDS));
        commonCardFieldsModel.setRules(settingsAccessor.getString(SettingsFieldNames.GAME_TEXT));
        commonCardFieldsModel.setFlavourText(settingsAccessor.getString(SettingsFieldNames.FLAVOR));
        commonCardFieldsModel.setVictory(settingsAccessor.getString(SettingsFieldNames.VICTORY));
        commonCardFieldsModel.setCopyright(settingsAccessor.getString("Copyright"));

        // TODO: spacing between each field
    }

    public static void populatePlayerCardFields(SettingsAccessor settingsAccessor, PlayerCardFieldsModel playerCardFieldsModel) {
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

    public static void populatingNumbering(DIY diy, SettingsAccessor settingsAccessor, NumberingModel numberingModel) {
        numberingModel.setCollectionNumber(settingsAccessor.getString(SettingsFieldNames.COLLECTION_NUMBER));
        numberingModel.setEncounterNumber(settingsAccessor.getString(SettingsFieldNames.ENCOUNTER_NUMBER));
        numberingModel.setEncounterTotal(settingsAccessor.getString(SettingsFieldNames.ENCOUNTER_TOTAL));

        // TODO: portraits
    }

    public static void populateArt(DIY diy, SettingsAccessor settingsAccessor, PortraitWithArtistModel artPortraitWithArtistModel) {
        artPortraitWithArtistModel.setArtist(settingsAccessor.getString(SettingsFieldNames.ARTIST));

        // TODO: portraits
    }
}
