package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import com.mickeytheq.hades.core.model.cardfaces.Investigator;
import com.mickeytheq.hades.core.model.common.InvestigatorClass;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.CardFaceMigrationContext;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsAccessor;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsFieldNames;

public class InvestigatorMigrator {
    public Investigator build(CardFaceMigrationContext context) {
        SettingsAccessor settingsAccessor = context.getSettingsAccessor();

        Investigator investigator = new Investigator();

        MigrationUtils.populateCommonCardFields(context, investigator.getCommonCardFieldsModel());
        MigrationUtils.populateCollection(context, investigator.getCollectionModel());
        MigrationUtils.populateEncounterSet(context, investigator.getEncounterSetModel());
        MigrationUtils.populateArt(context, investigator.getPortraitModel());

        investigator.getInvestigatorFieldsModel().setHealth(settingsAccessor.getString(SettingsFieldNames.HEALTH));
        investigator.getInvestigatorFieldsModel().setSanity(settingsAccessor.getString(SettingsFieldNames.SANITY));

        investigator.getInvestigatorFieldsModel().setWillpower(settingsAccessor.getString(SettingsFieldNames.WILLPOWER));
        investigator.getInvestigatorFieldsModel().setIntellect(settingsAccessor.getString(SettingsFieldNames.INTELLECT));
        investigator.getInvestigatorFieldsModel().setCombat(settingsAccessor.getString(SettingsFieldNames.COMBAT));
        investigator.getInvestigatorFieldsModel().setAgility(settingsAccessor.getString(SettingsFieldNames.AGILITY));

        // TODO: parallel investigators

        String cardClass = settingsAccessor.getString(SettingsFieldNames.CARD_CLASS);

        // story investigators don't have an explicit card class defined
        if (cardClass == null)
            investigator.getInvestigatorFieldsModel().setInvestigatorClass(InvestigatorClass.Story);
        else
            investigator.getInvestigatorFieldsModel().setInvestigatorClass(InvestigatorClass.valueOf(cardClass));

        return investigator;
    }
}
