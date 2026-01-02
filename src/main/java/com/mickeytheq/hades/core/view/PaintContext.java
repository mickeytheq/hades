package com.mickeytheq.hades.core.view;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.configuration.ProjectConfiguration;

import java.awt.*;

/**
 * Context used when drawing/painting a {@link CardFaceView}
 */
public interface PaintContext {
    //
    // painting/drawing
    //

    // draw using this Graphics
    // implementations should return the same instance each time this is called so that alterations made
    // by earlier callers will persist to later callers
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

    //
    // utility
    //

    // gets the ProjectContext
    ProjectContext getProjectContext();
}
