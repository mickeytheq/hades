package com.mickeytheq.strangeeons.ahlcg4j.util;

import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.PaintContext;
import com.mickeytheq.strangeeons.ahlcg4j.codegenerated.GameConstants;
import resources.Language;

import java.awt.*;

public class PaintUtils {
    public static void paintLabel(PaintContext paintContext, Rectangle drawRegion, String labelText) {
        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getLargeLabelTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
        markupRenderer.setMarkupText(labelText);
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), drawRegion);

    }
}
