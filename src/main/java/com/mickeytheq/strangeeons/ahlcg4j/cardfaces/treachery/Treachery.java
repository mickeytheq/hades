package com.mickeytheq.strangeeons.ahlcg4j.cardfaces.treachery;

import ca.cgjennings.apps.arkham.component.DefaultPortrait;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.*;
import com.mickeytheq.strangeeons.ahlcg4j.CardFaceType;
import com.mickeytheq.strangeeons.ahlcg4j.WeaknessType;
import com.mickeytheq.strangeeons.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.strangeeons.ahlcg4j.entity.Property;
import org.checkerframework.checker.units.qual.N;
import resources.Settings;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@CardFaceType(typeCode = "Treachery", interfaceLanguageKey = InterfaceConstants.TREACHERY)
public class Treachery extends BaseCardFaceModel {
    private WeaknessType weaknessType;

    private final CommonCardFieldsModel commonCardFieldsModel;

    private final NumberingModel numberingModel;

    public Treachery() {
        weaknessType = WeaknessType.None;

        commonCardFieldsModel = new CommonCardFieldsModel();
        numberingModel = new NumberingModel();
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

    @Property(flatten = true)
    public NumberingModel getNumberingModel() {
        return numberingModel;
    }
}
