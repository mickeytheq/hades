package com.mickeytheq.ahlcg4j.scratchpad;

import com.mickeytheq.ahlcg4j.strangeeons.plugin.Bootstrapper;
import com.mickeytheq.ahlcg4j.strangeeons.ui.FontInstallManager;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Scratch {
    public static void main(String[] args) {
        Bootstrapper.installHydraTheme();

        Path userHomePath = Paths.get(System.getProperty("user.home"));

        FontInstallManager fontInstallManager = new FontInstallManager();
        fontInstallManager.addRequiredFontInfo("ArnoPro-Regular", userHomePath.resolve("arnopro-regular.otf"));
        fontInstallManager.addRequiredFontInfo("ArnoPro-Bold", userHomePath.resolve("arnopro-bold.otf"));
        fontInstallManager.addRequiredFontInfo("ArnoPro-Italic", userHomePath.resolve("arnopro-italic.otf"));
        fontInstallManager.addRequiredFontInfo("ArnoPro-BoldItalic", userHomePath.resolve("arnopro-bolditalic.otf"));
        fontInstallManager.showFontSetupDialog();
    }
}
