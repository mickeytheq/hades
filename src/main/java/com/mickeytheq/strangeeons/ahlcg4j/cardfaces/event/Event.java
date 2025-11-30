package com.mickeytheq.strangeeons.ahlcg4j.cardfaces.event;

import com.mickeytheq.strangeeons.ahlcg4j.CardFaceType;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.CardFaceModel;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.CommonCardFieldsModel;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.NumberingModel;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.PlayerCardFieldsModel;
import com.mickeytheq.strangeeons.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.strangeeons.ahlcg4j.entity.Property;

@CardFaceType(typeCode = "Event", interfaceLanguageKey = InterfaceConstants.EVENT)
public class Event implements CardFaceModel {
    private final CommonCardFieldsModel commonCardFieldsModel;
    private final NumberingModel numberingModel;
    private final PlayerCardFieldsModel playerCardFieldsModel;

    public Event() {
        playerCardFieldsModel = new PlayerCardFieldsModel();
        commonCardFieldsModel = new CommonCardFieldsModel();
        numberingModel = new NumberingModel();
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
}
