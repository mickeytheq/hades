package com.mickeytheq.hades.core.model;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.view.CardFaceSide;

import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

/**
 * Card model that contains a front and optional back face plus any fields that are not face specific.
 *
 * In the majority of cases the interesting information is in the {@link CardFaceModel} implementations.
 */
public class Card {
    private String id = UUID.randomUUID().toString();
    private CardFaceModel frontFaceModel;
    private CardFaceModel backFaceModel;
    private final CardMetadataModel cardMetadataModel = new CardMetadataModel();

    public Card() {
    }

    @Property("UniqueId")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CardFaceModel getFrontFaceModel() {
        return frontFaceModel;
    }

    public void setFrontFaceModel(CardFaceModel frontFaceModel) {
        this.frontFaceModel = frontFaceModel;
    }

    public CardFaceModel getBackFaceModel() {
        if (!hasBack())
            throw new IllegalStateException("No back face in this model");

        return backFaceModel;
    }

    public void setBackFaceModel(CardFaceModel backFaceModel) {
        this.backFaceModel = backFaceModel;
    }

    public CardFaceModel getCardFaceModel(CardFaceSide cardFaceSide) {
        if (cardFaceSide == CardFaceSide.Front)
            return getFrontFaceModel();
        else
            return getBackFaceModel();
    }

    public void setCardFaceModel(CardFaceSide cardFaceSide, CardFaceModel cardFaceModel) {
        if (cardFaceSide == CardFaceSide.Front)
            setFrontFaceModel(cardFaceModel);
        else
            setBackFaceModel(cardFaceModel);
    }

    public boolean hasBack() {
        return backFaceModel != null;
    }

    public CardMetadataModel getCardMetadataModel() {
        return cardMetadataModel;
    }
}
