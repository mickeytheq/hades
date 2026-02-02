package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.BaseCardFaceModel;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

@Model(typeCode = "LocationBack")
public class LocationBack extends BaseCardFaceModel implements HasCommonCardFieldsModel {
    private final CommonCardFieldsModel commonCardFieldsModel = new CommonCardFieldsModel();
    private final PortraitModel portraitModel = new PortraitModel();
    private final LocationFieldsModel locationFieldsModel = new LocationFieldsModel();

    public LocationBack() {
        commonCardFieldsModel.setCopyOtherFaceTitles(true);
    }

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        // by default copy the other side of the card
        locationFieldsModel.setCopyOtherFace(true);
    }

    @Property(CardModelPropertyNames.GENERAL)
    public CommonCardFieldsModel getCommonCardFieldsModel() {
        return commonCardFieldsModel;
    }

    @Property(CardModelPropertyNames.ART_PORTRAIT)
    public PortraitModel getPortraitModel() {
        return portraitModel;
    }

    @Property("Location")
    public LocationFieldsModel getLocationFieldsModel() {
        return locationFieldsModel;
    }
}
