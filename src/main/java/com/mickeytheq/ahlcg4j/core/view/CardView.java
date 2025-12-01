package com.mickeytheq.ahlcg4j.core.view;

import com.mickeytheq.ahlcg4j.core.model.Card;

/**
 * Container that joins the {@link Card} model and the front and back {@link CardFaceView}
 */
public class CardView {
    private final Card card;

    private CardFaceView frontFaceView;
    private CardFaceView backFaceView;

    public CardView(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public CardFaceView getFrontFaceView() {
        return frontFaceView;
    }

    public void setFrontFaceView(CardFaceView frontFaceView) {
        this.frontFaceView = frontFaceView;
    }

    public CardFaceView getBackFaceView() {
        if (!hasBack())
            throw new IllegalStateException("Card does not have a back view");

        return backFaceView;
    }

    public void setBackFaceView(CardFaceView backFaceView) {
        this.backFaceView = backFaceView;
    }

    public boolean hasBack() {
        return card.hasBack();
    }
}
