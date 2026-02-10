package com.mickeytheq.hades.strangeeons.ui;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.plugins.BundleInstaller;
import ca.cgjennings.apps.arkham.plugins.PluginBundle;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.ui.DialogWithButtons;
import com.mickeytheq.hades.ui.LoggingLevel;
import com.mickeytheq.hades.ui.ProgressDialog;
import com.mickeytheq.hades.util.FontUtils;
import com.mickeytheq.hades.util.UrlUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class FontInstallManager {
    private static final Logger logger = LogManager.getLogger(FontInstallManager.class);

    private final List<FontSourceInfo> requiredFontInfo = new ArrayList<>();

    // default implementation to look for the required Arno Pro fonts in the user's home directory
    public static boolean checkAndLaunchSetupIfRequired() {
        Path fontInstallDirectory = BundleInstaller.PLUGIN_FOLDER.toPath();

        FontInstallManager fontInstallManager = new FontInstallManager();
        fontInstallManager.addRequiredFontInfo("ArnoPro-Regular", fontInstallDirectory.resolve("arnopro-regular.otf"), UrlUtils.fromString("https://www.dafontfree.net/arno-pro-regular-font/f64439.htm"));
        fontInstallManager.addRequiredFontInfo("ArnoPro-Bold", fontInstallDirectory.resolve("arnopro-bold.otf"), UrlUtils.fromString("https://www.dafontfree.net/arno-pro-bold-font/f64426.htm"));
        fontInstallManager.addRequiredFontInfo("ArnoPro-Italic", fontInstallDirectory.resolve("arnopro-italic.otf"), UrlUtils.fromString("https://www.dafontfree.net/arno-pro-italic-font/f64432.htm"));
        fontInstallManager.addRequiredFontInfo("ArnoPro-BoldItalic", fontInstallDirectory.resolve("arnopro-bolditalic.otf"), UrlUtils.fromString("https://www.dafontfree.net/arno-pro-bold-italic-font/f64427.htm"));

        logger.info("Trying to load ArnoPro fonts...");

        fontInstallManager.tryLoadFontsQuietly();

        if (fontInstallManager.isAllFontsInstalled()) {
            logger.info("All ArnoPro required fonts detected as installed");
            return true;
        }

        logger.info("One or more ArnoPro fonts are missing. Launching font setup...");

        return fontInstallManager.showFontSetupDialog();
    }

    public void addRequiredFontInfo(String fontName, Path expectedPath, URL downloadLocation) {
        requiredFontInfo.add(new FontSourceInfo(fontName, expectedPath, downloadLocation));
    }

    // return true if the dialog was completed successfully
    public boolean showFontSetupDialog() {
        return new FontSetupDialog().showDialog() != DialogWithButtons.CANCEL_OPTION;
    }

    class FontSetupDialog {
        private final Map<FontSourceInfo, JTextField> fontNameStatusMap = new HashMap<>();
        private final DialogWithButtons dialogWithButtons;
        public FontSetupDialog() {
            JPanel panel = MigLayoutUtils.createTitledPanel("Required fonts");

            for (FontSourceInfo fontSourceInfo : requiredFontInfo) {
                String fontName = fontSourceInfo.getFontName();
                Path expectedPath = fontSourceInfo.getExpectedPath();
                URL downloadLocation = fontSourceInfo.getDownloadLocation();

                panel.add(new JLabel("Font name: "));
                JTextField fontNameTextField = new JTextField(fontName, 20);
                fontNameTextField.setEditable(false);
                panel.add(fontNameTextField);

                panel.add(new JLabel("Expected location: "));
                JTextField locationTextField = new JTextField(40);
                locationTextField.setEditable(false);
                locationTextField.setText(expectedPath.toString());
                panel.add(locationTextField, "growx");

                panel.add(new JLabel("Status: "));
                JTextField statusTextField = new JTextField(20);
                statusTextField.setEditable(false);

                fontNameStatusMap.put(fontSourceInfo, statusTextField);
                panel.add(statusTextField, "growx");

                JButton openFolderButton = new JButton("Open install location");
                openFolderButton.addActionListener(e -> {
                    try {
                        Desktop.getDesktop().open(expectedPath.getParent().toFile());
                    } catch (IOException ex) {
                        // TODO: do something more user friendly here?
                        throw new RuntimeException(ex);
                    }
                });

                panel.add(openFolderButton);

                if (downloadLocation != null) {
                    JButton openDownloadLocation = new JButton("Download location");
                    openDownloadLocation.addActionListener(e -> {
                        try {
                            Desktop.getDesktop().browse(downloadLocation.toURI());
                        } catch (IOException | URISyntaxException ex) {
                            // TODO: do something more user friendly here?
                            throw new RuntimeException(ex);
                        }
                    });

                    panel.add(openDownloadLocation, "wrap");
                }
                else {
                    // empty cell to wrap to next line
                    panel.add(new JPanel(), "wrap");
                }
            }

            // TODO: have buttons to open the target folder and navigate to the download page

            // dialog
            dialogWithButtons = new DialogWithButtons((Frame)null, false);
            dialogWithButtons.setContentComponent(panel);
            dialogWithButtons.setTitle("Font setup");
            dialogWithButtons.setResizable(false);

            dialogWithButtons.addButton("Refresh", e -> {
                updateFontStatus();
            });

            dialogWithButtons.addDialogClosingButton("Continue", DialogWithButtons.OK_OPTION, () -> {
                if (!isAllFontsInstalled()) {
                    // if fonts aren't ready make sure the user is happy to continue
                    return JOptionPane.showConfirmDialog(dialogWithButtons,
                            "Not all fonts are installed and Hades will not function correctly. Are you sure you wish to continue?",
                            "Fonts not installed",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                }

                return true;
            });

            updateFontStatus();
        }

        public int showDialog() {
            return dialogWithButtons.showDialog();
        }

        public void updateFontStatus() {
            ProgressDialog progressDialog = new ProgressDialog(LoggingLevel.Normal);
            progressDialog.runWithinProgressDialog(() -> {
                Font[] installedFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();

                boolean allInstalled = true;

                for (Map.Entry<FontSourceInfo, JTextField> entry : fontNameStatusMap.entrySet()) {
                    FontSourceInfo fontSourceInfo = entry.getKey();
                    JTextField textField = entry.getValue();

                    String fontName = fontSourceInfo.getFontName();

                    boolean installed = isFontInstalled(installedFonts, fontName);

                    if (!installed) {
                        // try and load the font
                        try {
                            Path fontPath = fontSourceInfo.getExpectedPath();

                            FontUtils.loadFont(fontPath);

                            progressDialog.addLine("Loaded font '" + fontName + "' successfully");
                            installed = true;
                        } catch (Exception e) {
                            progressDialog.addLine("Failed to load font '" + fontName + "'" + e.getMessage());
                        }
                    }

                    if (installed) {
                        textField.setText("Installed");
                    }
                    else {
                        allInstalled = false;
                        textField.setText("Not installed");
                    }
                }

                if (allInstalled) {
                    progressDialog.addLine("All fonts loaded successfully");
                }
                else {
                    progressDialog.addLine("One or more fonts are missing. See above for details");
                }

                return null;
            });
        }
    }

    public void tryLoadFontsQuietly() {
        for (FontSourceInfo fontSourceInfo : requiredFontInfo) {
            try {
                FontUtils.loadFont(fontSourceInfo.getExpectedPath());
                logger.debug("Successfully loaded font with name " + fontSourceInfo.getFontName() + " from path " + fontSourceInfo.getExpectedPath());
            } catch (Exception e) {
                logger.warn("Failed to load font with name " + fontSourceInfo.getFontName() + " from path " + fontSourceInfo.getExpectedPath(), e);
            }
        }
    }

    public boolean isAllFontsInstalled() {
        Font[] installedFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();

        for (FontSourceInfo fontSourceInfo : requiredFontInfo) {
            if (!isFontInstalled(installedFonts, fontSourceInfo.getFontName()))
                return false;
        }

        return true;
    }

    private static boolean isFontInstalled(Font[] installedFonts, String fontName) {
        Optional<Font> matchedFont = Arrays.stream(installedFonts)
                .filter(o -> o.getName().equals(fontName))
                .findFirst();

        return matchedFont.isPresent();
    }

    static class FontSourceInfo {
        private final String fontName;
        private final Path expectedPath;
        private final URL downloadLocation;

        public FontSourceInfo(String fontName, Path expectedPath, URL downloadLocation) {
            this.fontName = fontName;
            this.expectedPath = expectedPath;
            this.downloadLocation = downloadLocation;
        }

        public String getFontName() {
            return fontName;
        }

        public Path getExpectedPath() {
            return expectedPath;
        }

        public URL getDownloadLocation() {
            return downloadLocation;
        }
    }
}
