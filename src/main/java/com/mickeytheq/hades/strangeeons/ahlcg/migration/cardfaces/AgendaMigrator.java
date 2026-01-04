package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import com.mickeytheq.hades.core.model.cardfaces.Agenda;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.CardFaceMigrationContext;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsAccessor;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsFieldNames;

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

        agenda.getAgendaCommonFieldsModel().setStory(settingsAccessor.getString(SettingsFieldNames.AGENDA_STORY));
        agenda.getAgendaCommonFieldsModel().setAfterStorySpace(settingsAccessor.getSpacingValue(SettingsFieldNames.AGENDA_STORY));

        // default logic will put 'rules' in the standard field, move it out
        agenda.getAgendaCommonFieldsModel().setRules(agenda.getCommonCardFieldsModel().getRules());
        agenda.getAgendaCommonFieldsModel().setRules(null);

        return agenda;
    }
}
