package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.BaseCardFaceModel;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

@Model(typeCode = "Treachery", version = 1)
public class Treachery extends BaseCardFaceModel implements HasCommonCardFieldsModel, HasEncounterSetModel, HasCollectionModel, HasPortraitModel {
    private final TreacheryFieldsModel treacheryFieldsModel = new TreacheryFieldsModel();
    private final CommonCardFieldsModel commonCardFieldsModel = new CommonCardFieldsModel();
    private final CollectionModel collectionModel = new CollectionModel();
    private final EncounterSetModel encounterSetModel = new EncounterSetModel();
    private final PortraitModel portraitModel = new PortraitModel();

    public Treachery() {
    }

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        encounterSetModel.initialiseNew(projectContext, cardFaceSide);
        collectionModel.initialiseNew(projectContext, cardFaceSide);
    }

    @Property("Treachery")
    public TreacheryFieldsModel getTreacheryFieldsModel() {
        return treacheryFieldsModel;
    }

    @Property(CardModelPropertyNames.GENERAL)
    public CommonCardFieldsModel getCommonCardFieldsModel() {
        return commonCardFieldsModel;
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

    public static class TreacheryFieldsModel {
        private WeaknessType weaknessType;

        @Property("WeaknessType")
        public WeaknessType getWeaknessType() {
            return weaknessType;
        }

        public void setWeaknessType(WeaknessType weaknessType) {
            this.weaknessType = weaknessType;
        }
    }
}
