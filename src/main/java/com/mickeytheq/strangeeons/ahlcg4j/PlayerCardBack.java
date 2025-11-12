package com.mickeytheq.strangeeons.ahlcg4j;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import com.mickeytheq.strangeeons.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.strangeeons.ahlcg4j.util.ImageUtils;
import resources.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@CardFaceType(typeCode = "PlayerCardBack", interfaceLanguageKey = InterfaceConstants.BACK_PLAYER)
public class PlayerCardBack extends BaseCardFace {
    @Override
    public BufferedImage loadTemplateImage() {
        return ImageUtils.loadImage(getClass().getResource("/templates/AHLCG-PlayerBack.jp2"));
    }

    @Override
    public void createEditors(JTabbedPane tabbedPane) {
        // nothing to do
    }

    @Override
    protected void paint(Sheet<Card> sheet, Graphics2D g, RenderTarget renderTarget) {
        // TODO: can we cache this instead of drawing it each time?
        // TODO: although may not be worth it. for bulk operations the sheet must be created a drawn from scratch so
        // TODO: this would only improve subsequent paints of the same sheet which aren't that high frequency
        g.drawImage(loadTemplateImage(), 0, 0, null);
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
