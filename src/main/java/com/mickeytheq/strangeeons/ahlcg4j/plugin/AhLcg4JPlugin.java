package com.mickeytheq.strangeeons.ahlcg4j.plugin;

import ca.cgjennings.apps.arkham.plugins.AbstractPlugin;
import ca.cgjennings.apps.arkham.plugins.Plugin;
import ca.cgjennings.apps.arkham.plugins.PluginContext;
import gamedata.ClassMap;
import gamedata.Game;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class AhLcg4JPlugin extends AbstractPlugin {
    @Override
    public int getPluginType() {
        return Plugin.EXTENSION;
    }

    @Override
    public boolean initializePlugin(PluginContext context) {
//        Game.register("AHLCG4J", "Arkham Horror: LCG");
        ClassMap.add("/AHLCG.classmap");

        loadFonts();

        return true;
    }

    private void loadFonts() {
        loadFont("/fonts/arnopro-regular.otf");
        loadFont("/fonts/arnopro-bold.otf");
        loadFont("/fonts/arnopro-italic.otf");
        loadFont("/fonts/arnopro-bolditalic.otf");
        loadFont("/fonts/AHLCGSymbol.ttf");
    }

    private void loadFont(String resourcePath) {
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null)
                throw new RuntimeException("Font file not found at resource path '" + resourcePath + "'");

            Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);

            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
        } catch (IOException e) {
            throw new RuntimeException("Error reading from font file '" + resourcePath + "'", e);
        } catch (FontFormatException e) {
            throw new RuntimeException("Format error loading font file '" + resourcePath + "'", e);
        }
    }
}
