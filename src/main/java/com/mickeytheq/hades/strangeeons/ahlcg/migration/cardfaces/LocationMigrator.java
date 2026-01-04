package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import com.mickeytheq.hades.core.model.cardfaces.Location;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.CardFaceMigrationContext;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsAccessor;

public class LocationMigrator {
    public Location build(CardFaceMigrationContext context) {
        Location location = new Location();

        MigrationUtils.populateCommonCardFields(context, location.getCommonCardFieldsModel());
        MigrationUtils.populateCollection(context, location.getCollectionModel());
        MigrationUtils.populateEncounterSet(context, location.getEncounterSetModel());
        MigrationUtils.populateArt(context, location.getPortraitModel());
        MigrationUtils.populateLocationFields(context, location.getLocationFieldsModel());

        return location;
    }
}
