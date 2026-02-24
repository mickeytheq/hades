package com.mickeytheq.hades.core;

import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.model.cardfaces.Location;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.model.image.ImageProxy;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.ProjectContexts;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.core.model.CardFaceModel;
import com.mickeytheq.hades.core.view.CardFaceView;
import com.mickeytheq.hades.core.view.CardView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class Cards {
    public static Card createNewCardModel(Class<? extends CardFaceModel> frontFaceModelClass, Class<? extends CardFaceModel> backFaceModelClass, ProjectContext projectContext) {
        return ProjectContexts.withContextReturn(projectContext, () -> {
            CardFaceModel frontFaceModel = createModelForClass(frontFaceModelClass);
            frontFaceModel.initialiseNew(projectContext, CardFaceSide.Front);

            CardFaceModel backFaceModel = null;
            if (backFaceModelClass != null) {
                backFaceModel = createModelForClass(backFaceModelClass);
                backFaceModel.initialiseNew(projectContext, CardFaceSide.Back);
            }

            return createCardModel(frontFaceModel, backFaceModel);
        });
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

    public static CardFaceModel createFaceModelForTypeCode(String typeCode, ProjectContext projectContext) {
        return ProjectContexts.withContextReturn(projectContext, () -> {
            CardFaceTypeRegister.CardFaceInfo cardFaceInfo = CardFaceTypeRegister.get().getInfoForTypeCode(typeCode);

            return createModelForClass(cardFaceInfo.getCardFaceModelClass());
        });
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

    // resolves any 'copy other face' or similar references in the card model so they reflect the value being pointed to
    // this makes it easier for readers to consume the data rather than having to detect these instances
    public static void resolveReferences(Card card) {
        if (!card.hasBack()) {
            resolveReferences(card.getFrontFaceModel(), null);
            return;
        }

        // resolve references both ways
        // any circular references will be resolved by the first pass setting the target to null/not-set which will then be copied
        // back on the second pass
        resolveReferences(card.getFrontFaceModel(), card.getBackFaceModel());
        resolveReferences(card.getBackFaceModel(), card.getFrontFaceModel());
    }

    private static void resolveReferences(CardFaceModel targetModel, CardFaceModel otherFaceModel) {
        // titles
        HasCommonCardFieldsModel.getInstance(targetModel).ifPresent(commonCardFieldsModel -> {
            if (!commonCardFieldsModel.getCopyOtherFaceTitles())
                return;

            Optional<CommonCardFieldsModel> otherModel = Optional.empty();

            if (otherFaceModel != null)
                otherModel = HasCommonCardFieldsModel.getInstance(otherFaceModel);

            if (!otherModel.isPresent()) {
                commonCardFieldsModel.setTitle(null);
                commonCardFieldsModel.setSubtitle(null);
            }
            else {
                commonCardFieldsModel.setTitle(otherModel.get().getTitle());
                commonCardFieldsModel.setTitle(otherModel.get().getSubtitle());
            }
        });

        // portrait
        HasPortraitModel.getInstance(targetModel).ifPresent(portraitModel -> {
            if (!portraitModel.isCopyOtherFace())
                return;

            Optional<PortraitModel> otherModel = Optional.empty();

            if (otherFaceModel != null)
                otherModel = HasPortraitModel.getInstance(otherFaceModel);

            if (!otherModel.isPresent()) {
                portraitModel.setImage(ImageProxy.createEmptyReadOnly());
            }
            else {
                portraitModel.setImage(otherModel.get().getImage());
                portraitModel.setPanX(otherModel.get().getPanX());
                portraitModel.setPanY(otherModel.get().getPanY());
                portraitModel.setScale(otherModel.get().getScale());
                portraitModel.setRotation(otherModel.get().getRotation());
            }
        });

        // encounter set
        HasEncounterSetModel.getInstance(targetModel).ifPresent(encounterSetModel -> {
            if (!encounterSetModel.isCopyOtherFace())
                return;

            Optional<EncounterSetModel> otherModel = Optional.empty();

            if (otherFaceModel != null)
                otherModel = HasEncounterSetModel.getInstance(otherFaceModel);

            if (!otherModel.isPresent()) {
                encounterSetModel.setEncounterSetConfiguration(null);
                encounterSetModel.setNumber(null);
                encounterSetModel.setTotal(null);
            }
            else {
                encounterSetModel.setEncounterSetConfiguration(otherModel.get().getEncounterSetConfiguration());
                encounterSetModel.setNumber(otherModel.get().getNumber());
                encounterSetModel.setTotal(otherModel.get().getTotal());
            }
        });

        // collection
        HasCollectionModel.getInstance(targetModel).ifPresent(collectionModel -> {
            if (!collectionModel.isCopyOtherFace())
                return;

            Optional<CollectionModel> otherModel = Optional.empty();

            if (otherFaceModel != null)
                otherModel = HasCollectionModel.getInstance(otherFaceModel);

            if (!otherModel.isPresent()) {
                collectionModel.setCollectionConfiguration(null);
                collectionModel.setNumber(null);
            }
            else {
                collectionModel.setCollectionConfiguration(otherModel.get().getCollectionConfiguration());
                collectionModel.setNumber(otherModel.get().getNumber());
            }
        });

        // location
        HasLocationFieldsModel.getInstance(targetModel).ifPresent(locationFieldsModel -> {
            Optional<LocationFieldsModel> otherModel = Optional.empty();

            if (otherFaceModel != null)
                otherModel = HasLocationFieldsModel.getInstance(otherFaceModel);

            // full copy
            if (locationFieldsModel.isCopyOtherFace()) {
                locationFieldsModel.setLocationIcon(otherModel.map(LocationFieldsModel::getLocationIcon).orElse(null));
                locationFieldsModel.setConnectionIcon1(otherModel.map(LocationFieldsModel::getConnectionIcon1).orElse(null));
                locationFieldsModel.setConnectionIcon2(otherModel.map(LocationFieldsModel::getConnectionIcon2).orElse(null));
                locationFieldsModel.setConnectionIcon3(otherModel.map(LocationFieldsModel::getConnectionIcon3).orElse(null));
                locationFieldsModel.setConnectionIcon4(otherModel.map(LocationFieldsModel::getConnectionIcon4).orElse(null));
                locationFieldsModel.setConnectionIcon5(otherModel.map(LocationFieldsModel::getConnectionIcon5).orElse(null));
                locationFieldsModel.setConnectionIcon6(otherModel.map(LocationFieldsModel::getConnectionIcon6).orElse(null));
                return;
            }

            // look for individual copies
            if (LocationFieldsModel.COPY_OTHER_VALUE.equals(locationFieldsModel.getLocationIcon()))
                locationFieldsModel.setLocationIcon(otherModel.map(LocationFieldsModel::getLocationIcon).orElse(null));

            if (LocationFieldsModel.COPY_OTHER_VALUE.equals(locationFieldsModel.getConnectionIcon1()))
                locationFieldsModel.setConnectionIcon1(otherModel.map(LocationFieldsModel::getConnectionIcon1).orElse(null));

            if (LocationFieldsModel.COPY_OTHER_VALUE.equals(locationFieldsModel.getConnectionIcon2()))
                locationFieldsModel.setConnectionIcon2(otherModel.map(LocationFieldsModel::getConnectionIcon2).orElse(null));

            if (LocationFieldsModel.COPY_OTHER_VALUE.equals(locationFieldsModel.getConnectionIcon3()))
                locationFieldsModel.setConnectionIcon3(otherModel.map(LocationFieldsModel::getConnectionIcon3).orElse(null));

            if (LocationFieldsModel.COPY_OTHER_VALUE.equals(locationFieldsModel.getConnectionIcon4()))
                locationFieldsModel.setConnectionIcon4(otherModel.map(LocationFieldsModel::getConnectionIcon4).orElse(null));

            if (LocationFieldsModel.COPY_OTHER_VALUE.equals(locationFieldsModel.getConnectionIcon5()))
                locationFieldsModel.setConnectionIcon5(otherModel.map(LocationFieldsModel::getConnectionIcon5).orElse(null));

            if (LocationFieldsModel.COPY_OTHER_VALUE.equals(locationFieldsModel.getConnectionIcon6()))
                locationFieldsModel.setConnectionIcon6(otherModel.map(LocationFieldsModel::getConnectionIcon6).orElse(null));
        });

        // act
        HasActFieldsModel.getInstance(targetModel).ifPresent(actFieldsModel -> {
            if (!actFieldsModel.isCopyOtherFace())
                return;

            Optional<ActFieldsModel> otherModel = Optional.empty();

            if (otherFaceModel != null)
                otherModel = HasActFieldsModel.getInstance(otherFaceModel);

            if (!otherModel.isPresent()) {
                actFieldsModel.setNumber(null);
                actFieldsModel.setDeckId(null);
            }
            else {
                actFieldsModel.setNumber(otherModel.get().getNumber());
                actFieldsModel.setDeckId(otherModel.get().getDeckId());
            }
        });

        // agenda
        HasAgendaFieldsModel.getInstance(targetModel).ifPresent(agendaFieldsModel -> {
            if (!agendaFieldsModel.isCopyOtherFace())
                return;

            Optional<AgendaFieldsModel> otherModel = Optional.empty();

            if (otherFaceModel != null)
                otherModel = HasAgendaFieldsModel.getInstance(otherFaceModel);

            if (!otherModel.isPresent()) {
                agendaFieldsModel.setNumber(null);
                agendaFieldsModel.setDeckId(null);
            }
            else {
                agendaFieldsModel.setNumber(otherModel.get().getNumber());
                agendaFieldsModel.setDeckId(otherModel.get().getDeckId());
            }
        });
    }
}
