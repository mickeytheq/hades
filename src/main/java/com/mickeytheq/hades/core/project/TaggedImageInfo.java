package com.mickeytheq.hades.core.project;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.image.BufferedImage;

public class TaggedImageInfo {
    @JsonProperty("Tag")
    private String tag;

    @JsonProperty("DisplayName")
    private String displayName;

    @JsonProperty("Image")
    private BufferedImage image;

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

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String toString() {
        return displayName;
    }
}
