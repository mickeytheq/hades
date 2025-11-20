package com.mickeytheq.strangeeons.ahlcg4j.cardfaces;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.layout.MarkupRenderer;

import java.awt.*;

public interface PaintContext {
    Graphics2D getGraphics();

    RenderTarget getRenderTarget();

    //
    // markup methods
    //

    // tag replacements
    // adds a tag replacement that will be used by other method(s)
    // for example "<fullname>" -> "Card title"
    void addTagReplacement(String tag, String replacement);

    // creates a MarkupRenderer with a resolution that matches the current sheet/template
    // and applies all previously added tag replacements to a MarkupRenderer
    MarkupRenderer createMarkupRenderer();
}
