package com.mickeytheq.hades.core;

import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.core.model.CardFaceModel;
import com.mickeytheq.hades.core.view.CardFaceView;
import com.mickeytheq.hades.core.view.CardView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CardFaces {
    public static Card createNewCardModel(Class<? extends CardFaceModel> frontFaceModelClass, Class<? extends CardFaceModel> backFaceModelClass, ProjectContext projectContext) {
        CardFaceModel frontFaceModel = createModelForClass(frontFaceModelClass);
        frontFaceModel.initialiseNew(projectContext, CardFaceSide.Front);

        CardFaceModel backFaceModel = null;
        if (backFaceModelClass != null) {
            backFaceModel = createModelForClass(backFaceModelClass);
            backFaceModel.initialiseNew(projectContext, CardFaceSide.Back);
        }

        return createCardModel(frontFaceModel, backFaceModel);
    }

    public static Card createCardModel(CardFaceModel frontFaceModel, CardFaceModel backFaceModel) {
        Card card = new Card();

        card.setFrontFaceModel(frontFaceModel);
        card.setBackFaceModel(backFaceModel);

        return card;
    }

    public static CardView createCardView(Card card, ProjectContext projectContext) {
        CardView cardView = new CardView(card, projectContext);

        CardFaceView frontFaceView = createViewForModel(card.getFrontFaceModel());
        cardView.setFrontFaceView(frontFaceView);

        if (card.hasBack()) {
            CardFaceView backFaceView = createViewForModel(card.getBackFaceModel());
            cardView.setBackFaceView(backFaceView);
        }

        // initialise views after the Card is fully populated
        cardView.getFrontFaceView().initialiseView(cardView, CardFaceSide.Front, card.getFrontFaceModel());

        if (card.hasBack())
            cardView.getBackFaceView().initialiseView(cardView, CardFaceSide.Back, card.getBackFaceModel());

        return cardView;
    }

    public static CardFaceModel createFaceModelForTypeCode(String typeCode) {
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

    public static CardFaceView createViewForModel(CardFaceModel cardFaceModel) {
        CardFaceTypeRegister.CardFaceInfo cardFaceInfo = CardFaceTypeRegister.get().getInfoForCardFaceModelClass(cardFaceModel.getClass());
        CardFaceView cardFaceView = createView(cardFaceInfo.getCardFaceViewClass());

        return cardFaceView;
    }

    private static CardFaceView createView(Class<? extends CardFaceView> cardFaceViewClass) {
        Constructor<? extends CardFaceView> constructor;
        try {
            constructor = cardFaceViewClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("The required single argument 'Card' constructor is not present on CardFaceView class '" + cardFaceViewClass.getName() + "'");
        }

        CardFaceView cardFaceView;
        try {
            cardFaceView = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return cardFaceView;
    }
}
