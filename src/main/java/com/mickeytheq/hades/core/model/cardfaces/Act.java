package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.BaseCardFaceModel;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

@Model(typeCode = "Act")
public class Act extends BaseCardFaceModel implements HasCommonCardFieldsModel {
    private String actNumber;
    private String deckId;
    private Statistic clues = Statistic.empty();

    private final CommonCardFieldsModel commonCardFieldsModel = new CommonCardFieldsModel();
    private final StorySectionModel storySectionModel = new StorySectionModel();
    private final CollectionModel collectionModel = new CollectionModel();
    private final EncounterSetModel encounterSetModel = new EncounterSetModel();
    private final PortraitModel portraitModel = new PortraitModel();

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        actNumber = "1";
        deckId = "a";
        clues = new Statistic("3", false);

        encounterSetModel.initialiseNew(projectContext, cardFaceSide);
        collectionModel.initialiseNew(projectContext, cardFaceSide);
    }

    @Property("ActNumber")
    public String getActNumber() {
        return actNumber;
    }

    public void setActNumber(String actNumber) {
        this.actNumber = actNumber;
    }

    @Property("DeckId")
    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }

    @Property("Clues")
    public Statistic getClues() {
        return clues;
    }

    public void setClues(Statistic clues) {
        this.clues = clues;
    }

    @Property(flatten = true)
    public CommonCardFieldsModel getCommonCardFieldsModel() {
        return commonCardFieldsModel;
    }

    @Property(flatten = true)
    public StorySectionModel getStorySectionModel() {
        return storySectionModel;
    }

    @Property("Collection")
    public CollectionModel getCollectionModel() {
        return collectionModel;
    }

    @Property("EncounterSet")
    public EncounterSetModel getEncounterSetModel() {
        return encounterSetModel;
    }

    @Property("ArtPortrait")
    public PortraitModel getPortraitModel() {
        return portraitModel;
    }
}
