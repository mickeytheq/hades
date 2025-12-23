package com.mickeytheq.hades.strangeeons.ahlcg.migration;

import ca.cgjennings.apps.arkham.diy.DIY;
import com.mickeytheq.hades.core.project.configuration.ProjectConfiguration;
import com.mickeytheq.hades.core.view.CardFaceSide;

public interface CardFaceMigrationContext {
    DIY getDIY();

    CardFaceSide getCardFaceSide();

    ProjectConfiguration getProjectConfiguration();

    SettingsAccessor getSettingsAccessor();
}
