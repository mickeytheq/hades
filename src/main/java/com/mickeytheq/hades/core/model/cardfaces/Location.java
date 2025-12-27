package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.BaseCardFaceModel;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

@Model(typeCode = "Location")
public class Location extends BaseCardFaceModel {
    private CommonCardFieldsModel commonCardFieldsModel;
    private NumberingModel numberingModel;
    private PortraitWithArtistModel portraitWithArtistModel;
    private LocationFieldsModel locationFieldsModel;

    public Location() {
        commonCardFieldsModel = new CommonCardFieldsModel();
        numberingModel = new NumberingModel();
        portraitWithArtistModel = new PortraitWithArtistModel();
        locationFieldsModel = new LocationFieldsModel();
    }

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {

    }

    @Property(flatten = true)
    public CommonCardFieldsModel getCommonCardFieldsModel() {
        return commonCardFieldsModel;
    }

    public void setCommonCardFieldsModel(CommonCardFieldsModel commonCardFieldsModel) {
        this.commonCardFieldsModel = commonCardFieldsModel;
    }

    @Property(flatten = true)
    public NumberingModel getNumberingModel() {
        return numberingModel;
    }

    public void setNumberingModel(NumberingModel numberingModel) {
        this.numberingModel = numberingModel;
    }

    @Property("ArtPortrait")
    public PortraitWithArtistModel getPortraitWithArtistModel() {
        return portraitWithArtistModel;
    }

    public void setPortraitWithArtistModel(PortraitWithArtistModel portraitWithArtistModel) {
        this.portraitWithArtistModel = portraitWithArtistModel;
    }

    @Property("Location")
    public LocationFieldsModel getLocationFieldsModel() {
        return locationFieldsModel;
    }

    public void setLocationFieldsModel(LocationFieldsModel locationFieldsModel) {
        this.locationFieldsModel = locationFieldsModel;
    }
}
