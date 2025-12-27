package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.serialise.NullDiscriminator;
import org.apache.commons.lang3.StringUtils;

public class PortraitWithArtistModel implements NullDiscriminator {
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

    @Override
    public boolean isNull() {
        if (!getPortraitModel().isNull())
            return false;

        if (!StringUtils.isEmpty(artist))
            return false;

        return true;
    }
}
