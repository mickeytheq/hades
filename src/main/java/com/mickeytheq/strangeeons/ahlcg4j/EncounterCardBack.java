package com.mickeytheq.strangeeons.ahlcg4j;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import resources.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@CardFaceType(settingsTypeCode = "EncounterCardBack")
public class EncounterCardBack extends BaseCardFace {
    @Override
    public BufferedImage loadTemplateImage() {
        try {
            return ImageIO.read(getClass().getResource("/templates/AHLCG-EncounterBack.jp2"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createEditors(JTabbedPane tabbedPane) {
        // nothing to do
    }

    @Override
    protected void paint(Sheet<Card> sheet, RenderTarget renderTarget) {
        Graphics2D g = sheet.createGraphics();
        try {
            g.drawImage(loadTemplateImage(), 0, 0, null);
        } finally {
            g.dispose();
        }
    }

    @Override
    public void readFace(Settings settings) {

    }

    @Override
    public void writeFace(Settings settings) {

    }
}
