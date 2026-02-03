package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.BaseCardFaceModel;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.serialise.discriminator.NumberEmptyWhenZeroDiscriminator;

@Model(typeCode = "Enemy", version = 1)
public class Enemy extends BaseCardFaceModel implements HasCommonCardFieldsModel {
    private final EnemyFieldsModel enemyFieldsModel = new EnemyFieldsModel();
    private final CommonCardFieldsModel commonCardFieldsModel = new CommonCardFieldsModel();
    private final CollectionModel collectionModel = new CollectionModel();
    private final EncounterSetModel encounterSetModel = new EncounterSetModel();
    private final PortraitModel portraitModel = new PortraitModel();

    public Enemy() {
    }

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        encounterSetModel.initialiseNew(projectContext, cardFaceSide);
        collectionModel.initialiseNew(projectContext, cardFaceSide);
    }

    @Property("Enemy")
    public EnemyFieldsModel getEnemyFieldsModel() {
        return enemyFieldsModel;
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

    public static class EnemyFieldsModel {
        private Statistic combat = Statistic.empty();
        private Statistic health = Statistic.empty();
        private Statistic evade = Statistic.empty();
        private int damage = 0;
        private int horror = 0;
        private WeaknessType weaknessType;

        @Property("Combat")
        public Statistic getCombat() {
            return combat;
        }

        public void setCombat(Statistic combat) {
            this.combat = combat;
        }

        @Property("Health")
        public Statistic getHealth() {
            return health;
        }

        public void setHealth(Statistic health) {
            this.health = health;
        }

        @Property("Evade")
        public Statistic getEvade() {
            return evade;
        }

        public void setEvade(Statistic evade) {
            this.evade = evade;
        }

        @Property(value = "Damage", discriminator = NumberEmptyWhenZeroDiscriminator.class)
        public int getDamage() {
            return damage;
        }

        public void setDamage(int damage) {
            this.damage = damage;
        }

        @Property(value = "Horror", discriminator = NumberEmptyWhenZeroDiscriminator.class)
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
    }
}
