package com.mickeytheq.hades.scratchpad;

import com.mickeytheq.hades.strangeeons.plugin.Bootstrapper;
import com.mickeytheq.hades.ui.LoggingLevel;
import com.mickeytheq.hades.ui.ProgressDialog;
import com.mickeytheq.hades.util.log4j.GlobalMemoryAppender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Scratch {
    public static void main(String[] args) throws Exception {
        Bootstrapper.installHydraTheme();

        ProgressDialog progressDialog = new ProgressDialog(LoggingLevel.Verbose);
        progressDialog.runWithinProgressDialog(() -> {
            Logger logger = LogManager.getLogger("com.mickeytheq.hades");
            logger.info("Info");
            logger.debug("Debug");
            logger.trace("Trace");
            return null;
        });

        String globalLog = GlobalMemoryAppender.getGlobalLog();

        System.out.println(globalLog);
    }
}
