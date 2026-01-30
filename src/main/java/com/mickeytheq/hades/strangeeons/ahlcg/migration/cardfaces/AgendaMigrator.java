package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import com.mickeytheq.hades.core.model.cardfaces.Agenda;
import com.mickeytheq.hades.core.model.common.Distance;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.CardFaceMigrationContext;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsAccessor;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsFieldNames;
import com.mickeytheq.hades.util.shape.Unit;

public class AgendaMigrator {
    public Agenda build(CardFaceMigrationContext context) {
        SettingsAccessor settingsAccessor = context.getSettingsAccessor();

        Agenda agenda = new Agenda();

        MigrationUtils.populateCommonCardFields(context, agenda.getCommonCardFieldsModel());
        MigrationUtils.populateCollection(context, agenda.getCollectionModel());
        MigrationUtils.populateEncounterSet(context, agenda.getEncounterSetModel());
        MigrationUtils.populateArt(context, agenda.getPortraitModel());

        agenda.setAgendaNumber(settingsAccessor.getString("ScenarioIndex"));
        agenda.setDeckId(settingsAccessor.getString("ScenarioDeckID"));
        agenda.setDoom(MigrationUtils.parseStatistic(settingsAccessor, "Doom", SettingsFieldNames.PER_INVESTIGATOR));

        agenda.getStorySectionModel().setStory(settingsAccessor.getString(SettingsFieldNames.AGENDA_STORY));
        agenda.getStorySectionModel().setAfterStorySpace(new Distance(settingsAccessor.getSpacingValue(SettingsFieldNames.AGENDA_STORY), Unit.Pixel));

        // default logic will put 'rules' in the standard field, move it out
        agenda.getStorySectionModel().setRules(agenda.getCommonCardFieldsModel().getRules());
        agenda.getStorySectionModel().setRules(null);

        return agenda;
    }
}
