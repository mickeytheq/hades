package com.mickeytheq.hades.strangeeons.ahlcg.migration;

import ca.cgjennings.apps.arkham.diy.DIY;
import com.mickeytheq.hades.core.project.configuration.ProjectConfiguration;
import com.mickeytheq.hades.core.view.CardFaceSide;

public interface CardFaceMigrationContext {
    DIY getDIY();

    String getTemplateKey();

    CardFaceSide getCardFaceSide();

    ProjectConfiguration getProjectConfiguration();

    SettingsAccessor getSettingsAccessor();

    // Hades uses templates that are larger than the existing AHLCG templates so some measurements that are in
    // pixels need to be scaled up. for example portrait panning/scaling and spacing
    double convertPixelSize(double pixelSize);
}
