package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import ca.cgjennings.apps.arkham.diy.DIY;
import com.mickeytheq.hades.core.model.cardfaces.Skill;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.CardFaceMigrationContext;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsAccessor;

public class SkillMigrator {
    public Skill build(CardFaceMigrationContext context) {
        Skill skill = new Skill();

        MigrationUtils.populateCommonCardFields(context, skill.getCommonCardFieldsModel());
        MigrationUtils.populatePlayerCardFields(context, skill.getPlayerCardFieldsModel());
        MigrationUtils.populateCollection(context, skill.getCollectionModel());
        MigrationUtils.populateEncounterSet(context, skill.getEncounterSetModel());
        MigrationUtils.populateArt(context, skill.getPortraitModel());

        return skill;
    }
}
