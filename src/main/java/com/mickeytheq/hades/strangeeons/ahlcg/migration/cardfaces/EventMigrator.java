package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import ca.cgjennings.apps.arkham.diy.DIY;
import com.mickeytheq.hades.core.model.cardfaces.Event;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsAccessor;

public class EventMigrator {
    public Event build(DIY diy, CardFaceSide cardFaceSide, SettingsAccessor settingsAccessor) {
        Event event = new Event();

        MigrationUtils.populateCommonCardFields(diy, cardFaceSide, settingsAccessor, event.getCommonCardFieldsModel());
        MigrationUtils.populatePlayerCardFields(settingsAccessor, event.getPlayerCardFieldsModel());
        MigrationUtils.populatingNumbering(diy, settingsAccessor, event.getNumberingModel());
        MigrationUtils.populateArt(diy, settingsAccessor, event.getPortraitWithArtistModel());

        // TODO: encounter/collection/artist + portrait

        return event;
    }
}
