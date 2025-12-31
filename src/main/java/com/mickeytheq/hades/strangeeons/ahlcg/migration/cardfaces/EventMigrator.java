package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import ca.cgjennings.apps.arkham.diy.DIY;
import com.mickeytheq.hades.core.model.cardfaces.Event;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.CardFaceMigrationContext;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsAccessor;

public class EventMigrator {
    public Event build(CardFaceMigrationContext context) {
        Event event = new Event();

        MigrationUtils.populateCommonCardFields(context, event.getCommonCardFieldsModel());
        MigrationUtils.populatePlayerCardFields(context, event.getPlayerCardFieldsModel());
        MigrationUtils.populateCollection(context, event.getCollectionModel());
        MigrationUtils.populateEncounterSet(context, event.getEncounterSetModel());
        MigrationUtils.populateArt(context, event.getPortraitWithArtistModel());

        return event;
    }
}
