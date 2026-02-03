package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.CardFaceModel;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

// a card face that shadows another card face for rendering purposes
@Model(typeCode = "Shadow", version = 1)
public class Shadow implements CardFaceModel {
    private String shadowCardId;
    private CardFaceSide shadowSide = CardFaceSide.Front;

    @Property("ShadowCardId")
    public String getShadowCardId() {
        return shadowCardId;
    }

    public void setShadowCardId(String shadowCardId) {
        this.shadowCardId = shadowCardId;
    }

    @Property("ShadowCardFaceSide")
    public CardFaceSide getShadowSide() {
        return shadowSide;
    }

    public void setShadowSide(CardFaceSide shadowSide) {
        this.shadowSide = shadowSide;
    }

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        // do nothing
    }
}
