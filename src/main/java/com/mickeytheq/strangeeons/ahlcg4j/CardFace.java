package com.mickeytheq.strangeeons.ahlcg4j;

import ca.cgjennings.apps.arkham.sheet.Sheet;
import resources.Settings;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface CardFace {
    // called immediately after a CardFace is constructed, either created new or de-serialised/loaded from a file
    void initialise(Card card, CardFaceSide cardFaceSide);

    Card getCard();

    CardFaceSide getCardFaceSide();

    void createEditors(JTabbedPane tabbedPane);

    Sheet<Card> createSheet();

    void afterSettingsRead(Settings settings, ObjectInputStream objectInputStream);

    void beforeSettingsWrite(Settings settings);

    void afterSettingsWrite(ObjectOutputStream objectOutputStream);
}
