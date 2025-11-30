package com.mickeytheq.ahlcg4j.core.view;

import com.mickeytheq.ahlcg4j.core.Card;
import com.mickeytheq.ahlcg4j.core.model.CardFaceModel;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class BaseCardFaceView<M extends CardFaceModel> implements CardFaceView<M> {
    private Card card;
    private CardFaceSide cardFaceSide;
    private M cardFaceModel;

    @Override
    public final void initialiseView(Card card, CardFaceSide cardFaceSide, M cardFaceModel) {
        this.card = card;
        this.cardFaceSide = cardFaceSide;
        this.cardFaceModel = cardFaceModel;

        initialiseView();
    }

    public void initialiseView() {
        // default do nothing
    }

    public Card getCard() {
        return card;
    }

    public CardFaceSide getCardFaceSide() {
        return cardFaceSide;
    }

    public M getModel() {
        return cardFaceModel;
    }

    @Override
    public Dimension getDimension() {
        BufferedImage image = getTemplateImage();

        return new Dimension(image.getWidth(), image.getHeight());
    }

    protected abstract BufferedImage getTemplateImage();
}
