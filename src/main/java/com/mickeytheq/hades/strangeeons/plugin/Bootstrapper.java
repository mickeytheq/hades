package com.mickeytheq.hades.strangeeons.plugin;

import ca.cgjennings.imageio.JPEG2000;
import ca.cgjennings.io.protocols.MappedURLHandler;
import ca.cgjennings.ui.theme.HydraTheme;
import ca.cgjennings.ui.theme.Theme;
import com.mickeytheq.hades.util.FontUtils;
import resources.Language;

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

        FontUtils.loadFont("/fonts/arnopro-regular.otf");
        FontUtils.loadFont("/fonts/arnopro-bold.otf");
        FontUtils.loadFont("/fonts/arnopro-italic.otf");
        FontUtils.loadFont("/fonts/arnopro-bolditalic.otf");

        initialise();
    }

    public static void installHydraTheme() {
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
        FontUtils.loadFont("/fonts/AHLCGSymbol.ttf");
        FontUtils.loadFont("/fonts/Arkhamic.ttf");

        FontUtils.loadFont("/fonts/Bolton.ttf");

        FontUtils.loadFont("/fonts/BoltonBold.ttf");
        FontUtils.loadFont("/fonts/BoltonElongated.ttf");
        FontUtils.loadFont("/fonts/BoltonOutline.ttf");

        FontUtils.loadFont("/fonts/BoltonTitling.ttf");
        FontUtils.loadFont("/fonts/BoltonTitlingBold.ttf");
        FontUtils.loadFont("/fonts/BoltonTitlingOutline.ttf");
    }

    private static void loadLanguageFiles() {
        Language.getGame().addStrings("/language/Hades-Game");
        Language.getInterface().addStrings("/language/Hades-Interface");
    }
}
