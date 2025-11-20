package com.mickeytheq.strangeeons.ahlcg4j.cardfaces.backs;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import com.mickeytheq.strangeeons.ahlcg4j.Card;
import com.mickeytheq.strangeeons.ahlcg4j.CardFaceType;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.BaseCardFaceView;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.CardFaceView;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.PaintContext;
import com.mickeytheq.strangeeons.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.strangeeons.ahlcg4j.util.ImageUtils;
import resources.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class EncounterCardBackView extends BaseCardFaceView<EncounterCardBack> {
    @Override
    public BufferedImage loadTemplateImage() {
        return ImageUtils.loadImage(getClass().getResource("/templates/AHLCG-EncounterBack.jp2"));
    }

    @Override
    public void createEditors(JTabbedPane tabbedPane) {
        // nothing to do
    }

    @Override
    protected void paint(PaintContext paintContext) {
        // TODO: can we cache this instead of drawing it each time?
        // TODO: although may not be worth it. for bulk operations the sheet must be created a drawn from scratch so
        // TODO: this would only improve subsequent paints of the same sheet which aren't that high frequency
        paintContext.getGraphics().drawImage(loadTemplateImage(), 0, 0, null);
    }
}
