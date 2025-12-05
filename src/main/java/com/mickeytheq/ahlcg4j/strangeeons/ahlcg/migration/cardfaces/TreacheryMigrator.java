package com.mickeytheq.ahlcg4j.strangeeons.ahlcg.migration.cardfaces;

import ca.cgjennings.apps.arkham.diy.DIY;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.Skill;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.Treachery;
import com.mickeytheq.ahlcg4j.core.model.common.WeaknessType;
import com.mickeytheq.ahlcg4j.core.view.CardFaceSide;
import com.mickeytheq.ahlcg4j.strangeeons.ahlcg.migration.MigrationUtils;
import com.mickeytheq.ahlcg4j.strangeeons.ahlcg.migration.SettingsAccessor;
import com.mickeytheq.ahlcg4j.strangeeons.ahlcg.migration.SettingsFieldNames;
import org.apache.commons.lang3.StringUtils;

public class TreacheryMigrator {
    public Treachery build(DIY diy, CardFaceSide cardFaceSide, SettingsAccessor settingsAccessor) {
        Treachery treachery = new Treachery();

        MigrationUtils.populateCommonCardFields(diy, cardFaceSide, settingsAccessor, treachery.getCommonCardFieldsModel());
        MigrationUtils.populatingNumbering(diy, settingsAccessor, treachery.getNumberingModel());
        MigrationUtils.populateArt(diy, settingsAccessor, treachery.getPortraitWithArtistModel());

        treachery.setWeaknessType(WeaknessType.None);

        String subType = settingsAccessor.getString(SettingsFieldNames.SUBTYPE);

        if (!StringUtils.isEmpty(subType)) {
            String weaknessTypeStr = StringUtils.removeEnd(subType, "Weakness");

            treachery.setWeaknessType(WeaknessType.valueOf(weaknessTypeStr));
        }

        return treachery;
    }
}
