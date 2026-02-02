package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import com.mickeytheq.hades.core.model.cardfaces.AgendaBack;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.CardFaceMigrationContext;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;

public class AgendaBackMigrator {
    public AgendaBack build(CardFaceMigrationContext context) {
        AgendaBack agendaBack = new AgendaBack();

        MigrationUtils.populateCommonCardFields(context, agendaBack.getCommonCardFieldsModel());
        MigrationUtils.populateEncounterSet(context, agendaBack.getEncounterSetModel());

        agendaBack.getAgendaFieldsModel().setCopyOtherFace(true);

        MigrationUtils.populateStorySectionModel(context.getSettingsAccessor(), "A", agendaBack.getSection1());
        MigrationUtils.populateStorySectionModel(context.getSettingsAccessor(), "B", agendaBack.getSection2());
        MigrationUtils.populateStorySectionModel(context.getSettingsAccessor(), "C", agendaBack.getSection3());

        return agendaBack;
    }
}
