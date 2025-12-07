package com.mickeytheq.hades.core.project;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.image.BufferedImage;

public class EncounterSetInfo {
    @JsonProperty("Key")
    private String key;

    @JsonProperty("DisplayName")
    private String displayName;

    @JsonProperty("Image")
    private BufferedImage image;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    // TODO: maximum cards?

    // TODO: listener support for open cards to listen for changes to their encounter set
    // TODO: also consider how to handle additions and deletions
}
