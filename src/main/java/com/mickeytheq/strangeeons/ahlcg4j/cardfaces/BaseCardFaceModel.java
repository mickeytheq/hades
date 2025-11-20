package com.mickeytheq.strangeeons.ahlcg4j.cardfaces;

import com.mickeytheq.strangeeons.ahlcg4j.Card;
import com.mickeytheq.strangeeons.ahlcg4j.CardFaceSide;

public abstract class BaseCardFaceModel implements CardFaceModel {
    // these two fields should be considered effectively final as they are
    // set during initialisation and should not be touched again
    private Card card;
    private CardFaceSide cardFaceSide;

    @Override
    public final void initialiseModel(Card card, CardFaceSide cardFaceSide) {
        this.card = card;
        this.cardFaceSide = cardFaceSide;

        initialiseModel();
    }

    public Card getCard() {
        return card;
    }

    public CardFaceSide getCardFaceSide() {
        return cardFaceSide;
    }

    public void initialiseModel() {
        // default do nothing - for sub-classes to override
    }
}
