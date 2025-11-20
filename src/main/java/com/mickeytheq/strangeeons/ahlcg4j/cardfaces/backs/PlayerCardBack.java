package com.mickeytheq.strangeeons.ahlcg4j.cardfaces.backs;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import com.mickeytheq.strangeeons.ahlcg4j.Card;
import com.mickeytheq.strangeeons.ahlcg4j.CardFaceType;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.BaseCardFaceModel;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.BaseCardFaceView;
import com.mickeytheq.strangeeons.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.strangeeons.ahlcg4j.util.ImageUtils;
import resources.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@CardFaceType(typeCode = "PlayerCardBack", interfaceLanguageKey = InterfaceConstants.BACK_PLAYER)
public class PlayerCardBack extends BaseCardFaceModel {
    @Override
    public void afterSettingsRead(Settings settings, ObjectInputStream objectInputStream) {
        // nothing to do
    }

    @Override
    public void beforeSettingsWrite(Settings settings) {
        // nothing to do
    }

    @Override
    public void afterSettingsWrite(ObjectOutputStream objectOutputStream) {
        // nothing to do
    }
}
