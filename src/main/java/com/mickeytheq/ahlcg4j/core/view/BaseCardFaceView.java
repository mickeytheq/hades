package com.mickeytheq.ahlcg4j.core.view;

import com.mickeytheq.ahlcg4j.core.model.CardFaceModel;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class BaseCardFaceView<M extends CardFaceModel> implements CardFaceView {
    private CardView cardView;
    private CardFaceSide cardFaceSide;
    private M cardFaceModel;

    @Override
    public final void initialiseView(CardView cardView, CardFaceSide cardFaceSide, CardFaceModel cardFaceModel) {
        this.cardView = cardView;
        this.cardFaceSide = cardFaceSide;

        // TODO: validate the model is of the correct type

        this.cardFaceModel = (M)cardFaceModel;

        initialiseView();
    }

    public void initialiseView() {
        // default do nothing
    }

    public CardView getCardView() {
        return cardView;
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
