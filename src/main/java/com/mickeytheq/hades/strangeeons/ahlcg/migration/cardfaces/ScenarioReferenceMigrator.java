package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import com.mickeytheq.hades.core.model.cardfaces.ActBack;
import com.mickeytheq.hades.core.model.cardfaces.ScenarioReference;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.CardFaceMigrationContext;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsAccessor;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsFieldNames;
import org.apache.commons.lang3.StringUtils;

public class ScenarioReferenceMigrator {
    public ScenarioReference build(CardFaceMigrationContext context) {
        ScenarioReference scenarioReference = new ScenarioReference();

        MigrationUtils.populateCommonCardFields(context, scenarioReference.getCommonCardFieldsModel());
        MigrationUtils.populateEncounterSet(context, scenarioReference.getEncounterSetModel());
        MigrationUtils.populateCollection(context, scenarioReference.getCollectionModel());

        scenarioReference.setDifficulty(context.getCardFaceSide() == CardFaceSide.Front ? ScenarioReference.Difficulty.EasyStandard : ScenarioReference.Difficulty.HardExpert);

        SettingsAccessor settingsAccessor = context.getSettingsAccessor();

        scenarioReference.setTrackingBox(settingsAccessor.getRawSettingsValue("TrackerBox"));

        populateChaosSymbolInfo(settingsAccessor, scenarioReference.getSkull(), SettingsFieldNames.CHAOS_TOKEN_SKULL);
        populateChaosSymbolInfo(settingsAccessor, scenarioReference.getCultist(), SettingsFieldNames.CHAOS_TOKEN_CULTIST);
        populateChaosSymbolInfo(settingsAccessor, scenarioReference.getTablet(), SettingsFieldNames.CHAOS_TOKEN_TABLET);
        populateChaosSymbolInfo(settingsAccessor, scenarioReference.getElderThing(), SettingsFieldNames.CHAOS_TOKEN_ELDER_THING);

        return scenarioReference;
    }

    private void populateChaosSymbolInfo(SettingsAccessor settingsAccessor, ScenarioReference.SymbolChaosTokenInfo chaosTokenInfo, String settingKey) {
        chaosTokenInfo.setRules(settingsAccessor.getString(settingKey));

        String combineWith = settingsAccessor.getString(SettingsFieldNames.CHAOS_TOKEN_MERGE_PREFIX + settingKey);

        if (!StringUtils.isEmpty(combineWith)) {
            chaosTokenInfo.setCombineWith(ScenarioReference.SymbolChaosToken.valueOf(combineWith));
        }
    }
}
