package com.mickeytheq.ahlcg4j.core.view;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.layout.MarkupRenderer;

import java.awt.*;

/**
 * Context used when drawing/painting a {@link CardFaceView}
 */
public interface PaintContext {
    //
    // context basics
    //

    // draw using this Graphics
    Graphics2D getGraphics();

    // used to select quality vs speed
    RenderTarget getRenderTarget();

    // desired resolution of the rendering
    double getRenderingDpi();

    //
    // markup methods
    //

    // creates a MarkupRenderer with a resolution that matches the current sheet/template
    MarkupRenderer createMarkupRenderer();
}
