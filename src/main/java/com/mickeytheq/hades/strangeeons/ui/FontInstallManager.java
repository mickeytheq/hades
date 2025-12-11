package com.mickeytheq.hades.strangeeons.ui;

import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.ui.DialogWithButtons;
import com.mickeytheq.hades.ui.LoggingLevel;
import com.mickeytheq.hades.ui.ProgressDialog;
import com.mickeytheq.hades.util.FontUtils;
import com.mickeytheq.hades.util.UrlUtils;

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
    private final List<FontSourceInfo> requiredFontInfo = new ArrayList<>();

    // default implementation to look for the required Arno Pro fonts in the user's home directory
    public static boolean checkAndLaunchSetupIfRequired() {
        Path userHomePath = Paths.get(System.getProperty("user.home"));

        FontInstallManager fontInstallManager = new FontInstallManager();
        fontInstallManager.addRequiredFontInfo("ArnoPro-Regular", userHomePath.resolve("arnopro-regular.otf"), UrlUtils.fromString("https://www.dafontfree.net/arno-pro-regular-font/f64439.htm"));
        fontInstallManager.addRequiredFontInfo("ArnoPro-Bold", userHomePath.resolve("arnopro-bold.otf"), UrlUtils.fromString("https://www.dafontfree.net/arno-pro-bold-font/f64426.htm"));
        fontInstallManager.addRequiredFontInfo("ArnoPro-Italic", userHomePath.resolve("arnopro-italic.otf"), UrlUtils.fromString("https://www.dafontfree.net/arno-pro-italic-font/f64432.htm"));
        fontInstallManager.addRequiredFontInfo("ArnoPro-BoldItalic", userHomePath.resolve("arnopro-bolditalic.otf"), UrlUtils.fromString("https://www.dafontfree.net/arno-pro-bold-italic-font/f64427.htm"));

        fontInstallManager.tryLoadFontsQuietly();

        if (fontInstallManager.isAllFontsInstalled())
            return true;

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

            dialogWithButtons.addDialogClosingButton("Continue", 1, () -> {
                if (!isAllFontsInstalled()) {
                    JOptionPane.showMessageDialog(dialogWithButtons, "Not all fonts are installed");
                    return false;
                }

                return true;
            });

            dialogWithButtons.addDialogClosingButton("Cancel", DialogWithButtons.CANCEL_OPTION, () -> true);

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
            } catch (Exception e) {
                // ignore any exception
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
