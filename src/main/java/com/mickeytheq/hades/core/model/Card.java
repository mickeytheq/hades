package com.mickeytheq.hades.core.model;

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
    private String comments;

    public Card() {
    }

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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean hasBack() {
        return backFaceModel != null;
    }
}
