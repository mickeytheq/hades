package com.mickeytheq.hades.core.view;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.utils.PaintUtils;
import com.mickeytheq.hades.util.shape.RectangleEx;

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

    // returns the resolution being rendered
    int getResolutionInPixelsPerInch();

    // switches the painting context to only rendering inside the bleed/on to the card content OR including the bleed
    //
    // when called with true
    // - the drawing region is the entire template including bleed
    //
    // when called with false
    // - the drawing region is the portion of the template excluding the bleed margin
    // - the origin (0,0) becomes the top left corner of the template as if it had no bleed, e.g. what actually makes up the card content
    // - any drawing outside the card content/into the bleed will be ignored
    //
    // this PaintContext starts in the state as-if this method had been called with 'true'
    //
    // when painting content that is exclusive to the card interior such as labels, text etc this should be set to false first so that
    // the coordinate system can work off the top left point of the real card content rather than the template. then if the bleed margin of the template
    // is changed nothing needs to be altered in the core painting
    //
    // when painting content that may spill into the bleed margin, such as the template itself or background art this should be done
    // when this is in 'true' mode. that painting code must then be bleed margin aware and position elements accordingly
    void setRenderingIncludeBleedRegion(boolean includeBleed);

    // paints the template image onto the Graphics2D of this PaintContext
    void paintTemplate();

    // convenience method to convert millimeters to pixels using the context's resolution
    default int millimetersToPixels(double millimeters) {
        return (int)Math.round(millimeters / PaintUtils.MILLIMETERS_PER_INCH * getResolutionInPixelsPerInch() + 0.5);
    }

    // creates a Rectangle in pixel units for the given input rectangle which may be in other units
    default Rectangle toPixelRect(RectangleEx rectangleEx) {
        return rectangleEx.toPixelRectangle(getResolutionInPixelsPerInch());
    }

    //
    // markup methods
    //

    // creates a MarkupRenderer with a resolution that matches the current template
    MarkupRenderer createMarkupRenderer();

    //
    // utility
    //

    // gets the ProjectContext
    ProjectContext getProjectContext();
}
