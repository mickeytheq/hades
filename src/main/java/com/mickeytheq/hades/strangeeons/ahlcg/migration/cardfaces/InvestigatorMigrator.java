package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import ca.cgjennings.apps.arkham.diy.DIY;
import com.mickeytheq.hades.core.model.cardfaces.Investigator;
import com.mickeytheq.hades.core.model.common.InvestigatorClass;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.CardFaceMigrationContext;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsAccessor;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsFieldNames;

public class InvestigatorMigrator {
    public Investigator build(CardFaceMigrationContext context) {
        SettingsAccessor settingsAccessor = context.getSettingsAccessor();

        Investigator investigator = new Investigator();

        MigrationUtils.populateCommonCardFields(context, investigator.getCommonCardFieldsModel());
        MigrationUtils.populatingNumbering(context, investigator.getNumberingModel());
        MigrationUtils.populateArt(context, investigator.getPortraitWithArtistModel());

        investigator.setHealth(settingsAccessor.getString(SettingsFieldNames.HEALTH));
        investigator.setSanity(settingsAccessor.getString(SettingsFieldNames.SANITY));

        investigator.setWillpower(settingsAccessor.getString(SettingsFieldNames.WILLPOWER));
        investigator.setIntellect(settingsAccessor.getString(SettingsFieldNames.INTELLECT));
        investigator.setCombat(settingsAccessor.getString(SettingsFieldNames.COMBAT));
        investigator.setAgility(settingsAccessor.getString(SettingsFieldNames.AGILITY));

        // TODO: parallel investigators

        String cardClass = settingsAccessor.getString(SettingsFieldNames.CARD_CLASS);

        // story investigators don't have an explicit card class defined
        if (cardClass == null)
            investigator.setInvestigatorClass(InvestigatorClass.Story);
        else
            investigator.setInvestigatorClass(InvestigatorClass.valueOf(cardClass));

        return investigator;
    }
}
