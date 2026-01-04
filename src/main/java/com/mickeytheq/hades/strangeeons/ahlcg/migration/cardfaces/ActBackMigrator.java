package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import com.mickeytheq.hades.core.CardFaces;
import com.mickeytheq.hades.core.model.cardfaces.Act;
import com.mickeytheq.hades.core.model.cardfaces.ActBack;
import com.mickeytheq.hades.core.model.common.ActAgendaCommonFieldsModel;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.CardFaceMigrationContext;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsAccessor;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsFieldNames;

public class ActBackMigrator {
    public ActBack build(CardFaceMigrationContext context) {
        ActBack actBack = new ActBack();

        MigrationUtils.populateCommonCardFields(context, actBack.getCommonCardFieldsModel());
        MigrationUtils.populateEncounterSet(context, actBack.getEncounterSetModel());

        actBack.setShadowFront(true);

        MigrationUtils.populateActAgendaCommonFieldsModel(context.getSettingsAccessor(), "A", actBack.getSection1());
        MigrationUtils.populateActAgendaCommonFieldsModel(context.getSettingsAccessor(), "B", actBack.getSection2());
        MigrationUtils.populateActAgendaCommonFieldsModel(context.getSettingsAccessor(), "C", actBack.getSection3());

        return actBack;
    }
}
