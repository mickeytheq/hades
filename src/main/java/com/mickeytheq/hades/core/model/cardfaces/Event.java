package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.CardFaceModel;
import com.mickeytheq.hades.core.model.common.CommonCardFieldsModel;
import com.mickeytheq.hades.core.model.common.NumberingModel;
import com.mickeytheq.hades.core.model.common.PlayerCardFieldsModel;
import com.mickeytheq.hades.core.model.common.PortraitWithArtistModel;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

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

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        numberingModel.initialiseNew(projectContext, cardFaceSide);
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
