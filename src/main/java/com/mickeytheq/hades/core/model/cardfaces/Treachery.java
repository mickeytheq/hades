package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.BaseCardFaceModel;
import com.mickeytheq.hades.core.model.common.CommonCardFieldsModel;
import com.mickeytheq.hades.core.model.common.NumberingModel;
import com.mickeytheq.hades.core.model.common.PortraitWithArtistModel;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.common.WeaknessType;

@Model(typeCode = "Treachery")
public class Treachery extends BaseCardFaceModel {
    private WeaknessType weaknessType;

    private final CommonCardFieldsModel commonCardFieldsModel;
    private final NumberingModel numberingModel;
    private final PortraitWithArtistModel portraitWithArtistModel;

    public Treachery() {
        weaknessType = WeaknessType.None;

        commonCardFieldsModel = new CommonCardFieldsModel();
        numberingModel = new NumberingModel();
        portraitWithArtistModel = new PortraitWithArtistModel();
    }

    @Property("WeaknessType")
    public WeaknessType getWeaknessType() {
        return weaknessType;
    }

    public void setWeaknessType(WeaknessType weaknessType) {
        this.weaknessType = weaknessType;
    }

    @Property(flatten = true)
    public CommonCardFieldsModel getCommonCardFieldsModel() {
        return commonCardFieldsModel;
    }

    @Property(flatten = true)
    public NumberingModel getNumberingModel() {
        return numberingModel;
    }

    @Property("ArtPortrait")
    public PortraitWithArtistModel getPortraitWithArtistModel() {
        return portraitWithArtistModel;
    }
}
