package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.BaseCardFaceModel;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.serialise.discriminator.EmptyEntityDiscriminator;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Model(typeCode = "Asset", version = 1)
public class Asset extends BaseCardFaceModel implements HasCommonCardFieldsModel, HasEncounterSetModel, HasCollectionModel, HasPortraitModel {
    public enum AssetSlot {
        Ally,
        Accessory,
        Body,
        Hand,
        TwoHands,
        Arcane,
        TwoArcane,
        Tarot
    }

    private final AssetFieldsModel assetFieldsModel = new AssetFieldsModel();
    private final CommonCardFieldsModel commonCardFieldsModel;
    private final CollectionModel collectionModel = new CollectionModel();
    private final EncounterSetModel encounterSetModel = new EncounterSetModel();
    private final PlayerCardFieldsModel playerCardFieldsModel;
    private final PortraitModel portraitModel;

    public Asset() {
        playerCardFieldsModel = new PlayerCardFieldsModel();
        commonCardFieldsModel = new CommonCardFieldsModel();
        portraitModel = new PortraitModel();
    }

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        encounterSetModel.initialiseNew(projectContext, cardFaceSide);
        collectionModel.initialiseNew(projectContext, cardFaceSide);
        playerCardFieldsModel.initialiseNew(projectContext, cardFaceSide);
    }

    @Property("Asset")
    public AssetFieldsModel getAssetFieldsModel() {
        return assetFieldsModel;
    }

    @Property(CardModelUtils.GENERAL)
    public CommonCardFieldsModel getCommonCardFieldsModel() {
        return commonCardFieldsModel;
    }

    @Property(CardModelUtils.COLLECTION)
    public CollectionModel getCollectionModel() {
        return collectionModel;
    }

    @Property(CardModelUtils.ENCOUNTER_SET)
    public EncounterSetModel getEncounterSetModel() {
        return encounterSetModel;
    }

    @Property(CardModelUtils.PLAYER)
    public PlayerCardFieldsModel getPlayerCardFieldsModel() {
        return playerCardFieldsModel;
    }

    @Property(CardModelUtils.ART_PORTRAIT)
    public PortraitModel getPortraitModel() {
        return portraitModel;
    }

    public static class AssetFieldsModel implements EmptyEntityDiscriminator {
        private AssetSlot slot1;
        private AssetSlot slot2;
        private Statistic health = Statistic.empty();
        private Statistic sanity = Statistic.empty();

        @Property("Slot1")
        public AssetSlot getSlot1() {
            return slot1;
        }

        public void setSlot1(AssetSlot slot1) {
            this.slot1 = slot1;
        }

        @Property("Slot2")
        public AssetSlot getSlot2() {
            return slot2;
        }

        public void setSlot2(AssetSlot slot2) {
            this.slot2 = slot2;
        }

        @Property("Health")
        public Statistic getHealth() {
            return health;
        }

        public void setHealth(Statistic health) {
            this.health = health;
        }

        @Property("Sanity")
        public Statistic getSanity() {
            return sanity;
        }

        public void setSanity(Statistic sanity) {
            this.sanity = sanity;
        }

        // convenience method for getting a list of asset slots
        public List<AssetSlot> getAssetSlots() {
            return Stream.of(getSlot1(), getSlot2())
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
        }

        @Override
        public boolean isEmpty() {
            if (slot1 != null || slot2 != null)
                return false;

            if (!getHealth().isEmpty() || !getSanity().isEmpty())
                return false;

            return true;
        }
    }
}
