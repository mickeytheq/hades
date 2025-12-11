package com.mickeytheq.hades.scratchpad;

import com.mickeytheq.hades.strangeeons.plugin.Bootstrapper;
import com.mickeytheq.hades.strangeeons.ui.FontInstallManager;
import com.mickeytheq.hades.ui.LoggingLevel;
import com.mickeytheq.hades.ui.ProgressDialog;

public class Scratch {
    public static void main(String[] args) throws Exception {
        Bootstrapper.installHydraTheme();

        FontInstallManager.checkAndLaunchSetupIfRequired();
    }
}
