package com.mickeytheq.hades.core.view.common;

import ca.cgjennings.apps.arkham.PortraitPanel;
import ca.cgjennings.apps.arkham.component.AbstractPortrait;
import ca.cgjennings.apps.arkham.component.Portrait;
import ca.cgjennings.graphics.ImageUtilities;
import ca.cgjennings.graphics.filters.InversionFilter;
import com.mickeytheq.hades.core.model.common.PortraitModel;
import com.mickeytheq.hades.core.view.EditorContext;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.utils.ImageUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

// a view of a portrait using Strange Eons existing portrait functionality
//
// the Portrait implementation is embedded/out of sight of other code and mainly serves as a bridge to the PortraitModel
// which has all the persistent state
//
// some of the code was lifted from Strange Eons and a lot of unused elements stripped out
public class PortraitView {
    public static final URL BLANK_PORTRAIT_IMAGE_RESOURCE = PortraitModel.class.getResource("/resources/spacers/empty1x1.png");
    public static final URL DEFAULT_PORTRAIT_IMAGE_RESOURCE = PortraitModel.class.getResource("/portrait/DefaultTexture.jp2");
    private final PortraitModel portraitModel;
    private final Dimension portraitDrawDimension;

    private final URL defaultImageResource;

    // the local transient image - ideally we would always rely on the model's image but when using a default
    // image we don't want to put that in the model so a local copy is maintained here
    private BufferedImage image;

    // private to ensure that a defaultImageResource is always provided, even if just an empty image
    // the default is used whenever the model's source is null
    //
    // we have to know the Dimension of the draw area at this stage as it is used by the portrait editor control
    // to show an overlay of what part of image will be visible or not when adjusting the pan/scale settings. it also
    // allows automatic scaling of a new image to an appropriate size
    //
    // the exact draw region/rectangle is only needed when painting
    private PortraitView(PortraitModel portraitModel, Dimension portraitDrawDimension, URL defaultImageResource) {
        this.portraitModel = portraitModel;
        this.portraitDrawDimension = portraitDrawDimension;
        this.defaultImageResource = defaultImageResource;

        if (portraitModel.getImage() != null) {
            image = portraitModel.getImage();
        }
        else {
            installDefaultImage();
        }
    }

    // create a new PortraitView with the given model and draw dimension which defaults to a blank image when
    // no source is provided by the model
    public static PortraitView createWithBlankImage(PortraitModel portraitModel, Dimension portraitDrawDimension) {
        return new PortraitView(portraitModel, portraitDrawDimension, BLANK_PORTRAIT_IMAGE_RESOURCE);
    }

    // create a new PortraitView with the given model and draw dimension which defaults to a standard/default image when
    // no source is provided by the model
    public static PortraitView createWithDefaultImage(PortraitModel portraitModel, Dimension portraitDrawDimension) {
        return new PortraitView(portraitModel, portraitDrawDimension, DEFAULT_PORTRAIT_IMAGE_RESOURCE);
    }

    private void installDefaultImage() {
        // install the default image
        // note that we do not set the model's image when installing a default as default images are not persisted
        image = ImageUtils.loadImage(defaultImageResource);
        calculateImageDefaults();
    }

    private void installImage(BufferedImage image) {
        // set a user-specified image by updating the model and the local image view
        portraitModel.setImage(image);
        this.image = image;
        calculateImageDefaults();
    }

    private void calculateImageDefaults() {
        // set pan/scale/rotation to sensible defaults
        portraitModel.setPanX(0);
        portraitModel.setPanY(0);
        portraitModel.setScale(computeDefaultImageScale(image));
        portraitModel.setRotation(0);
    }

    public Dimension getPortraitDrawDimension() {
        return portraitDrawDimension;
    }

    // creates a portrait panel hosting this portrait content and passes changed events back through the EditorContext
    public PortraitPanel createPortraitPanel(EditorContext editorContext, String panelTitle) {
        PortraitPanel portraitPanel = new PortraitPanel();
        portraitPanel.setPanelTitle(panelTitle);
        portraitPanel.setPortrait(new PortraitImpl(editorContext::markChanged));
        return portraitPanel;
    }

    private static final double DEGREES_TO_RADIANS = -0.0174532925d;

    private boolean getClipping() {
        return true;
    }

    private boolean fillBackground = true;

    public final void setBackgroundFilled(boolean fillBackground) {
        if (this.fillBackground != fillBackground) {
            this.fillBackground = fillBackground;
        }
    }

    public final boolean isBackgroundFilled() {
        return fillBackground;
    }

    public void paint(PaintContext paintContext, Rectangle drawRegion, boolean inverted) {
        Graphics2D g = paintContext.getGraphics();

        BufferedImage image = this.image;
        if (image == null) {
            return;
        }

        Shape oldClip = null;
        final boolean obeyClip = getClipping();
        if (obeyClip) {
            oldClip = g.getClip();
            g.setClip(drawRegion);
        }

        if (isBackgroundFilled()) {
            final Paint p = g.getPaint();
            g.setPaint(Color.WHITE);
            g.fillRect(drawRegion.x, drawRegion.y, drawRegion.width, drawRegion.height);
            g.setPaint(p);
        }

        // sometimes we want the portrait inverted, for example when painting an icon provided with a black foreground
        // onto a black background. in this case the inversion will convert black to white
        if (inverted) {
            BufferedImageOp inversionOp = new InversionFilter();
            image = inversionOp.filter(image, null);
        }

        double rotation = portraitModel.getRotation();
        double panX = portraitModel.getPanX();
        double panY = portraitModel.getPanY();
        double scale = portraitModel.getScale();

        final double scaledWidth = image.getWidth() * scale;
        final double scaledHeight = image.getHeight() * scale;

        final double centerX = scaledWidth / 2d;
        final double centerY = scaledHeight / 2d;

        // note that the pan value is relative to the center of the clip region;
        // i.e. a pan of (0,0) always centers the image
        final double regionX = drawRegion.getCenterX();
        final double regionY = drawRegion.getCenterY();

        if (rotation != 0) {
            AffineTransform xform = AffineTransform.getTranslateInstance(regionX - centerX + panX, regionY - centerY + panY);
            xform.concatenate(AffineTransform.getRotateInstance(rotation * DEGREES_TO_RADIANS, centerX, centerY));
            xform.concatenate(AffineTransform.getScaleInstance(scale, scale));
            AffineTransformOp xformop = new AffineTransformOp(xform, paintContext.getRenderTarget().getTransformInterpolationType());
            g.drawImage(image, xformop, 0, 0);
        } else {
            final int x0 = (int) (regionX - centerX + panX);
            final int y0 = (int) (regionY - centerY + panY);
            final int w = (int) (scaledWidth + 0.5d);
            final int h = (int) (scaledHeight + 0.5d);
            g.drawImage(image, x0, y0, w, h, null);
        }

        if (obeyClip) {
            g.setClip(oldClip);
        }
    }

    private double computeDefaultImageScale(BufferedImage image) {
        Dimension clip = getPortraitDrawDimension();
        double idealWidth = clip.getWidth();
        double idealHeight = clip.getHeight();
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();

        return ImageUtilities.idealCoveringScaleForImage(idealWidth, idealHeight, imageWidth, imageHeight);
    }

    private class PortraitImpl extends AbstractPortrait {
        private final Runnable changeListener;

        public PortraitImpl(Runnable changeListener) {
            this.changeListener = changeListener;
        }

        @Override
        public void setSource(String newSource) {
            URL sourceUrl = urlFromSource(newSource);

            // if the source is null then reset the image to the default
            if (sourceUrl == null) {
                installDefaultImage();
                return;
            }

            installImage(ImageUtils.loadImage(sourceUrl));

            changeListener.run();
        }

        // source can be a URL or a file path so handle either
        private URL urlFromSource(String source) {
            if (source == null)
                return null;

            try {
                return new URL(source);
            } catch (MalformedURLException e) {
                File file = new File(source);

                if (!file.exists())
                    throw new RuntimeException("Source string '" + source + "' is not a valid URL or file", e);

                try {
                    return file.toURI().toURL();
                } catch (MalformedURLException ex) {
                    throw new RuntimeException("Source string '" + source + "' is not a valid URL or file", e);
                }
            }
        }

        @Override
        public void installDefault() {
            installDefaultImage();

            changeListener.run();
        }

        @Override
        public String getSource() {
            return null;
        }

        @Override
        public BufferedImage getImage() {
            return image;
        }

        @Override
        public void setImage(String source, BufferedImage bufferedImage) {
            // TODO: don't think needs to be implemented. PortraitPanel/Control doesn't seem to interact with it
            throw new UnsupportedOperationException();
        }

        @Override
        public double getScale() {
            return portraitModel.getScale();
        }

        @Override
        public void setScale(double scale) {
            portraitModel.setScale(scale);

            changeListener.run();
        }

        @Override
        public double getPanX() {
            return portraitModel.getPanX();
        }

        @Override
        public void setPanX(double panX) {
            portraitModel.setPanX(panX);

            changeListener.run();
        }

        @Override
        public double getPanY() {
            return portraitModel.getPanY();
        }

        @Override
        public double getRotation() {
            return portraitModel.getRotation();
        }

        @Override
        public void setRotation(double angleInDegrees) {
            portraitModel.setRotation(angleInDegrees);

            changeListener.run();
        }

        @Override
        public void setPanY(double panY) {
            portraitModel.setPanY(panY);

            changeListener.run();
        }

        @Override
        public Dimension getClipDimensions() {
            return portraitDrawDimension;
        }


        // NOTE: moved persistent fields to the corresponding PortraitModel
        // NOTE: removed any references the owning GameComponent
        // NOTE: removed ability have a parent portrait
        // NOTE: removed ability have child portraits
        // NOTE: removed ability to configure the features. We always run all the features (pan, scale, rotation)

        @Override
        public final EnumSet<Feature> getFeatures() {
            return Portrait.ROTATABLE_PORTRAIT_FEATURES;
        }


        // NOTE: synthetic edge limit code removed that supported created a bleed margin/border automatically. don't think we need this

        // NOTE: scaleUsesMinimum logic removed that supported specifying whether by default the portrait should fill the clip region
        // and leave some of the source image outside the clip OR fit the entire image into the stencil leaving areas blank. we always want the former
        // so this was removed

        @Override
        public BufferedImage getClipStencil() {
    //        if (hasExplicitClip) {
    //            return explicitClip;
    //        }
    //
    //        if (gc == null || key == null || noClip) {
    //            return null;
    //        }
    //
    //        if (cachedClipSheetIndex < 0) {
    //            // flush the cache since we might need to use a different sheet
    //            cachedClip = null;
    //            // find the lowest-indexed sheet that the portrait is drawn on
    //            int bits = sheetBitMap;
    //            for (cachedClipSheetIndex = 0; cachedClipSheetIndex <= MAXIMUM_SHEET_INDEX; ++cachedClipSheetIndex) {
    //                if ((bits & 1) != 0) {
    //                    break;
    //                }
    //                bits >>= 1;
    //            }
    //        }
    //        // portrait doesn't appear on any sheet
    //        if (cachedClipSheetIndex == 0) {
    //            return null;
    //        }
    //
    //        // check if the sheet actually exists
    //        final Sheet[] sheets = gc.getSheets();
    //        if (sheets == null || cachedClipSheetIndex >= sheets.length) {
    //            return null;
    //        }
    //
    //        // check if the sheet's template image has changed
    //        final BufferedImage template = sheets[cachedClipSheetIndex].getTemplateImage();
    //        if (template != cachedClipTemplate) {
    //            cachedClipTemplate = template;
    //            cachedClip = null;
    //        }
    //
    //        // nothing can possibly show through since there is no alpha channel
    //        if (template.getTransparency() == BufferedImage.OPAQUE) {
    //            return null;
    //        }
    //
    //        if (cachedClip == null) {
    //            cachedClip = createStencil(template, gc.getSettings().getRegion(key + "-portrait-clip"));
    //        }
    //
    //        return cachedClip;

            // TODO: see if this needs implementing
            return null;
        }
    }
}
