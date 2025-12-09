package com.mickeytheq.hades.strangeeons.plugin;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.plugins.AbstractPlugin;
import ca.cgjennings.apps.arkham.plugins.Plugin;
import ca.cgjennings.apps.arkham.plugins.PluginContext;
import ca.cgjennings.apps.arkham.project.Actions;
import com.mickeytheq.hades.core.project.ProjectConfiguration;
import com.mickeytheq.hades.core.project.ProjectConfigurationProviderJson;
import com.mickeytheq.hades.core.project.ProjectConfigurations;
import com.mickeytheq.hades.strangeeons.tasks.HadesActionTree;
import com.mickeytheq.hades.strangeeons.ui.FontInstallManager;

import java.nio.file.Path;
import java.nio.file.Paths;

public class HadesPlugin extends AbstractPlugin {
    @Override
    public int getPluginType() {
        return Plugin.EXTENSION;
    }

    @Override
    public boolean initializePlugin(PluginContext context) {
        if (!checkFonts())
            return false;

        // TODO: do we need to register anything?
        //        Game.register("Hades", "Arkham Horror: LCG");

        Bootstrapper.initialise();

        // StrangeEons.getOpenProject is not resolvable until after the plugin has finished loading and StrangeEons has
        // finished starting. Therefore we use a Supplier of the Path so the loading of the config and path resolution can be deferred to first access
        ProjectConfigurations.setDefaultProvider(new ProjectConfigurationProviderJson(() -> StrangeEons.getOpenProject().getFile().toPath().resolve(ProjectConfigurationProviderJson.DEFAULT_FILENAME)));

        Actions.register(new HadesActionTree(), Actions.PRIORITY_IMPORT_EXPORT);

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
