package com.mickeytheq.ahlcg4j.core.model.cardfaces;

import ca.cgjennings.layout.PageShape;
import com.mickeytheq.ahlcg4j.core.model.Model;
import com.mickeytheq.ahlcg4j.core.model.CardFaceModel;
import com.mickeytheq.ahlcg4j.core.model.common.CommonCardFieldsModel;
import com.mickeytheq.ahlcg4j.core.model.common.NumberingModel;
import com.mickeytheq.ahlcg4j.core.model.common.PlayerCardFieldsModel;
import com.mickeytheq.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.ahlcg4j.core.model.common.PortraitWithArtistModel;
import com.mickeytheq.ahlcg4j.core.model.entity.Property;
import org.apache.commons.lang3.concurrent.Memoizer;

import java.awt.geom.Path2D;
import java.util.function.Function;

@Model(typeCode = "Event")
public class Event implements CardFaceModel {
    private final CommonCardFieldsModel commonCardFieldsModel;
    private final NumberingModel numberingModel;
    private final PlayerCardFieldsModel playerCardFieldsModel;
    private final PortraitWithArtistModel portraitWithArtistModel;

    public Event() {
        playerCardFieldsModel = new PlayerCardFieldsModel();
        commonCardFieldsModel = new CommonCardFieldsModel();
        numberingModel = new NumberingModel();
        portraitWithArtistModel = new PortraitWithArtistModel();
    }

    @Property(flatten = true)
    public CommonCardFieldsModel getCommonCardFieldsModel() {
        return commonCardFieldsModel;
    }

    @Property(flatten = true)
    public NumberingModel getNumberingModel() {
        return numberingModel;
    }

    @Property(flatten = true)
    public PlayerCardFieldsModel getPlayerCardFieldsModel() {
        return playerCardFieldsModel;
    }

    @Property("ArtPortrait")
    public PortraitWithArtistModel getPortraitWithArtistModel() {
        return portraitWithArtistModel;
    }
}
