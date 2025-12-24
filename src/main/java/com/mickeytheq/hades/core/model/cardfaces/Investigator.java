package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.CardFaceModel;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.common.CommonCardFieldsModel;
import com.mickeytheq.hades.core.model.common.InvestigatorClass;
import com.mickeytheq.hades.core.model.common.NumberingModel;
import com.mickeytheq.hades.core.model.common.PortraitWithArtistModel;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

// TODO: parallel investigator support
@Model(typeCode = "Investigator")
public class Investigator implements CardFaceModel {
    private CommonCardFieldsModel commonCardFieldsModel;
    private NumberingModel numberingModel;
    private PortraitWithArtistModel portraitWithArtistModel;
    private InvestigatorClass investigatorClass;
    private String health;
    private String sanity;
    private String willpower;
    private String intellect;
    private String combat;
    private String agility;

    public Investigator() {
        commonCardFieldsModel = new CommonCardFieldsModel();
        numberingModel = new NumberingModel();
        portraitWithArtistModel = new PortraitWithArtistModel();

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
        numberingModel.initialiseNew(projectContext, cardFaceSide);
    }

    @Property(flatten = true)
    public CommonCardFieldsModel getCommonCardFieldsModel() {
        return commonCardFieldsModel;
    }

    public void setCommonCardFieldsModel(CommonCardFieldsModel commonCardFieldsModel) {
        this.commonCardFieldsModel = commonCardFieldsModel;
    }

    @Property(flatten = true)
    public NumberingModel getNumberingModel() {
        return numberingModel;
    }

    public void setNumberingModel(NumberingModel numberingModel) {
        this.numberingModel = numberingModel;
    }

    @Property("ArtPortrait")
    public PortraitWithArtistModel getPortraitWithArtistModel() {
        return portraitWithArtistModel;
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
