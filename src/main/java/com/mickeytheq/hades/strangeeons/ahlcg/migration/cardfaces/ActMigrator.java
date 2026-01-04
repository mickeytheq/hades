package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import com.mickeytheq.hades.core.model.cardfaces.Act;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.CardFaceMigrationContext;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsAccessor;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsFieldNames;

public class ActMigrator {
    public Act build(CardFaceMigrationContext context) {
        SettingsAccessor settingsAccessor = context.getSettingsAccessor();

        Act act = new Act();

        MigrationUtils.populateCommonCardFields(context, act.getCommonCardFieldsModel());
        MigrationUtils.populateCollection(context, act.getCollectionModel());
        MigrationUtils.populateEncounterSet(context, act.getEncounterSetModel());
        MigrationUtils.populateArt(context, act.getPortraitModel());

        act.setActNumber(settingsAccessor.getString("ScenarioIndex"));
        act.setDeckId(settingsAccessor.getString("ScenarioDeckID"));
        act.setClues(MigrationUtils.parseStatistic(settingsAccessor, "Clues", SettingsFieldNames.PER_INVESTIGATOR));

        act.getActCommonFieldsModel().setStory(settingsAccessor.getString(SettingsFieldNames.ACT_STORY));
        act.getActCommonFieldsModel().setAfterStorySpace(settingsAccessor.getSpacingValue(SettingsFieldNames.ACT_STORY));

        // default logic will put 'rules' in the standard field, move it out
        act.getActCommonFieldsModel().setRules(act.getCommonCardFieldsModel().getRules());
        act.getActCommonFieldsModel().setRules(null);

        return act;
    }
}
