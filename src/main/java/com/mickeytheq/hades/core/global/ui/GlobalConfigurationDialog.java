package com.mickeytheq.hades.core.global.ui;

import com.mickeytheq.hades.core.global.configuration.GlobalConfiguration;
import com.mickeytheq.hades.core.global.configuration.GlobalConfigurations;
import com.mickeytheq.hades.ui.DialogEx;

import javax.swing.*;
import java.awt.*;

public class GlobalConfigurationDialog extends DialogEx {
    public GlobalConfigurationDialog(Frame owner) {
        super(owner, false);

        setTitle("Hades global configuration"); // TODO: i18n

        GlobalConfiguration globalConfiguration = GlobalConfigurations.get();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Card preview", new PreviewPanel(globalConfiguration.getCardPreview())); // TODO: i18n

        setContentComponent(tabbedPane);

        addOkCancelButtons(() -> {
            GlobalConfigurations.save(globalConfiguration);
            return true;
        });
    }

    public static void openDialog(Frame owner) {
        new GlobalConfigurationDialog(owner).showDialog();
    }
}
