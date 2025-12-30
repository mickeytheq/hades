package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.BaseCardFaceModel;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

@Model(typeCode = "Agenda")
public class Agenda extends BaseCardFaceModel {
    private String agendaNumber;
    private String deckId;
    private Statistic doom;

    private CommonCardFieldsModel commonCardFieldsModel = new CommonCardFieldsModel();
    private ActAgendaCommonFieldsModel agendaCommonFieldsModel = new ActAgendaCommonFieldsModel();
    private NumberingModel numberingModel = new NumberingModel();
    private PortraitWithArtistModel portraitWithArtistModel = new PortraitWithArtistModel();

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        agendaNumber = "1";
        deckId = "a";
        doom = new Statistic("3", false);
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
    public ActAgendaCommonFieldsModel getAgendaCommonFieldsModel() {
        return agendaCommonFieldsModel;
    }

    @Property(flatten = true)
    public NumberingModel getNumberingModel() {
        return numberingModel;
    }

    @Property("ArtPortrait")
    public PortraitWithArtistModel getPortraitWithArtistModel() {
        return portraitWithArtistModel;
    }
}
