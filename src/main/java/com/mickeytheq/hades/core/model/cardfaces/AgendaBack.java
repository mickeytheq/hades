package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.BaseCardFaceModel;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

@Model(typeCode = "AgendaBack", version = 1)
public class AgendaBack extends BaseCardFaceModel implements HasCommonCardFieldsModel, HasEncounterSetModel, HasAgendaFieldsModel {
    private final AgendaFieldsModel agendaFieldsModel = new AgendaFieldsModel();
    private final CommonCardFieldsModel commonCardFieldsModel = new CommonCardFieldsModel();
    private final StorySectionModel section1 = new StorySectionModel();
    private final StorySectionModel section2 = new StorySectionModel();
    private final StorySectionModel section3 = new StorySectionModel();
    private final EncounterSetModel encounterSetModel = new EncounterSetModel();

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        // this face will almost always be on the back but just in case something is making a weird
        // card combo we only set the shadowing if it is a 'Back'
        agendaFieldsModel.setCopyOtherFace(cardFaceSide == CardFaceSide.Back);

        encounterSetModel.initialiseNew(projectContext, cardFaceSide);
    }

    @Property(CardModelUtils.AGENDA)
    public AgendaFieldsModel getAgendaFieldsModel() {
        return agendaFieldsModel;
    }

    @Property(CardModelUtils.GENERAL)
    public CommonCardFieldsModel getCommonCardFieldsModel() {
        return commonCardFieldsModel;
    }

    @Property(CardModelUtils.STORY_SECTION_1)
    public StorySectionModel getSection1() {
        return section1;
    }

    @Property(CardModelUtils.STORY_SECTION_2)
    public StorySectionModel getSection2() {
        return section2;
    }

    @Property(CardModelUtils.STORY_SECTION_3)
    public StorySectionModel getSection3() {
        return section3;
    }

    @Property(CardModelUtils.ENCOUNTER_SET)
    public EncounterSetModel getEncounterSetModel() {
        return encounterSetModel;
    }
}
