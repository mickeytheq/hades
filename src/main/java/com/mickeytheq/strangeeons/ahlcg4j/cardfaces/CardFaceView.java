package com.mickeytheq.strangeeons.ahlcg4j.cardfaces;

import ca.cgjennings.apps.arkham.sheet.Sheet;
import com.mickeytheq.strangeeons.ahlcg4j.Card;
import com.mickeytheq.strangeeons.ahlcg4j.CardFaceSide;
import resources.Settings;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface CardFaceView<M extends CardFaceModel> {
    void initialiseView(M cardFaceModel);

    void createEditors(JTabbedPane tabbedPane);

    Sheet<Card> createSheet();
}
