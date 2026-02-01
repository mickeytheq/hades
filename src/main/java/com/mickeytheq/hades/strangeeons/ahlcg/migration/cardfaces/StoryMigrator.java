package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import com.mickeytheq.hades.core.model.cardfaces.Story;
import com.mickeytheq.hades.core.model.common.Distance;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.CardFaceMigrationContext;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsAccessor;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsFieldNames;
import com.mickeytheq.hades.util.shape.Unit;

public class StoryMigrator {
    public Story build(CardFaceMigrationContext context) {
        SettingsAccessor settingsAccessor = context.getSettingsAccessor();

        Story story = new Story();

        MigrationUtils.populateCommonCardFields(context, story.getCommonCardFieldsModel());
        MigrationUtils.populateCollection(context, story.getCollectionModel());
        MigrationUtils.populateEncounterSet(context, story.getEncounterSetModel());

        MigrationUtils.populateStorySectionModel(context.getSettingsAccessor(), "A", story.getSection1());
        MigrationUtils.populateStorySectionModel(context.getSettingsAccessor(), "B", story.getSection2());
        MigrationUtils.populateStorySectionModel(context.getSettingsAccessor(), "C", story.getSection3());

        // traits are encoded slightly differently than normal, has an 'A' suffix
        story.getCommonCardFieldsModel().setTraits(settingsAccessor.getString(SettingsFieldNames.TRAITS + "A"));
        story.getCommonCardFieldsModel().setAfterKeywordsSpacing(new Distance(settingsAccessor.getSpacingValue(SettingsFieldNames.TRAITS + "A"), Unit.Point));

        return story;
    }
}
