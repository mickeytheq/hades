package com.mickeytheq.strangeeons.ahlcg4j.cardfaces;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.strangeeons.ahlcg4j.CardGameComponent;
import com.mickeytheq.strangeeons.ahlcg4j.CardFaceSide;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

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
