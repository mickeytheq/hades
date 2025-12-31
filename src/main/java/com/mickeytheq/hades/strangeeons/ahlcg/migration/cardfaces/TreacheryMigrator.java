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

        String subType = settingsAccessor.getString(SettingsFieldNames.SUBTYPE);

        treachery.setWeaknessType(getWeaknessType(subType));

        return treachery;
    }

    private WeaknessType getWeaknessType(String subType) {
        if (StringUtils.isEmpty(subType))
            return WeaknessType.None;

        if (subType.equals("Weakness"))
            return WeaknessType.Investigator;

        String weaknessType = Strings.CS.removeEnd(subType, "Weakness");

        return WeaknessType.valueOf(weaknessType);
    }
}
