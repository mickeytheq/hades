package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.BaseCardFaceModel;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.common.CardModelUtils;
import com.mickeytheq.hades.core.model.common.HasPortraitModel;
import com.mickeytheq.hades.core.model.common.PortraitModel;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

@Model(typeCode = "InvestigatorMiniCard", version = 1)
public class InvestigatorMiniCard extends BaseCardFaceModel implements HasPortraitModel {
    private final PortraitModel portraitModel;

    public InvestigatorMiniCard() {
        this.portraitModel = new PortraitModel();
    }

    @Property(CardModelUtils.ART_PORTRAIT)
    public PortraitModel getPortraitModel() {
        return portraitModel;
    }

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        portraitModel.setCopyOtherFace(cardFaceSide == CardFaceSide.Back);
    }
}
