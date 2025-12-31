package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.BaseCardFaceModel;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Model(typeCode = "Asset")
public class Asset extends BaseCardFaceModel {
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

    private AssetSlot assetSlot1;
    private AssetSlot assetSlot2;
    private Statistic health;
    private Statistic sanity;

    private final CommonCardFieldsModel commonCardFieldsModel;
    private final CollectionModel collectionModel = new CollectionModel();
    private final EncounterSetModel encounterSetModel = new EncounterSetModel();
    private final PlayerCardFieldsModel playerCardFieldsModel;
    private final PortraitWithArtistModel portraitWithArtistModel;

    public Asset() {
        playerCardFieldsModel = new PlayerCardFieldsModel();
        commonCardFieldsModel = new CommonCardFieldsModel();
        portraitWithArtistModel = new PortraitWithArtistModel();

        health = Statistic.empty();
        sanity = Statistic.empty();
    }

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        encounterSetModel.initialiseNew(projectContext, cardFaceSide);
        collectionModel.initialiseNew(projectContext, cardFaceSide);
    }

    @Property("AssetSlot1")
    public AssetSlot getAssetSlot1() {
        return assetSlot1;
    }

    public void setAssetSlot1(AssetSlot assetSlot1) {
        this.assetSlot1 = assetSlot1;
    }

    @Property("AssetSlot2")
    public AssetSlot getAssetSlot2() {
        return assetSlot2;
    }

    public void setAssetSlot2(AssetSlot assetSlot2) {
        this.assetSlot2 = assetSlot2;
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

    @Property(flatten = true)
    public PlayerCardFieldsModel getPlayerCardFieldsModel() {
        return playerCardFieldsModel;
    }

    @Property("ArtPortrait")
    public PortraitWithArtistModel getPortraitWithArtistModel() {
        return portraitWithArtistModel;
    }

    public List<AssetSlot> getAssetSlots() {
        return Stream.of(getAssetSlot1(), getAssetSlot2())
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }
}
