package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.CardFaceModel;

import java.util.Optional;

public interface HasCollectionModel {
    CollectionModel getCollectionModel();

    static Optional<CollectionModel> getInstance(CardFaceModel cardFaceModel) {
        if (cardFaceModel instanceof HasCollectionModel)
            return Optional.of(((HasCollectionModel)cardFaceModel).getCollectionModel());

        return Optional.empty();
    }
}
