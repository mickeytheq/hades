package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.BaseCardFaceModel;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

@Model(typeCode = "Act")
public class Act extends BaseCardFaceModel implements HasCommonCardFieldsModel {
    private final ActFieldsModel actFieldsModel = new ActFieldsModel();
    private final CommonCardFieldsModel commonCardFieldsModel = new CommonCardFieldsModel();
    private final StorySectionModel storySectionModel = new StorySectionModel();
    private final CollectionModel collectionModel = new CollectionModel();
    private final EncounterSetModel encounterSetModel = new EncounterSetModel();
    private final PortraitModel portraitModel = new PortraitModel();

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        getActFieldsModel().setNumber("1");
        getActFieldsModel().setDeckId("a");
        getActFieldsModel().setClues(new Statistic("3", false));

        encounterSetModel.initialiseNew(projectContext, cardFaceSide);
        collectionModel.initialiseNew(projectContext, cardFaceSide);
    }

    @Property(CardModelPropertyNames.ACT)
    public ActFieldsModel getActFieldsModel() {
        return actFieldsModel;
    }

    @Property(CardModelPropertyNames.GENERAL)
    public CommonCardFieldsModel getCommonCardFieldsModel() {
        return commonCardFieldsModel;
    }

    @Property(CardModelPropertyNames.STORY)
    public StorySectionModel getStorySectionModel() {
        return storySectionModel;
    }

    @Property(CardModelPropertyNames.COLLECTION)
    public CollectionModel getCollectionModel() {
        return collectionModel;
    }

    @Property(CardModelPropertyNames.ENCOUNTER_SET)
    public EncounterSetModel getEncounterSetModel() {
        return encounterSetModel;
    }

    @Property(CardModelPropertyNames.ART_PORTRAIT)
    public PortraitModel getPortraitModel() {
        return portraitModel;
    }

}
