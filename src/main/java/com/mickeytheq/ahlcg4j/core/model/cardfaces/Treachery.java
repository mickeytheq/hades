package com.mickeytheq.ahlcg4j.core.model.cardfaces;

import com.mickeytheq.ahlcg4j.core.model.BaseCardFaceModel;
import com.mickeytheq.ahlcg4j.core.model.common.CommonCardFieldsModel;
import com.mickeytheq.ahlcg4j.core.model.common.NumberingModel;
import com.mickeytheq.ahlcg4j.core.model.entity.Property;
import com.mickeytheq.ahlcg4j.core.model.Model;
import com.mickeytheq.ahlcg4j.core.model.common.WeaknessType;
import com.mickeytheq.ahlcg4j.codegenerated.InterfaceConstants;

@Model(typeCode = "Treachery")
public class Treachery extends BaseCardFaceModel {
    private WeaknessType weaknessType;

    private final CommonCardFieldsModel commonCardFieldsModel;

    private final NumberingModel numberingModel;

    public Treachery() {
        weaknessType = WeaknessType.None;

        commonCardFieldsModel = new CommonCardFieldsModel();
        numberingModel = new NumberingModel();
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
}
