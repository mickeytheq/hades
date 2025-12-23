package com.mickeytheq.hades.core.project.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mickeytheq.hades.core.model.image.ImageProxy;

import java.awt.image.BufferedImage;

public class TaggedImageInfo {
    @JsonProperty("Tag")
    private String tag;

    @JsonProperty("DisplayName")
    private String displayName;

    @JsonProperty("Image")
    private ImageProxy image = ImageProxy.createEmpty();

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ImageProxy getImage() {
        return image;
    }

    public void setImage(ImageProxy image) {
        this.image = image;
    }

    public String toString() {
        return displayName;
    }
}
