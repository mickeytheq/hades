package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.CardFaceModel;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public interface HasLocationFieldsModel {
    LocationFieldsModel getLocationFieldsModel();

    static Optional<LocationFieldsModel> getInstance(CardFaceModel cardFaceModel) {
        if (cardFaceModel instanceof HasLocationFieldsModel)
            return Optional.of(((HasLocationFieldsModel)cardFaceModel).getLocationFieldsModel());

        return Optional.empty();
    }
}
