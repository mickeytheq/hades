package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.CardFaceModel;

import java.util.Optional;

public interface HasAgendaFieldsModel {
    AgendaFieldsModel getAgendaFieldsModel();

    static Optional<AgendaFieldsModel> getInstance(CardFaceModel cardFaceModel) {
        if (cardFaceModel instanceof HasAgendaFieldsModel)
            return Optional.of(((HasAgendaFieldsModel)cardFaceModel).getAgendaFieldsModel());

        return Optional.empty();
    }
}
