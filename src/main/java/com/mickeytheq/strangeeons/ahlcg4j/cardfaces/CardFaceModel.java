package com.mickeytheq.strangeeons.ahlcg4j.cardfaces;

import com.mickeytheq.strangeeons.ahlcg4j.Card;
import com.mickeytheq.strangeeons.ahlcg4j.CardFaceSide;
import resources.Settings;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface CardFaceModel {
    void initialiseModel(Card card, CardFaceSide cardFaceSide);

    Card getCard();

    CardFaceSide getCardFaceSide();
}
