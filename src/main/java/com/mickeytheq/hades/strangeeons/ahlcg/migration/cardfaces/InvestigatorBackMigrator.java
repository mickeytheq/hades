package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import ca.cgjennings.apps.arkham.diy.DIY;
import com.mickeytheq.hades.core.model.cardfaces.InvestigatorBack;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.CardFaceMigrationContext;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsAccessor;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsFieldNames;
import org.apache.commons.lang3.Strings;

public class InvestigatorBackMigrator {
    public InvestigatorBack build(CardFaceMigrationContext context) {
        SettingsAccessor settingsAccessor = context.getSettingsAccessor();

        InvestigatorBack investigatorBack = new InvestigatorBack();

        MigrationUtils.populateArt(context, investigatorBack.getPortraitWithArtistModel());

        investigatorBack.setSection1(createSection(settingsAccessor, 1));
        investigatorBack.setSection2(createSection(settingsAccessor, 2));
        investigatorBack.setSection3(createSection(settingsAccessor, 3));
        investigatorBack.setSection4(createSection(settingsAccessor, 4));
        investigatorBack.setSection5(createSection(settingsAccessor, 5));
        investigatorBack.setSection6(createSection(settingsAccessor, 6));
        investigatorBack.setSection7(createSection(settingsAccessor, 7));
        investigatorBack.setSection8(createSection(settingsAccessor, 8));

        investigatorBack.setStory(settingsAccessor.getString(SettingsFieldNames.INVESTIGATOR_STORY));

        return investigatorBack;
    }

    private InvestigatorBack.InvestigatorBackSection createSection(SettingsAccessor settingsAccessor, int index) {
        InvestigatorBack.InvestigatorBackSection backSection = new InvestigatorBack.InvestigatorBackSection();

        // Back is always present at the end of the keys but the SettingsAccessor will add this

        // change from AHLCG - AHLCG auto-includes the <hdr> and : formatting during rendering. change this to be in the field itself
        String header = settingsAccessor.getString("Text" + index + "Name");
        header = Strings.CS.prependIfMissing(header, "<hdr>");
        header = Strings.CS.appendIfMissing(header, "</hdr>: ");

        backSection.setHeader(header);
        backSection.setText(settingsAccessor.getString("Text" + index));
        backSection.setAfterSpacing(settingsAccessor.getSpacingValue("Text" + index));

        return backSection;
    }
}
