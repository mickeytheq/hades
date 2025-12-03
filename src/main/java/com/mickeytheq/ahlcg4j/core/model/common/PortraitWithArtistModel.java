package com.mickeytheq.ahlcg4j.core.model.common;

import com.mickeytheq.ahlcg4j.core.model.entity.Property;

public class PortraitWithArtistModel {
    private PortraitModel portraitModel = new PortraitModel();
    private String artist;

    @Property("Portrait")
    public PortraitModel getPortraitModel() {
        return portraitModel;
    }

    public void setPortraitModel(PortraitModel portraitModel) {
        this.portraitModel = portraitModel;
    }

    @Property("Artist")
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
