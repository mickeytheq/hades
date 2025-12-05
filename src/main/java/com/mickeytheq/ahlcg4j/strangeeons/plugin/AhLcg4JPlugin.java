package com.mickeytheq.ahlcg4j.strangeeons.plugin;

import ca.cgjennings.apps.arkham.plugins.AbstractPlugin;
import ca.cgjennings.apps.arkham.plugins.Plugin;
import ca.cgjennings.apps.arkham.plugins.PluginContext;
import ca.cgjennings.apps.arkham.project.Actions;
import com.google.common.collect.Lists;
import com.mickeytheq.ahlcg4j.strangeeons.tasks.AhLcg4jActionTree;
import com.mickeytheq.ahlcg4j.strangeeons.ui.FontInstallManager;
import gamedata.ClassMap;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AhLcg4JPlugin extends AbstractPlugin {
    @Override
    public int getPluginType() {
        return Plugin.EXTENSION;
    }

    @Override
    public boolean initializePlugin(PluginContext context) {
        if (!checkFonts())
            return false;

        //        Game.register("AHLCG4J", "Arkham Horror: LCG");
        ClassMap.add("/AHLCG.classmap");

        Bootstrapper.initialise();

        Actions.register(new AhLcg4jActionTree(), Actions.PRIORITY_IMPORT_EXPORT);

        return true;
    }

    private boolean checkFonts() {
        Path userHomePath = Paths.get(System.getProperty("user.home"));

        FontInstallManager fontInstallManager = new FontInstallManager();
        fontInstallManager.addRequiredFontInfo("ArnoPro-Regular", userHomePath.resolve("arnopro-regular.otf"));
        fontInstallManager.addRequiredFontInfo("ArnoPro-Bold", userHomePath.resolve("arnopro-bold.otf"));
        fontInstallManager.addRequiredFontInfo("ArnoPro-Italic", userHomePath.resolve("arnopro-italic.otf"));
        fontInstallManager.addRequiredFontInfo("ArnoPro-BoldItalic", userHomePath.resolve("arnopro-bolditalic.otf"));

        fontInstallManager.tryLoadFontsQuietly();

        if (fontInstallManager.isAllFontsInstalled())
            return true;

        return fontInstallManager.showFontSetupDialog();
    }
}
