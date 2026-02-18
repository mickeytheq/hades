package com.mickeytheq.hades.core.view.common;

import ca.cgjennings.apps.arkham.PortraitPanel;
import ca.cgjennings.apps.arkham.component.AbstractPortrait;
import ca.cgjennings.apps.arkham.component.Portrait;
import ca.cgjennings.graphics.ImageUtilities;
import ca.cgjennings.graphics.filters.InversionFilter;
import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.hades.codegenerated.GameConstants;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.common.PortraitModel;
import com.mickeytheq.hades.core.view.CardFaceView;
import com.mickeytheq.hades.core.view.EditorContext;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.utils.EditorUtils;
import com.mickeytheq.hades.core.view.utils.ImageUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.core.view.utils.TextStyleUtils;
import com.mickeytheq.hades.util.shape.RectangleEx;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import resources.Language;

import javax.swing.*;
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
//
// one important element is that portraits are used in two ways
// 1 - editing which is positioning an image within the available draw space
// 2 - painting the portrait
// there is a challenge with (1) because this requires positioning an element defined in pixels (the art image) in a space that
// is not defined in pixels (the draw region on the card). these two are only consistent for a given resolution PPI so that the
// latter can be translated to a pixel value.
// because there can be multiple templates used with different resolutions,e.g. 300 or 600 PPI if portraits are configured,
// this means that a portrait will only paint (2) correctly sized and positioned if the resolution used to position it is the same
// as the one painting it.
// to solve this we adopt a 'default' PPI which is used for (1) above and set the pan/scale values accordingly. when painting
// we scale those values to the painting PPI. for example if the default PPI was 300 and the painting PPI was 600 the pan/scale values
// would be doubled
public class PortraitView {
    private static final Logger logger = LogManager.getLogger(PortraitView.class);

    // the PPI used as noted above to define the image draw region for the purposes of editing/positioning the art image
    // note that this value CANNOT be changed unless migration occurs to adjust the existing portrait values to match
    public static final int ASSUMED_PERSISTENCE_PPI = 600;

    public static final URL BLANK_PORTRAIT_IMAGE_RESOURCE = PortraitModel.class.getResource("/resources/spacers/empty1x1.png");
    public static final URL DEFAULT_PORTRAIT_IMAGE_RESOURCE = PortraitModel.class.getResource("/portrait/DefaultTexture.jp2");
    private final PortraitModel portraitModel;
    private final CardFaceView cardFaceView;
    private final Dimension portraitDrawDimension;

    private final URL defaultImageResource;

    private JCheckBox copyOtherFaceEditor;
    private JTextField artistEditor;

    // the local transient image - ideally we would always rely on the model's image but when using a default
    // image we don't want to put that in the model so a local copy is maintained here
    private BufferedImage image;

    // private to ensure that a defaultImageResource is always provided, even if just an empty image
    // the default is used whenever the model's image is null
    //
    // we have to know the Dimension of the draw area at this stage as it is used by the portrait editor control
    // to show an overlay of what part of image will be visible or not when adjusting the pan/scale settings. it also
    // allows automatic scaling of a new image to an appropriate size
    //
    // the exact draw region/rectangle is only needed when painting
    private PortraitView(PortraitModel portraitModel, CardFaceView cardFaceView, Dimension portraitDrawDimension, URL defaultImageResource) {
        this.portraitModel = portraitModel;
        this.cardFaceView = cardFaceView;
        this.portraitDrawDimension = portraitDrawDimension;
        this.defaultImageResource = defaultImageResource;

        if (!portraitModel.getImage().isEmpty()) {
            image = portraitModel.getImage().get();
        }
        else {
            installDefaultImage();
        }
    }

    // create a new PortraitView with the given model and draw dimension which defaults to a blank image when
    // no source is provided by the model
    public static PortraitView createWithBlankImage(PortraitModel portraitModel, CardFaceView cardFaceView, RectangleEx portraitDrawRegion) {
        return new PortraitView(portraitModel, cardFaceView, portraitDrawRegion.toPixelDimension(ASSUMED_PERSISTENCE_PPI), BLANK_PORTRAIT_IMAGE_RESOURCE);
    }

    // create a new PortraitView with the given model and draw dimension which defaults to a standard/default image when
    // no source is provided by the model
    public static PortraitView createWithDefaultImage(PortraitModel portraitModel, CardFaceView cardFaceView, RectangleEx portraitDrawRegion) {
        return new PortraitView(portraitModel, cardFaceView, portraitDrawRegion.toPixelDimension(ASSUMED_PERSISTENCE_PPI), DEFAULT_PORTRAIT_IMAGE_RESOURCE);
    }

    private void installDefaultImage() {
        // install the default image
        // note that we do not set the model's image to this when installing a default as default images are not persisted
        // instead we set the model image to null
        image = ImageUtils.loadImageReadOnly(defaultImageResource);
        portraitModel.getImage().set(null);
        calculateImageDefaults();
    }

    private void installImage(BufferedImage image) {
        // set a user-specified image by updating the model and the local image view
        portraitModel.getImage().set(image);
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

    public BufferedImage getImage() {
        return image;
    }

    public void createEditors(EditorContext editorContext) {
        artistEditor = EditorUtils.createTextField(30);
        EditorUtils.bindTextComponent(artistEditor, editorContext.wrapConsumerWithMarkedChanged(portraitModel::setArtist));
        artistEditor.setText(portraitModel.getArtist());

        copyOtherFaceEditor = EditorUtils.createCheckBox();
        EditorUtils.bindToggleButton(copyOtherFaceEditor, editorContext.wrapConsumerWithMarkedChanged(portraitModel::setCopyOtherFace));
        copyOtherFaceEditor.setSelected(portraitModel.isCopyOtherFace());
    }

    public JPanel createStandardArtPanel(EditorContext editorContext) {
        return createStandardArtPanel(editorContext, false);
    }

    public JPanel createStandardArtPanel(EditorContext editorContext, boolean copyOtherFaceOption) {
        // have a zero inset as to avoid a double-spaced margin as this panel is usually embedded within other panels
        JPanel artistWithPortraitPanel = MigLayoutUtils.createOrganiserPanel();

        JPanel artistPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.PORTRAIT_OPTIONS));
        if (copyOtherFaceOption) {
            MigLayoutUtils.addLabelledComponentWrapGrowPush(artistPanel, Language.string(InterfaceConstants.COPY_OTHER_FACE), copyOtherFaceEditor);
        }

        MigLayoutUtils.addLabelledComponentWrapGrowPush(artistPanel, Language.string(InterfaceConstants.ARTIST), artistEditor);
        artistWithPortraitPanel.add(artistPanel, "wrap, pushx, growx");

        PortraitPanel portraitPanel = createPortraitPanel(editorContext, Language.string(InterfaceConstants.PORTRAIT));

        artistWithPortraitPanel.add(portraitPanel, "wrap, pushx, growx");

        return artistWithPortraitPanel;

    }

    private static final RectangleEx ARTIST_DRAW_REGION = RectangleEx.millimetres(2.37, 86.70, 20.49, PaintConstants.FOOTER_TEXT_HEIGHT_MMS);

    public void paintArtPortrait(PaintContext paintContext, Rectangle drawRegion) {
        paint(paintContext, drawRegion, false);
    }

    public void paintArtist(PaintContext paintContext) {
        paintArtist(paintContext, paintContext.toPixelRect(ARTIST_DRAW_REGION));
    }

    public void paintArtist(PaintContext paintContext, Rectangle drawRegion) {
        if (StringUtils.isEmpty(portraitModel.getArtist()))
            return;

        String artistText = portraitModel.getArtist();

        // add the standard illustration prefix if missing
        artistText = Strings.CS.prependIfMissing(artistText, Language.gstring(GameConstants.ILLUSTRATORSHORT) + " ");

        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getArtistTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_LEFT);
        markupRenderer.setMarkupText(artistText);
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), drawRegion);
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
        StopWatch stopWatch = StopWatch.createStarted();
        doPaint(paintContext, drawRegion, inverted);

        if (logger.isTraceEnabled())
            logger.trace("Painting of art portrait completed in " + stopWatch.getTime() + "ms");
    }

    private void doPaint(PaintContext paintContext, Rectangle drawRegion, boolean inverted) {
        Graphics2D g = paintContext.getGraphics();

        // if copying the other face delegate to its paint function
        if (portraitModel.isCopyOtherFace()) {
            Optional<PortraitView> otherFacePortraitView = cardFaceView.getOtherFaceView()
                    .filter(o -> o instanceof HasArtPortraitView).map(o -> (((HasArtPortraitView)o).getArtPortraitView()));

            otherFacePortraitView.ifPresent(portraitView -> portraitView.paint(paintContext, drawRegion, inverted));

            return;
        }

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

        // perform the scaling as noted above by apply a scaling factor to account for the default vs painting PPI
        double resolutionScalingFactor = (double)paintContext.getResolutionInPixelsPerInch() / ASSUMED_PERSISTENCE_PPI;
        panX = panX * resolutionScalingFactor;
        panY = panY * resolutionScalingFactor;
        scale = scale * resolutionScalingFactor;

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

            // if the source is null then reset the image to the default and clear the model's image
            if (sourceUrl == null) {
                installDefaultImage();
                return;
            }

            installImage(ImageUtils.loadImageReadOnly(sourceUrl));

            changeListener.run();
        }

        // source can be a URL or a file path so handle either
        private URL urlFromSource(String source) {
            if (StringUtils.isEmpty(source))
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
