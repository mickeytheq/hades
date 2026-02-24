package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.CardFaceModel;

import java.util.Optional;

public interface HasPortraitModel {
    PortraitModel getPortraitModel();

    static Optional<PortraitModel> getInstance(CardFaceModel cardFaceModel) {
        if (cardFaceModel instanceof HasPortraitModel)
            return Optional.of(((HasPortraitModel)cardFaceModel).getPortraitModel());

        return Optional.empty();
    }
}
