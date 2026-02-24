package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.CardFaceModel;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

// TODO: parallel investigator support
@Model(typeCode = "Investigator", version = 1)
public class Investigator implements CardFaceModel, HasCommonCardFieldsModel, HasEncounterSetModel, HasCollectionModel, HasPortraitModel {
    private final InvestigatorFieldsModel investigatorFieldsModel = new InvestigatorFieldsModel();
    private final CommonCardFieldsModel commonCardFieldsModel = new CommonCardFieldsModel();
    private final CollectionModel collectionModel = new CollectionModel();
    private final EncounterSetModel encounterSetModel = new EncounterSetModel();
    private final PortraitModel portraitModel = new PortraitModel();

    public Investigator() {
        commonCardFieldsModel.setUnique(true);

        investigatorFieldsModel.setInvestigatorClass(InvestigatorClass.Guardian);
        investigatorFieldsModel.setHealth("7");
        investigatorFieldsModel.setSanity("7");
        investigatorFieldsModel.setWillpower("3");
        investigatorFieldsModel.setIntellect("3");
        investigatorFieldsModel.setCombat("3");
        investigatorFieldsModel.setAgility("3");
    }

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        encounterSetModel.initialiseNew(projectContext, cardFaceSide);
        collectionModel.initialiseNew(projectContext, cardFaceSide);
    }

    @Property("Investigator")
    public InvestigatorFieldsModel getInvestigatorFieldsModel() {
        return investigatorFieldsModel;
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

    public static class InvestigatorFieldsModel {
        private InvestigatorClass investigatorClass;
        private String health;
        private String sanity;
        private String willpower;
        private String intellect;
        private String combat;
        private String agility;

        @Property("Class")
        public InvestigatorClass getInvestigatorClass() {
            return investigatorClass;
        }

        public void setInvestigatorClass(InvestigatorClass investigatorClass) {
            this.investigatorClass = investigatorClass;
        }

        @Property("Health")
        public String getHealth() {
            return health;
        }

        public void setHealth(String health) {
            this.health = health;
        }

        @Property("Sanity")
        public String getSanity() {
            return sanity;
        }

        public void setSanity(String sanity) {
            this.sanity = sanity;
        }

        @Property("Willpower")
        public String getWillpower() {
            return willpower;
        }

        public void setWillpower(String willpower) {
            this.willpower = willpower;
        }

        @Property("Intellect")
        public String getIntellect() {
            return intellect;
        }

        public void setIntellect(String intellect) {
            this.intellect = intellect;
        }

        @Property("Combat")
        public String getCombat() {
            return combat;
        }

        public void setCombat(String combat) {
            this.combat = combat;
        }

        @Property("Agility")
        public String getAgility() {
            return agility;
        }

        public void setAgility(String agility) {
            this.agility = agility;
        }
    }
}
