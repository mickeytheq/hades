package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import com.mickeytheq.hades.core.model.cardfaces.InvestigatorMiniCard;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.CardFaceMigrationContext;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;

public class InvestigatorMiniCardMigrator {
    public InvestigatorMiniCard build(CardFaceMigrationContext context) {
        InvestigatorMiniCard investigatorMiniCard = new InvestigatorMiniCard();

        MigrationUtils.populateArt(context, investigatorMiniCard.getPortraitModel());

        return investigatorMiniCard;
    }
}
