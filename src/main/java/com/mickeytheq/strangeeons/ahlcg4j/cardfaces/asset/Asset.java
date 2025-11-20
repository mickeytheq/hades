package com.mickeytheq.strangeeons.ahlcg4j.cardfaces.asset;

import com.mickeytheq.strangeeons.ahlcg4j.CardFaceType;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.BaseCardFaceModel;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.CommonCardFieldsModel;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.NumberingModel;
import com.mickeytheq.strangeeons.ahlcg4j.codegenerated.InterfaceConstants;
import resources.Settings;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@CardFaceType(typeCode = "Asset", interfaceLanguageKey = InterfaceConstants.ASSET)
public class Asset extends BaseCardFaceModel {
    public enum AssetType {
        Guardian, Seeker, Mystic, Rogue, Survivor, Neutral
    }

    private AssetType assetType;

    private CommonCardFieldsModel commonCardFieldsModel;
    private NumberingModel numberingModel;

    @Override
    public void initialiseModel() {
        // default to Guardian
        assetType = AssetType.Guardian;

        commonCardFieldsModel = new CommonCardFieldsModel();
        numberingModel = new NumberingModel();
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    public CommonCardFieldsModel getCommonCardFieldsModel() {
        return commonCardFieldsModel;
    }

    public NumberingModel getNumberingModel() {
        return numberingModel;
    }

    @Override
    public void afterSettingsRead(Settings settings, ObjectInputStream objectInputStream) {

    }

    @Override
    public void beforeSettingsWrite(Settings settings) {

    }

    @Override
    public void afterSettingsWrite(ObjectOutputStream objectOutputStream) {

    }
}
