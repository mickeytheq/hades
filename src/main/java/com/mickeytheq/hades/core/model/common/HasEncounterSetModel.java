package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.CardFaceModel;

import java.util.Optional;

public interface HasEncounterSetModel {
    EncounterSetModel getEncounterSetModel();

    static Optional<EncounterSetModel> getInstance(CardFaceModel cardFaceModel) {
        if (cardFaceModel instanceof HasEncounterSetModel)
            return Optional.of(((HasEncounterSetModel)cardFaceModel).getEncounterSetModel());

        return Optional.empty();
    }
}
