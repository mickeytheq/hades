package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import com.mickeytheq.hades.core.model.cardfaces.Act;
import com.mickeytheq.hades.core.model.common.Distance;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.CardFaceMigrationContext;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsAccessor;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsFieldNames;
import com.mickeytheq.hades.util.shape.Unit;

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

        act.getStorySectionModel().setStory(settingsAccessor.getString(SettingsFieldNames.ACT_STORY));
        act.getStorySectionModel().setAfterStorySpacing(new Distance(settingsAccessor.getSpacingValue(SettingsFieldNames.ACT_STORY), Unit.Point));

        // default logic will put 'rules' in the standard field, move it out
        act.getStorySectionModel().setRules(act.getCommonCardFieldsModel().getRules());
        act.getStorySectionModel().setRules(null);

        return act;
    }
}
