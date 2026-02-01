package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import com.mickeytheq.hades.core.model.cardfaces.Location;
import com.mickeytheq.hades.core.model.cardfaces.LocationBack;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.CardFaceMigrationContext;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;

public class LocationBackMigrator {
    public LocationBack build(CardFaceMigrationContext context) {
        LocationBack locationBack = new LocationBack();

        MigrationUtils.populateCommonCardFields(context, locationBack.getCommonCardFieldsModel());
//        MigrationUtils.populateCollection(context, locationBack.getCollectionModel());
//        MigrationUtils.populateEncounterSet(context, locationBack.getEncounterSetModel());
        MigrationUtils.populateArt(context, locationBack.getPortraitModel());
        MigrationUtils.populateLocationFields(context, locationBack.getLocationFieldsModel());

        return locationBack;
    }
}
