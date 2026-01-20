package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.CardFaceModel;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

// TODO: parallel investigator support
@Model(typeCode = "Investigator")
public class Investigator implements CardFaceModel, HasCommonCardFieldsModel {
    private final CommonCardFieldsModel commonCardFieldsModel;
    private final CollectionModel collectionModel = new CollectionModel();
    private final EncounterSetModel encounterSetModel = new EncounterSetModel();
    private final PortraitModel portraitModel;
    private InvestigatorClass investigatorClass;
    private String health;
    private String sanity;
    private String willpower;
    private String intellect;
    private String combat;
    private String agility;

    public Investigator() {
        commonCardFieldsModel = new CommonCardFieldsModel();
        portraitModel = new PortraitModel();

        investigatorClass = InvestigatorClass.Guardian;
        commonCardFieldsModel.setUnique(true);
        health = "7";
        sanity = "7";
        willpower = "3";
        intellect = "3";
        combat = "3";
        agility = "3";
    }

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        encounterSetModel.initialiseNew(projectContext, cardFaceSide);
        collectionModel.initialiseNew(projectContext, cardFaceSide);
    }

    @Property(flatten = true)
    public CommonCardFieldsModel getCommonCardFieldsModel() {
        return commonCardFieldsModel;
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

    @Property("InvestigatorClass")
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
