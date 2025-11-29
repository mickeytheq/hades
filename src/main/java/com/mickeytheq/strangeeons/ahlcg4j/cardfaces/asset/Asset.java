package com.mickeytheq.strangeeons.ahlcg4j.cardfaces.asset;

import com.mickeytheq.strangeeons.ahlcg4j.CardFaceType;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.BaseCardFaceModel;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.CommonCardFieldsModel;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.NumberingModel;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.PlayerCardFieldsModel;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.common.PlayerCardClass;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.common.PlayerCardSkillIcon;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.common.PlayerCardType;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.common.Statistic;
import com.mickeytheq.strangeeons.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.strangeeons.ahlcg4j.entity.Property;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CardFaceType(typeCode = "Asset", interfaceLanguageKey = InterfaceConstants.ASSET)
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
    private final NumberingModel numberingModel;
    private final PlayerCardFieldsModel playerCardFieldsModel;

    public Asset() {
        playerCardFieldsModel = new PlayerCardFieldsModel();
        commonCardFieldsModel = new CommonCardFieldsModel();
        numberingModel = new NumberingModel();

        health = Statistic.empty();
        sanity = Statistic.empty();
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

    @Property(flatten = true)
    public NumberingModel getNumberingModel() {
        return numberingModel;
    }

    @Property(flatten = true)
    public PlayerCardFieldsModel getPlayerCardFieldsModel() {
        return playerCardFieldsModel;
    }

    public List<AssetSlot> getAssetSlots() {
        return Stream.of(getAssetSlot1(), getAssetSlot2())
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }
}
