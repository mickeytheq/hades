package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.BaseCardFaceModel;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

@Model(typeCode = "EncounterCardBack", version = 1)
public class EncounterCardBack extends BaseCardFaceModel {
    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        // do nothing
    }
}
