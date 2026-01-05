package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.model.image.ImageProxy;
import com.mickeytheq.hades.serialise.EmptyEntityDiscriminator;
import org.apache.commons.lang3.StringUtils;

public class PortraitModel implements EmptyEntityDiscriminator {
    private double panX = 0;
    private double panY = 0;
    private double scale = 1.0;
    private double rotation = 0;
    private String artist;


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

    @Override
    public boolean isEmpty() {
        // if the image is set then this is not null
        if (!getImage().isEmpty())
            return false;

        if (!StringUtils.isEmpty(artist))
            return false;

        // otherwise any pan/scale settings are irrelevant and we can skip the entire entity
        return true;
    }
}
