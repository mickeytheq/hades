package com.mickeytheq.hades.core.model.common;

import ca.cgjennings.graphics.ImageUtilities;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.model.image.ImageProxy;
import com.mickeytheq.hades.serialise.discriminator.BooleanEmptyWhenFalseDiscriminator;
import com.mickeytheq.hades.serialise.discriminator.EmptyEntityDiscriminator;
import com.mickeytheq.hades.util.shape.DimensionEx;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PortraitModel implements EmptyEntityDiscriminator {
    // the PPI used as noted above to define the image draw region for the purposes of editing/positioning the art image
    // note that this value CANNOT be changed unless migration occurs to adjust the existing portrait values to match
    public static final int ASSUMED_PERSISTENCE_PPI = 600;
    private double panX = 0;
    private double panY = 0;
    private double scale = 1.0;
    private double rotation = 0;
    private String artist;
    private boolean copyOtherFace = false;


    private ImageProxy image = ImageProxy.createEmpty();

    @Property("PanX")
    public double getPanX() {
        return panX;
    }

    public void setPanX(double panX) {
        this.panX = panX;
    }

    @Property("PanY")
    public double getPanY() {
        return panY;
    }

    public void setPanY(double panY) {
        this.panY = panY;
    }

    @Property("Scale")
    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    @Property("Rotation")
    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    @Property("Image")
    public ImageProxy getImage() {
        return image;
    }

    public void setImage(ImageProxy image) {
        this.image = image;
    }

    @Property("Artist")
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Property(value = CardModelUtils.COPY_OTHER_FACE, discriminator = BooleanEmptyWhenFalseDiscriminator.class)
    public boolean isCopyOtherFace() {
        return copyOtherFace;
    }

    public void setCopyOtherFace(boolean copyOtherFace) {
        this.copyOtherFace = copyOtherFace;
    }

    @Override
    public boolean isEmpty() {
        // if the image is set then this is not null
        if (!getImage().isEmpty())
            return false;

        if (!StringUtils.isEmpty(artist))
            return false;

        if (copyOtherFace)
            return false;

        // otherwise any pan/scale settings are irrelevant and we can skip the entire entity
        return true;
    }

    // computes a scale to apply so that the image completely fills the draw area
    public static double computeDefaultImageScale(DimensionEx drawDimension, BufferedImage image) {
        Dimension pixelDimension = drawDimension.toPixelDimension(ASSUMED_PERSISTENCE_PPI);

        return computeDefaultImageScale(pixelDimension, image);
    }

    public static double computeDefaultImageScale(Dimension pixelDimension, BufferedImage image) {
        double idealWidth = pixelDimension.getWidth();
        double idealHeight = pixelDimension.getHeight();
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();

        return ImageUtilities.idealCoveringScaleForImage(idealWidth, idealHeight, imageWidth, imageHeight);
    }

    // helper for testing that primes this portrait model with an image, automatically scaling it to a useful default size
    public void prime(DimensionEx drawDimension, BufferedImage image) {
        double scale = computeDefaultImageScale(drawDimension, image);
        setScale(scale);
        setImage(ImageProxy.createPrimed(image));
    }
}
