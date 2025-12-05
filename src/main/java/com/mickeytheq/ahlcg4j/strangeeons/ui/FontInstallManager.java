package com.mickeytheq.ahlcg4j.strangeeons.ui;

import com.mickeytheq.ahlcg4j.ui.DialogWithButtons;
import com.mickeytheq.ahlcg4j.util.FontUtils;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.*;

public class FontInstallManager {
    private static final int CANCEL_CODE = 0;

    private final Map<String, Path> requiredFontInfo = new LinkedHashMap<>();

    public void addRequiredFontInfo(String fontName, Path expectedPath) {
        requiredFontInfo.put(fontName, expectedPath);
    }

    // return true if the dialog was completed successfully
    public boolean showFontSetupDialog() {
        return new FontSetupDialog().showDialog() != CANCEL_CODE;
    }

    class FontSetupDialog {
        private final Map<String, JTextField> fontNameStatusMap = new HashMap<>();
        private final DialogWithButtons dialogWithButtons;
        private final JTextArea logTextArea;

        public FontSetupDialog() {
            JPanel panel = new JPanel(new MigLayout());

            for (String fontName : requiredFontInfo.keySet()) {
                panel.add(new JLabel("Font name: "));
                JTextField fontNameTextField = new JTextField(fontName, 20);
                panel.add(fontNameTextField);

                panel.add(new JLabel("Expected location: "));
                JTextField locationTextField = new JTextField(40);
                locationTextField.setEditable(false);
                locationTextField.setText(requiredFontInfo.get(fontName).toString());
                panel.add(locationTextField, "growx");

                panel.add(new JLabel("Status: "));
                JTextField statusTextField = new JTextField(20);
                statusTextField.setEditable(false);

                fontNameStatusMap.put(fontName, statusTextField);
                panel.add(statusTextField, "wrap, growx");
            }

            // TODO: have buttons to open the target folder and navigate to the download page

            logTextArea = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(logTextArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            panel.add(scrollPane, "span, growx, wrap");

            // dialog
            dialogWithButtons = new DialogWithButtons(panel, false);
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

            dialogWithButtons.addDialogClosingButton("Cancel", CANCEL_CODE, () -> true);

            updateFontStatus();
        }

        public int showDialog() {
            return dialogWithButtons.showDialog();
        }

        public void updateFontStatus() {
            Font[] installedFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();

            boolean allInstalled = true;

            for (Map.Entry<String, JTextField> entry : fontNameStatusMap.entrySet()) {
                String fontName = entry.getKey();
                JTextField textField = entry.getValue();

                boolean installed = isFontInstalled(installedFonts, fontName);

                if (!installed) {
                    // try and load the font
                    try {
                        Path fontPath = requiredFontInfo.get(fontName);

                        FontUtils.loadFont(fontPath);

                        logTextArea.append("Loaded font '" + fontName + "' successfully");
                        logTextArea.append(System.lineSeparator());
                        installed = true;
                    } catch (Exception e) {
                        logTextArea.append("Failed to load font '" + fontName + "'" + e.getMessage());
                        logTextArea.append(System.lineSeparator());
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
                logTextArea.append("All fonts loaded successfully");
                logTextArea.append(System.lineSeparator());
            }
            else {
                logTextArea.append("One or more fonts are missing. See above for details");
                logTextArea.append(System.lineSeparator());
            }
        }
    }

    public void tryLoadFontsQuietly() {
        Font[] installedFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();

        for (Map.Entry<String, Path> entry : requiredFontInfo.entrySet()) {
            try {
                FontUtils.loadFont(entry.getValue());
            } catch (Exception e) {
                // ignore any exception
            }
        }
    }

    public boolean isAllFontsInstalled() {
        Font[] installedFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();

        for (String requiredFontName : requiredFontInfo.keySet()) {
            if (!isFontInstalled(installedFonts, requiredFontName))
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
}
