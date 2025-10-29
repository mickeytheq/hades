package com.mickeytheq.strangeeons.ahlcg4j;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import com.mickeytheq.strangeeons.ahlcg4j.util.ImageUtils;
import resources.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@CardFaceType(settingsTypeCode = "EncounterCardBack")
public class EncounterCardBack extends BaseCardFace {
    @Override
    public BufferedImage loadTemplateImage() {
        return ImageUtils.loadImage(getClass().getResource("/templates/AHLCG-EncounterBack.jp2"));
    }

    @Override
    public void createEditors(JTabbedPane tabbedPane) {
        // nothing to do
    }

    @Override
    protected void paint(Sheet<Card> sheet, RenderTarget renderTarget) {
        // TODO: can we cache this instead of drawing it each time?
        Graphics2D g = sheet.createGraphics();
        try {
            g.drawImage(loadTemplateImage(), 0, 0, null);
        } finally {
            g.dispose();
        }
    }

    @Override
    public void afterSettingsRead(Settings settings, ObjectInputStream objectInputStream) {

    }

    @Override
    public void beforeSettingsWrite(Settings settings) {

    }

    @Override
    public void afterSettingsWrite(ObjectOutputStream objectOutputStream) {

    }
}
