package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.CardFaceModel;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public interface HasCommonCardFieldsModel {
    CommonCardFieldsModel getCommonCardFieldsModel();

    static Optional<CommonCardFieldsModel> getInstance(CardFaceModel cardFaceModel) {
        if (cardFaceModel instanceof HasCommonCardFieldsModel)
            return Optional.of(((HasCommonCardFieldsModel)cardFaceModel).getCommonCardFieldsModel());

        return Optional.empty();
    }

    // convenience method that returns the title for a CardFaceModel
    // if the title is not available or empty/null then an empty Optional is returned
    static Optional<String> getTitle(CardFaceModel cardFaceModel) {
        return getInstance(cardFaceModel).map(CommonCardFieldsModel::getTitle).filter(StringUtils::isNotEmpty);
    }
}
