package com.mickeytheq.ahlcg4j.core;

import com.mickeytheq.ahlcg4j.core.view.CardFaceSide;
import com.mickeytheq.ahlcg4j.core.model.CardFaceModel;
import com.mickeytheq.ahlcg4j.core.view.CardFaceView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CardFaces {
    public static Card createCard(String frontFaceTypeCode, String backFaceTypeCode) {
        CardFaceModel frontFaceModel = createModelForTypeCode(frontFaceTypeCode);

        CardFaceModel backFaceModel = null;
        if (backFaceTypeCode != null)
            backFaceModel = createModelForTypeCode(backFaceTypeCode);

        return createCard(frontFaceModel, backFaceModel);
    }

    public static Card createCard(Class<? extends CardFaceModel> frontFaceModelClass, Class<? extends CardFaceModel> backFaceModelClass) {
        CardFaceModel frontFaceModel = createModelForClass(frontFaceModelClass);

        CardFaceModel backFaceModel = null;
        if (backFaceModelClass != null)
            backFaceModel = createModelForClass(backFaceModelClass);

        return createCard(frontFaceModel, backFaceModel);
    }

    public static Card createCard(CardFaceModel frontFaceModel, CardFaceModel backFaceModel) {
        Card card = new Card();

        card.setFrontFaceModel(frontFaceModel);
        card.setBackFaceModel(backFaceModel);

        CardFaceView<?> frontFaceView = createViewForModel(card, CardFaceSide.Front, card.getFrontFaceModel());
        card.setFrontFaceView(frontFaceView);

        if (backFaceModel != null) {
            CardFaceView<?> backFaceView = createViewForModel(card, CardFaceSide.Front, card.getFrontFaceModel());
            card.setBackFaceView(backFaceView);
        }

        // initialise views after the Card is fully populated
        card.getFrontFaceView().initialiseView(card, CardFaceSide.Front, card.getFrontFaceModel());

        if (card.getBackFaceView() != null)
            card.getBackFaceView().initialiseView(card, CardFaceSide.Back, card.getBackFaceModel());

        return card;
    }

    public static CardFaceModel createModelForTypeCode(String typeCode) {
        CardFaceTypeRegister.CardFaceInfo cardFaceInfo = CardFaceTypeRegister.get().getInfoForTypeCode(typeCode);

        return createModelForClass(cardFaceInfo.getCardFaceModelClass());
    }

    private static CardFaceModel createModelForClass(Class<? extends CardFaceModel> cardFaceClass) {
        Constructor<? extends CardFaceModel> constructor;
        try {
            constructor = cardFaceClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("The required single argument 'Card' constructor is not present on CardFace class '" + cardFaceClass.getName() + "'");
        }

        CardFaceModel cardFaceModel;
        try {
            cardFaceModel = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return cardFaceModel;
    }

    public static <M extends CardFaceModel> CardFaceView<M> createViewForModel(Card card, CardFaceSide cardFaceSide, M cardFaceModel) {
        CardFaceTypeRegister.CardFaceInfo cardFaceInfo = CardFaceTypeRegister.get().getInfoForCardFaceModelClass(cardFaceModel.getClass());
        CardFaceView<M> cardFaceView = (CardFaceView<M>) createView(cardFaceInfo.getCardFaceViewClass());

        return cardFaceView;
    }

    private static CardFaceView<?> createView(Class<? extends CardFaceView> cardFaceViewClass) {
        Constructor<? extends CardFaceView> constructor;
        try {
            constructor = cardFaceViewClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("The required single argument 'Card' constructor is not present on CardFaceView class '" + cardFaceViewClass.getName() + "'");
        }

        CardFaceView<?> cardFaceView;
        try {
            cardFaceView = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return cardFaceView;
    }
}
