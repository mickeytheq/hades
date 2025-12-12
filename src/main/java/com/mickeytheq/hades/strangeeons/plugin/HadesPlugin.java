package com.mickeytheq.hades.strangeeons.plugin;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.ViewQuality;
import ca.cgjennings.apps.arkham.plugins.AbstractPlugin;
import ca.cgjennings.apps.arkham.plugins.Plugin;
import ca.cgjennings.apps.arkham.plugins.PluginContext;
import ca.cgjennings.apps.arkham.project.Actions;
import com.mickeytheq.hades.core.project.ProjectConfigurationProviderJson;
import com.mickeytheq.hades.core.project.ProjectConfigurations;
import com.mickeytheq.hades.strangeeons.tasks.HadesActionTree;
import com.mickeytheq.hades.strangeeons.ui.FontInstallManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

public class HadesPlugin extends AbstractPlugin {
    private static final Logger logger = LogManager.getLogger(HadesPlugin.class);

    @Override
    public int getPluginType() {
        return Plugin.EXTENSION;
    }

    @Override
    public boolean initializePlugin(PluginContext context) {
        // TODO: do we need to register anything?
        //        Game.register("Hades", "Arkham Horror: LCG");

        try {
            Bootstrapper.initialise();

            if (!checkFonts())
                return false;

            forceViewQualityToHighWithNoAutomaticChanging();

            // StrangeEons.getOpenProject is not resolvable until after the plugin has finished loading and StrangeEons has
            // finished starting. Therefore we use a Supplier of the Path so the loading of the config and path resolution can be deferred to first access
            ProjectConfigurations.setDefaultProvider(new ProjectConfigurationProviderJson(() -> StrangeEons.getOpenProject().getFile().toPath().resolve(ProjectConfigurationProviderJson.DEFAULT_FILENAME)));

            Actions.register(new HadesActionTree(), Actions.PRIORITY_IMPORT_EXPORT);

            return true;
        } catch (Exception e) {
            logger.fatal("Failed to initialise Hades plugin", e);
            throw e;
        }
    }

    private boolean checkFonts() {
        return FontInstallManager.checkAndLaunchSetupIfRequired();
    }

    // strange eons has this 'interesting' mechanic that automatically adjusts the view quality as a card is rendered
    // multiple times - you can see this in the SheetViewer.QualityManager class. From what I can tell if SE determines
    // that the card is rendering 'reasonably fast' then it will increase the quality. Unfortunately there is a significant
    // drop in performance when going from HIGH to ULTRAHIGH which decreases the repainting responsiveness noticeably
    //
    // we could ask/remind users to manual change the view quality from auto -> HIGH in the SE UI - however people might
    // not remember to do this so we're going to force it here
    //
    // it would be slightly cleaner to reflectively call ViewQuality.set() but this results in a permanent change as it
    // updates the Strange Eons settings
    private void forceViewQualityToHighWithNoAutomaticChanging() {
        try {
            Field field = ViewQuality.class.getDeclaredField("auto");
            field.setAccessible(true);
            field.set(null, false);

            field = ViewQuality.class.getDeclaredField("current");
            field.setAccessible(true);
            field.set(null, ViewQuality.HIGH);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("Failed to force view quality settings to desired values", e);
        }
    }
}
