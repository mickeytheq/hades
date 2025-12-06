package com.mickeytheq.hades.scratchpad;

import com.mickeytheq.hades.strangeeons.plugin.Bootstrapper;
import com.mickeytheq.hades.ui.LoggingLevel;
import com.mickeytheq.hades.ui.ProgressDialog;

public class Scratch {
    public static void main(String[] args) throws Exception {
        Bootstrapper.installHydraTheme();

//        Path userHomePath = Paths.get(System.getProperty("user.home"));
//
//        FontInstallManager fontInstallManager = new FontInstallManager();
//        fontInstallManager.addRequiredFontInfo("ArnoPro-Regular", userHomePath.resolve("arnopro-regular.otf"));
//        fontInstallManager.addRequiredFontInfo("ArnoPro-Bold", userHomePath.resolve("arnopro-bold.otf"));
//        fontInstallManager.addRequiredFontInfo("ArnoPro-Italic", userHomePath.resolve("arnopro-italic.otf"));
//        fontInstallManager.addRequiredFontInfo("ArnoPro-BoldItalic", userHomePath.resolve("arnopro-bolditalic.otf"));
//        fontInstallManager.showFontSetupDialog();

        ProgressDialog progressDialog = new ProgressDialog(LoggingLevel.Debug);
        progressDialog.runWithinProgressDialog(() -> {
            for (int i = 0; i < 10; i++) {
                progressDialog.addLine("Message" + i);
                Thread.sleep(1000);
            }

            return null;
        });
    }
}
