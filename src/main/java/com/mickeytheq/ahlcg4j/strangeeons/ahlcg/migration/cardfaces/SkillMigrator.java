package com.mickeytheq.ahlcg4j.strangeeons.ahlcg.migration.cardfaces;

import ca.cgjennings.apps.arkham.diy.DIY;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.Event;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.Skill;
import com.mickeytheq.ahlcg4j.core.view.CardFaceSide;
import com.mickeytheq.ahlcg4j.strangeeons.ahlcg.migration.MigrationUtils;
import com.mickeytheq.ahlcg4j.strangeeons.ahlcg.migration.SettingsAccessor;

public class SkillMigrator {
    public Skill build(DIY diy, CardFaceSide cardFaceSide, SettingsAccessor settingsAccessor) {
        Skill skill = new Skill();

        MigrationUtils.populateCommonCardFields(diy, cardFaceSide, settingsAccessor, skill.getCommonCardFieldsModel());
        MigrationUtils.populatePlayerCardFields(settingsAccessor, skill.getPlayerCardFieldsModel());
        MigrationUtils.populatingNumbering(diy, settingsAccessor, skill.getNumberingModel());
        MigrationUtils.populateArt(diy, settingsAccessor, skill.getPortraitWithArtistModel());

        return skill;
    }
}
