package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.BaseCardFaceModel;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

@Model(typeCode = "LocationBack")
public class LocationBack extends BaseCardFaceModel {
    private final CommonCardFieldsModel commonCardFieldsModel;
    private final CollectionModel collectionModel = new CollectionModel();
    private final EncounterSetModel encounterSetModel = new EncounterSetModel();
    private final PortraitWithArtistModel portraitWithArtistModel;
    private final LocationFieldsModel locationFieldsModel;

    public LocationBack() {
        commonCardFieldsModel = new CommonCardFieldsModel();
        portraitWithArtistModel = new PortraitWithArtistModel();
        locationFieldsModel = new LocationFieldsModel();
    }

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        // by default copy the other side of the card
        locationFieldsModel.setCopyOtherFace(true);

        encounterSetModel.initialiseNew(projectContext, cardFaceSide);
        collectionModel.initialiseNew(projectContext, cardFaceSide);
    }

    @Property(flatten = true)
    public CommonCardFieldsModel getCommonCardFieldsModel() {
        return commonCardFieldsModel;
    }

    @Property("Collection")
    public CollectionModel getCollectionModel() {
        return collectionModel;
    }

    @Property("EncounterSet")
    public EncounterSetModel getEncounterSetModel() {
        return encounterSetModel;
    }

    @Property("ArtPortrait")
    public PortraitWithArtistModel getPortraitWithArtistModel() {
        return portraitWithArtistModel;
    }

    @Property("Location")
    public LocationFieldsModel getLocationFieldsModel() {
        return locationFieldsModel;
    }
}
