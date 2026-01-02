package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.BaseCardFaceModel;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

@Model(typeCode = "Enemy")
public class Enemy extends BaseCardFaceModel {
    private WeaknessType weaknessType;

    private Statistic combat = Statistic.empty();
    private Statistic health = Statistic.empty();
    private Statistic evade = Statistic.empty();
    private int damage = 0;
    private int horror = 0;

    private final CommonCardFieldsModel commonCardFieldsModel = new CommonCardFieldsModel();
    private final CollectionModel collectionModel = new CollectionModel();
    private final EncounterSetModel encounterSetModel = new EncounterSetModel();
    private final PortraitModel portraitModel = new PortraitModel();

    public Enemy() {
        weaknessType = WeaknessType.None;
    }

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        encounterSetModel.initialiseNew(projectContext, cardFaceSide);
        collectionModel.initialiseNew(projectContext, cardFaceSide);
    }

    public Statistic getCombat() {
        return combat;
    }

    public void setCombat(Statistic combat) {
        this.combat = combat;
    }

    public Statistic getHealth() {
        return health;
    }

    public void setHealth(Statistic health) {
        this.health = health;
    }

    public Statistic getEvade() {
        return evade;
    }

    public void setEvade(Statistic evade) {
        this.evade = evade;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getHorror() {
        return horror;
    }

    public void setHorror(int horror) {
        this.horror = horror;
    }

    @Property("WeaknessType")
    public WeaknessType getWeaknessType() {
        return weaknessType;
    }

    public void setWeaknessType(WeaknessType weaknessType) {
        this.weaknessType = weaknessType;
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
}
