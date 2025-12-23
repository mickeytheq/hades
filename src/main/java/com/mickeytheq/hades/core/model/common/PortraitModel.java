package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.model.image.ImageProxy;

import java.awt.image.BufferedImage;
import java.net.URL;

public class PortraitModel {
    private double panX = 0;
    private double panY = 0;
    private double scale = 1.0;
    private double rotation = 0;

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
}
