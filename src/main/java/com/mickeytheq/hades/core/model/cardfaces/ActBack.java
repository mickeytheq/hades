package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.BaseCardFaceModel;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.common.ActAgendaCommonFieldsModel;
import com.mickeytheq.hades.core.model.common.CommonCardFieldsModel;
import com.mickeytheq.hades.core.model.common.EncounterSetModel;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

@Model(typeCode = "ActBack")
public class ActBack extends BaseCardFaceModel {
    private boolean shadowFront;
    private String actNumber;
    private String deckId;

    private final CommonCardFieldsModel commonCardFieldsModel = new CommonCardFieldsModel();
    private final ActAgendaCommonFieldsModel section1 = new ActAgendaCommonFieldsModel();
    private final ActAgendaCommonFieldsModel section2 = new ActAgendaCommonFieldsModel();
    private final ActAgendaCommonFieldsModel section3 = new ActAgendaCommonFieldsModel();
    private final EncounterSetModel encounterSetModel = new EncounterSetModel();

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        // this face will almost always be on the back but just in case something is making a weird
        // card combo we only set the shadowing if it is a 'Back'
        shadowFront = cardFaceSide == CardFaceSide.Back;

        encounterSetModel.initialiseNew(projectContext, cardFaceSide);
    }

    @Property("ShadowFront")
    public boolean isShadowFront() {
        return shadowFront;
    }

    public void setShadowFront(boolean shadowFront) {
        this.shadowFront = shadowFront;
    }

    public String getActNumber() {
        return actNumber;
    }

    public void setActNumber(String actNumber) {
        this.actNumber = actNumber;
    }

    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }

    @Property(flatten = true)
    public CommonCardFieldsModel getCommonCardFieldsModel() {
        return commonCardFieldsModel;
    }

    @Property("Section1")
    public ActAgendaCommonFieldsModel getSection1() {
        return section1;
    }

    @Property("Section2")
    public ActAgendaCommonFieldsModel getSection2() {
        return section2;
    }

    @Property("Section3")
    public ActAgendaCommonFieldsModel getSection3() {
        return section3;
    }

    @Property("EncounterSet")
    public EncounterSetModel getEncounterSetModel() {
        return encounterSetModel;
    }
}
