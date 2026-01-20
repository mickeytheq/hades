package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.BaseCardFaceModel;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

@Model(typeCode = "Agenda")
public class Agenda extends BaseCardFaceModel implements HasCommonCardFieldsModel {
    private String agendaNumber;
    private String deckId;
    private Statistic doom = Statistic.empty();

    private final CommonCardFieldsModel commonCardFieldsModel = new CommonCardFieldsModel();
    private final StorySectionModel storySectionModel = new StorySectionModel();
    private final CollectionModel collectionModel = new CollectionModel();
    private final EncounterSetModel encounterSetModel = new EncounterSetModel();
    private final PortraitModel portraitModel = new PortraitModel();

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        agendaNumber = "1";
        deckId = "a";
        doom = new Statistic("3", false);

        encounterSetModel.initialiseNew(projectContext, cardFaceSide);
        collectionModel.initialiseNew(projectContext, cardFaceSide);
    }

    @Property("AgendaNumber")
    public String getAgendaNumber() {
        return agendaNumber;
    }

    public void setAgendaNumber(String agendaNumber) {
        this.agendaNumber = agendaNumber;
    }

    @Property("DeckId")
    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }

    @Property("Doom")
    public Statistic getDoom() {
        return doom;
    }

    public void setDoom(Statistic doom) {
        this.doom = doom;
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
