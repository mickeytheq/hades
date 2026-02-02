package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import ca.cgjennings.apps.arkham.diy.DIY;
import com.mickeytheq.hades.core.model.cardfaces.Treachery;
import com.mickeytheq.hades.core.model.common.WeaknessType;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.CardFaceMigrationContext;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsAccessor;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsFieldNames;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

public class TreacheryMigrator {
    public Treachery build(CardFaceMigrationContext context) {
        SettingsAccessor settingsAccessor = context.getSettingsAccessor();

        Treachery treachery = new Treachery();

        MigrationUtils.populateCommonCardFields(context, treachery.getCommonCardFieldsModel());
        MigrationUtils.populateCollection(context, treachery.getCollectionModel());
        MigrationUtils.populateEncounterSet(context, treachery.getEncounterSetModel());
        MigrationUtils.populateArt(context, treachery.getPortraitModel());

        treachery.getTreacheryFieldsModel().setWeaknessType(MigrationUtils.getWeaknessType(settingsAccessor.getString(SettingsFieldNames.SUBTYPE)));

        return treachery;
    }
}
