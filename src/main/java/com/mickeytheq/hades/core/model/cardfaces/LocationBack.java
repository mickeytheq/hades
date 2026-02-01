package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.BaseCardFaceModel;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

@Model(typeCode = "LocationBack")
public class LocationBack extends BaseCardFaceModel implements HasCommonCardFieldsModel {
    private final CommonCardFieldsModel commonCardFieldsModel;
    private final CollectionModel collectionModel = new CollectionModel();
    private final EncounterSetModel encounterSetModel = new EncounterSetModel();
    private final PortraitModel portraitModel;
    private final LocationFieldsModel locationFieldsModel;

    public LocationBack() {
        commonCardFieldsModel = new CommonCardFieldsModel();
        portraitModel = new PortraitModel();
        locationFieldsModel = new LocationFieldsModel();

        commonCardFieldsModel.setCopyOtherFaceTitles(true);
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
    public PortraitModel getPortraitModel() {
        return portraitModel;
    }

    @Property("Location")
    public LocationFieldsModel getLocationFieldsModel() {
        return locationFieldsModel;
    }
}
