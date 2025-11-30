package com.mickeytheq.ahlcg4j.strangeeons.plugin;

import ca.cgjennings.apps.arkham.plugins.AbstractPlugin;
import ca.cgjennings.apps.arkham.plugins.Plugin;
import ca.cgjennings.apps.arkham.plugins.PluginContext;
import ca.cgjennings.apps.arkham.project.Actions;
import com.mickeytheq.ahlcg4j.strangeeons.tasks.AhLcg4jActionTree;
import gamedata.ClassMap;

public class AhLcg4JPlugin extends AbstractPlugin {
    @Override
    public int getPluginType() {
        return Plugin.EXTENSION;
    }

    @Override
    public boolean initializePlugin(PluginContext context) {
//        Game.register("AHLCG4J", "Arkham Horror: LCG");
        ClassMap.add("/AHLCG.classmap");

        Bootstrapper.initialise();

        // TODO: add a 'New' action that bypasses the regular SE new dialog and provides a better experience for creating a new card
        // TODO: allowing easy creation of common card types and completely custom/arbitrary front/back combinations

        Actions.register(new AhLcg4jActionTree(), Actions.PRIORITY_IMPORT_EXPORT);

        return true;
    }

}
