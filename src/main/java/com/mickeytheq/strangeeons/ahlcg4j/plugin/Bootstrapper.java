package com.mickeytheq.strangeeons.ahlcg4j.plugin;

import ca.cgjennings.imageio.JPEG2000;
import ca.cgjennings.io.protocols.MappedURLHandler;
import ca.cgjennings.ui.theme.HydraTheme;
import ca.cgjennings.ui.theme.Theme;
import resources.Language;
import resources.ResourceKit;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

public class Bootstrapper {
    // used for testing outside Strange Eons
    public static void initaliseOutsideStrangeEons() {
        JPEG2000.registerServiceProviders(true);
        Language.setGameLocale(Locale.ENGLISH);
        Language.setInterfaceLocale(Locale.ENGLISH);
        MappedURLHandler.install();
        installHydraTheme();

        initialise();
    }

    private static void installHydraTheme() {
        Theme theme = new HydraTheme();

        theme.modifyManagerDefaults(UIManager.getDefaults());
        LookAndFeel laf;
        try {
            Constructor<?> constructor = Class.forName(theme.getLookAndFeelClassName()).getConstructor();
            constructor.setAccessible(true);
            laf = (LookAndFeel) constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        theme.modifyLookAndFeel(laf);
        try {
            UIManager.setLookAndFeel(laf);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initialise() {
        loadFonts();
        loadLanguageFiles();
    }

    private static void loadFonts() {
        // TODO: not allowed to embed Arno Pro so instead check for and fail to start if Arno Pro isn't present
        // TODO: and provide (a link to) clear instructions to download the fonts and where to put them (plugin folder maybe?)
        loadFont("/fonts/arnopro-regular.otf");
        loadFont("/fonts/arnopro-bold.otf");
        loadFont("/fonts/arnopro-italic.otf");
        loadFont("/fonts/arnopro-bolditalic.otf");
        loadFont("/fonts/AHLCGSymbol.ttf");
        loadFont("/fonts/Arkhamic.ttf");
    }

    private static void loadFont(String resourcePath) {
        try (InputStream inputStream = Bootstrapper.class.getResourceAsStream(resourcePath)) {
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

    private static void loadLanguageFiles() {
        Language.getGame().addStrings("/language/AHLCG-Game");
        Language.getInterface().addStrings("/language/AHLCG-Interface");
    }
}
