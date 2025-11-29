package com.mickeytheq.strangeeons.ahlcg4j.cardfaces.backs;

import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.BaseCardFaceView;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.EditorContext;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.PaintContext;
import com.mickeytheq.strangeeons.ahlcg4j.util.ImageUtils;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class EncounterCardBackView extends BaseCardFaceView<EncounterCardBack> {
    @Override
    public BufferedImage getTemplateImage() {
        return ImageUtils.loadImage(getClass().getResource("/templates/AHLCG-EncounterBack.jp2"));
    }

    @Override
    public void createEditors(EditorContext editorContext) {
        // nothing to do
    }

    @Override
    public void paint(PaintContext paintContext) {
        // TODO: can we cache this instead of drawing it each time?
        // TODO: although may not be worth it. for bulk operations the sheet must be created a drawn from scratch so
        // TODO: this would only improve subsequent paints of the same sheet which aren't that high frequency
        paintContext.getGraphics().drawImage(getTemplateImage(), 0, 0, null);
    }
}
