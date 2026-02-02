package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import com.mickeytheq.hades.core.model.cardfaces.Location;
import com.mickeytheq.hades.core.model.cardfaces.LocationBack;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.CardFaceMigrationContext;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;

public class LocationBackMigrator {
    public LocationBack build(CardFaceMigrationContext context) {
        LocationBack locationBack = new LocationBack();

        MigrationUtils.populateCommonCardFields(context, locationBack.getCommonCardFieldsModel());
        MigrationUtils.populateArt(context, locationBack.getPortraitModel());
        MigrationUtils.populateLocationFields(context, locationBack.getLocationFieldsModel());

        // by default location backs copy their image
        locationBack.getPortraitModel().setCopyOtherFace(true);

        return locationBack;
    }
}
