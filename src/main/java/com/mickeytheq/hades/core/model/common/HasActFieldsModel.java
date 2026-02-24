package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.CardFaceModel;

import java.util.Optional;

public interface HasActFieldsModel {
    ActFieldsModel getActFieldsModel();

    static Optional<ActFieldsModel> getInstance(CardFaceModel cardFaceModel) {
        if (cardFaceModel instanceof HasActFieldsModel)
            return Optional.of(((HasActFieldsModel)cardFaceModel).getActFieldsModel());

        return Optional.empty();
    }
}
