package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import com.mickeytheq.hades.core.model.cardfaces.ActBack;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.CardFaceMigrationContext;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;

public class ActBackMigrator {
    public ActBack build(CardFaceMigrationContext context) {
        ActBack actBack = new ActBack();

        MigrationUtils.populateCommonCardFields(context, actBack.getCommonCardFieldsModel());
        MigrationUtils.populateEncounterSet(context, actBack.getEncounterSetModel());

        actBack.setShadowFront(true);

        MigrationUtils.populateStorySectionModel(context.getSettingsAccessor(), "A", actBack.getSection1());
        MigrationUtils.populateStorySectionModel(context.getSettingsAccessor(), "B", actBack.getSection2());
        MigrationUtils.populateStorySectionModel(context.getSettingsAccessor(), "C", actBack.getSection3());

        return actBack;
    }
}
