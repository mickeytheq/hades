package com.mickeytheq.ahlcg4j.core.view.common;

import ca.cgjennings.apps.arkham.component.AbstractPortrait;
import ca.cgjennings.apps.arkham.component.Portrait;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import ca.cgjennings.graphics.ImageUtilities;
import ca.cgjennings.graphics.filters.InversionFilter;
import com.mickeytheq.ahlcg4j.core.model.common.PortraitModel;
import com.mickeytheq.ahlcg4j.core.view.PaintContext;
import com.mickeytheq.ahlcg4j.core.view.utils.ImageUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class PortraitView extends AbstractPortrait {
    public static final URL BLANK_PORTRAIT_IMAGE_RESOURCE = PortraitModel.class.getResource("/resources/spacers/empty1x1.png");
    public static final URL DEFAULT_PORTRAIT_IMAGE_RESOURCE = PortraitModel.class.getResource("/portrait/DefaultTexture.jp2");
    private final PortraitModel portraitModel;
    private final Rectangle portraitDrawRegion;
    private final Runnable changeListener;

    private final URL defaultImageResource;

    private BufferedImage image;

    // private to ensure that a defaultImageResource is always provided, even if just an empty image
    // the default is used whenever the model's source is null
    private PortraitView(PortraitModel portraitModel, Rectangle portraitDrawRegion, Runnable changeListener, URL defaultImageResource) {
        this.portraitModel = portraitModel;
        this.portraitDrawRegion = portraitDrawRegion;
        this.changeListener = changeListener;
        this.defaultImageResource = defaultImageResource;

        if (portraitModel.getSource() != null)
            image = ImageUtils.loadImage(portraitModel.getSource());
        else
            installDefault();
    }

    // create a new PortraitView with the given model and draw region which defaults to a blank image when
    // no source is provided by the model
    public static PortraitView createWithBlankImage(PortraitModel portraitModel, Rectangle portraitDrawRegion, Runnable changeListener) {
        return new PortraitView(portraitModel, portraitDrawRegion, changeListener, BLANK_PORTRAIT_IMAGE_RESOURCE);
    }

    // create a new PortraitView with the given model and draw region which defaults to a standard/default image when
    // no source is provided by the model
    public static PortraitView createWithDefaultImage(PortraitModel portraitModel, Rectangle portraitDrawRegion, Runnable changeListener) {
        return new PortraitView(portraitModel, portraitDrawRegion, changeListener, DEFAULT_PORTRAIT_IMAGE_RESOURCE);
    }

    public Rectangle getPortraitDrawRegion() {
        return portraitDrawRegion;
    }

    @Override
    public void setSource(String newSource) {
        URL sourceUrl = urlFromSource(newSource);

        // nothing changed - skip any update
        if (Objects.equals(portraitModel.getSource(), sourceUrl))
            return;

        // if the source is null then reset the image to the default
        if (sourceUrl == null) {
            installDefault();
            return;
        }

        // on loading a new image set all the pan/scale/rotation values to appropriate defaults for that image
        setImageAndCalculateDefaults(ImageUtils.loadImage(sourceUrl));

        portraitModel.setSource(sourceUrl);

        changeListener.run();
    }

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
        // install the default image and set pan/scale/rotation to sensible defaults
        setImageAndCalculateDefaults(ImageUtils.loadImage(defaultImageResource));

        changeListener.run();
    }

    private void setImageAndCalculateDefaults(BufferedImage image) {
        this.image = image;

        portraitModel.setPanX(0);
        portraitModel.setPanY(0);
        portraitModel.setScale(computeDefaultImageScale(image));
        portraitModel.setRotation(0);
    }

    @Override
    public String getSource() {
        URL sourceURl = portraitModel.getSource();

        if (sourceURl == null)
            return null;

        return sourceURl.toExternalForm();
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
        return new Dimension(portraitDrawRegion.getSize());
    }

    private boolean fillBackground = true;

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

    /**
     * Returns {@code true} if the portrait will be clipped to the clip region.
     */
    public final boolean getClipping() {
        return true;
    }

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

    /**
     * Sets whether the portrait clip region will be filled with solid white
     * before painting the portrait. If set, the portrait clip region will be
     * filled in with solid white before painting the portrait. This is usually
     * turned off when the user is expected to use portraits that have
     * transparency because the portrait is painted over a background
     * illustration.
     *
     * <p>
     * <b>Note:</b> This is normally called at most once, just after the
     * portrait is first created.
     *
     * @param fillBackground if {@code true}, the portrait background will be filled in
     * when it is drawn with {@link #paint}.
     */
    public final void setBackgroundFilled(boolean fillBackground) {
        if (this.fillBackground != fillBackground) {
            this.fillBackground = fillBackground;

            changeListener.run();
        }
    }

    /**
     * Returns {@code true} if portrait areas will be filled with solid white
     * before painting the portrait. See {@link #setBackgroundFilled(boolean)
     * } for details.
     *
     * @return {@code true} if the portrait clip region is filled before drawing
     * the portrait
     */
    public final boolean isBackgroundFilled() {
        return fillBackground;
    }

    public void paint(com.mickeytheq.ahlcg4j.core.view.PaintContext paintContext) {
        paint(paintContext, false);
    }

    public void paint(PaintContext paintContext, boolean paintInverted) {
        Graphics2D g = paintContext.getGraphics();

        BufferedImage image = getImage();
        if (image == null) {
            return;
        }

        Rectangle r = getPortraitDrawRegion();

        Shape oldClip = null;
        final boolean obeyClip = getClipping();
        if (obeyClip) {
            oldClip = g.getClip();
            g.setClip(r);
        }

        if (isBackgroundFilled()) {
            final Paint p = g.getPaint();
            g.setPaint(Color.WHITE);
            g.fillRect(r.x, r.y, r.width, r.height);
            g.setPaint(p);
        }

        // sometimes we want the portrait inverted, for example when painting an icon provided with a black foreground
        // onto a black background. in this case the inversion will convert black to white
        if (paintInverted) {
            BufferedImageOp inversionOp = new InversionFilter();
            image = inversionOp.filter(image, null);
        }

        double rotation = getRotation();
        double panX = getPanX();
        double panY = getPanY();
        double scale = getScale();

        final double scaledWidth = image.getWidth() * scale;
        final double scaledHeight = image.getHeight() * scale;

        final double centerX = scaledWidth / 2d;
        final double centerY = scaledHeight / 2d;

        // note that the pan value is relative to the center of the clip region;
        // i.e. a pan of (0,0) always centers the image
        final double regionX = r.getCenterX();
        final double regionY = r.getCenterY();

        if (rotation != 0 && getFeatures().contains(Feature.ROTATE)) {
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

        Sheet.drawPortraitBox(g, r, this);
    }

    /**
     * Converts angle measures and direction to match the portrait panel specs.
     */
    private static final double DEGREES_TO_RADIANS = -0.0174532925d;

    private double computeDefaultImageScale(BufferedImage image) {
        Rectangle clip = getPortraitDrawRegion();
        double idealWidth = clip.getWidth();
        double idealHeight = clip.getHeight();
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();

        return ImageUtilities.idealCoveringScaleForImage(idealWidth, idealHeight, imageWidth, imageHeight);
    }
}
