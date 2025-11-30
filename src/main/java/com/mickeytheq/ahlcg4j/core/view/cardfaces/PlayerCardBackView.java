package com.mickeytheq.ahlcg4j.core.view.cardfaces;

import com.mickeytheq.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.PlayerCardBack;
import com.mickeytheq.ahlcg4j.core.view.View;
import com.mickeytheq.ahlcg4j.core.view.utils.ImageUtils;
import com.mickeytheq.ahlcg4j.core.view.BaseCardFaceView;
import com.mickeytheq.ahlcg4j.core.view.EditorContext;
import com.mickeytheq.ahlcg4j.core.view.PaintContext;

import java.awt.image.BufferedImage;

@View(interfaceLanguageKey = InterfaceConstants.BACK_PLAYER)
public class PlayerCardBackView extends BaseCardFaceView<PlayerCardBack> {
    @Override
    public BufferedImage getTemplateImage() {
        return ImageUtils.loadImage(getClass().getResource("/templates/AHLCG-PlayerBack.jp2"));
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
