package com.mickeytheq.ahlcg4j.core.view;

import com.google.common.reflect.TypeToken;
import com.mickeytheq.ahlcg4j.core.model.CardFaceModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public abstract class BaseCardFaceView<M extends CardFaceModel> implements CardFaceView {
    private CardView cardView;
    private CardFaceSide cardFaceSide;
    private M cardFaceModel;

    @Override
    public final void initialiseView(CardView cardView, CardFaceSide cardFaceSide, CardFaceModel cardFaceModel) {
        this.cardView = cardView;
        this.cardFaceSide = cardFaceSide;

        // validate the model matches the type defined by the generic 'M'
        Class<M> declaredModelClass = (Class<M>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        if (!declaredModelClass.isInstance(cardFaceModel))
            throw new RuntimeException("Implementation of " + getClass().getSimpleName() + " declares " + declaredModelClass.getName() + " as required model type but a model of class " + cardFaceModel.getClass().getName() + " was provided");

        this.cardFaceModel = declaredModelClass.cast(cardFaceModel);

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
