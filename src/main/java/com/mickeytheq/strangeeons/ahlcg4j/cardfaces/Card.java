package com.mickeytheq.strangeeons.ahlcg4j.cardfaces;

public class Card {
    private CardFaceModel frontFaceModel;
    private CardFaceView frontFaceView;
    private CardFaceModel backFaceModel;
    private CardFaceView backFaceView;

    private String comments;

    public CardFaceModel getFrontFaceModel() {
        return frontFaceModel;
    }

    public void setFrontFaceModel(CardFaceModel frontFaceModel) {
        this.frontFaceModel = frontFaceModel;
    }

    public CardFaceView getFrontFaceView() {
        return frontFaceView;
    }

    public void setFrontFaceView(CardFaceView frontFaceView) {
        this.frontFaceView = frontFaceView;
    }

    public CardFaceModel getBackFaceModel() {
        return backFaceModel;
    }

    public void setBackFaceModel(CardFaceModel backFaceModel) {
        this.backFaceModel = backFaceModel;
    }

    public CardFaceView getBackFaceView() {
        return backFaceView;
    }

    public void setBackFaceView(CardFaceView backFaceView) {
        this.backFaceView = backFaceView;
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
