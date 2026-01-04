package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import com.mickeytheq.hades.core.model.cardfaces.ActBack;
import com.mickeytheq.hades.core.model.cardfaces.AgendaBack;
import com.mickeytheq.hades.core.model.common.ActAgendaCommonFieldsModel;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.CardFaceMigrationContext;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsAccessor;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsFieldNames;

public class AgendaBackMigrator {
    public AgendaBack build(CardFaceMigrationContext context) {
        AgendaBack agendaBack = new AgendaBack();

        MigrationUtils.populateCommonCardFields(context, agendaBack.getCommonCardFieldsModel());
        MigrationUtils.populateEncounterSet(context, agendaBack.getEncounterSetModel());

        agendaBack.setShadowFront(true);

        MigrationUtils.populateActAgendaCommonFieldsModel(context.getSettingsAccessor(), "A", agendaBack.getSection1());
        MigrationUtils.populateActAgendaCommonFieldsModel(context.getSettingsAccessor(), "B", agendaBack.getSection2());
        MigrationUtils.populateActAgendaCommonFieldsModel(context.getSettingsAccessor(), "C", agendaBack.getSection3());

        return agendaBack;
    }
}
