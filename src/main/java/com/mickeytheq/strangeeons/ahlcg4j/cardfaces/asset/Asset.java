package com.mickeytheq.strangeeons.ahlcg4j.cardfaces.asset;

import com.mickeytheq.strangeeons.ahlcg4j.CardFaceType;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.BaseCardFaceModel;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.CommonCardFieldsModel;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.NumberingModel;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.common.PlayerCardClass;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.common.PlayerCardSkillIcon;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.common.PlayerCardType;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.common.Statistic;
import com.mickeytheq.strangeeons.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.strangeeons.ahlcg4j.entity.Property;

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

    private PlayerCardType playerCardType;
    private AssetSlot assetSlot1;
    private AssetSlot assetSlot2;
    private String cost;
    private Integer level;
    private PlayerCardClass playerCardClass1;
    private PlayerCardClass playerCardClass2;
    private PlayerCardClass playerCardClass3;
    private Statistic health;
    private Statistic sanity;
    private PlayerCardSkillIcon skillIcon1;
    private PlayerCardSkillIcon skillIcon2;
    private PlayerCardSkillIcon skillIcon3;
    private PlayerCardSkillIcon skillIcon4;
    private PlayerCardSkillIcon skillIcon5;

    private CommonCardFieldsModel commonCardFieldsModel;
    private NumberingModel numberingModel;

    public Asset() {
        commonCardFieldsModel = new CommonCardFieldsModel();
        numberingModel = new NumberingModel();

        health = Statistic.empty();
        sanity = Statistic.empty();
    }

    @Override
    public void initialiseModel() {
        // default to Guardian
        playerCardType = PlayerCardType.Standard;
        playerCardClass1 = PlayerCardClass.Guardian;
        cost = "0";
        level = 0;
    }

    @Property("PlayerCardType")
    public PlayerCardType getPlayerCardType() {
        return playerCardType;
    }

    public void setPlayerCardType(PlayerCardType playerCardType) {
        this.playerCardType = playerCardType;
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

    @Property("Cost")
    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    @Property("Level")
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Property("PlayerCardClass1")
    public PlayerCardClass getPlayerCardClass1() {
        return playerCardClass1;
    }

    public void setPlayerCardClass1(PlayerCardClass playerCardClass1) {
        this.playerCardClass1 = playerCardClass1;
    }

    @Property("PlayerCardClass2")
    public PlayerCardClass getPlayerCardClass2() {
        return playerCardClass2;
    }

    public void setPlayerCardClass2(PlayerCardClass playerCardClass2) {
        this.playerCardClass2 = playerCardClass2;
    }

    @Property("PlayerCardClass3")
    public PlayerCardClass getPlayerCardClass3() {
        return playerCardClass3;
    }

    public void setPlayerCardClass3(PlayerCardClass playerCardClass3) {
        this.playerCardClass3 = playerCardClass3;
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

    @Property("SkillIcon1")
    public PlayerCardSkillIcon getSkillIcon1() {
        return skillIcon1;
    }

    public void setSkillIcon1(PlayerCardSkillIcon skillIcon1) {
        this.skillIcon1 = skillIcon1;
    }

    @Property("SkillIcon2")
    public PlayerCardSkillIcon getSkillIcon2() {
        return skillIcon2;
    }

    public void setSkillIcon2(PlayerCardSkillIcon skillIcon2) {
        this.skillIcon2 = skillIcon2;
    }

    @Property("SkillIcon3")
    public PlayerCardSkillIcon getSkillIcon3() {
        return skillIcon3;
    }

    public void setSkillIcon3(PlayerCardSkillIcon skillIcon3) {
        this.skillIcon3 = skillIcon3;
    }

    @Property("SkillIcon4")
    public PlayerCardSkillIcon getSkillIcon4() {
        return skillIcon4;
    }

    public void setSkillIcon4(PlayerCardSkillIcon skillIcon4) {
        this.skillIcon4 = skillIcon4;
    }

    @Property("SkillIcon5")
    public PlayerCardSkillIcon getSkillIcon5() {
        return skillIcon5;
    }

    public void setSkillIcon5(PlayerCardSkillIcon skillIcon5) {
        this.skillIcon5 = skillIcon5;
    }

    @Property(flatten = true)
    public CommonCardFieldsModel getCommonCardFieldsModel() {
        return commonCardFieldsModel;
    }

    @Property(flatten = true)
    public NumberingModel getNumberingModel() {
        return numberingModel;
    }
}
