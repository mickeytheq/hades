package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.CardFaceModel;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

@Model(typeCode = "Event", version = 1)
public class Event implements CardFaceModel, HasCommonCardFieldsModel, HasEncounterSetModel, HasCollectionModel, HasPortraitModel {
    private final CommonCardFieldsModel commonCardFieldsModel;
    private final CollectionModel collectionModel = new CollectionModel();
    private final EncounterSetModel encounterSetModel = new EncounterSetModel();
    private final PlayerCardFieldsModel playerCardFieldsModel;
    private final PortraitModel portraitModel;

    public Event() {
        playerCardFieldsModel = new PlayerCardFieldsModel();
        commonCardFieldsModel = new CommonCardFieldsModel();
        portraitModel = new PortraitModel();
    }

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        encounterSetModel.initialiseNew(projectContext, cardFaceSide);
        collectionModel.initialiseNew(projectContext, cardFaceSide);
        playerCardFieldsModel.initialiseNew(projectContext, cardFaceSide);
    }

    @Property(CardModelUtils.GENERAL)
    public CommonCardFieldsModel getCommonCardFieldsModel() {
        return commonCardFieldsModel;
    }

    @Property(CardModelUtils.COLLECTION)
    public CollectionModel getCollectionModel() {
        return collectionModel;
    }

    @Property(CardModelUtils.ENCOUNTER_SET)
    public EncounterSetModel getEncounterSetModel() {
        return encounterSetModel;
    }

    @Property(CardModelUtils.PLAYER)
    public PlayerCardFieldsModel getPlayerCardFieldsModel() {
        return playerCardFieldsModel;
    }

    @Property(CardModelUtils.ART_PORTRAIT)
    public PortraitModel getPortraitModel() {
        return portraitModel;
    }
}
