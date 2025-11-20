package com.mickeytheq.strangeeons.ahlcg4j.cardfaces;

public class NumberingModel {
    private String encounterNumber;
    private String encounterTotal;
    private String collectionNumber;
    private PortraitModel collectionPortraitModel = new PortraitModel();
    private PortraitModel encounterPortraitModel = new PortraitModel();

    public String getEncounterNumber() {
        return encounterNumber;
    }

    public void setEncounterNumber(String encounterNumber) {
        this.encounterNumber = encounterNumber;
    }

    public String getEncounterTotal() {
        return encounterTotal;
    }

    public void setEncounterTotal(String encounterTotal) {
        this.encounterTotal = encounterTotal;
    }

    public String getCollectionNumber() {
        return collectionNumber;
    }

    public void setCollectionNumber(String collectionNumber) {
        this.collectionNumber = collectionNumber;
    }

    public PortraitModel getCollectionPortraitModel() {
        return collectionPortraitModel;
    }

    public void setCollectionPortraitModel(PortraitModel collectionPortraitModel) {
        this.collectionPortraitModel = collectionPortraitModel;
    }

    public PortraitModel getEncounterPortraitModel() {
        return encounterPortraitModel;
    }

    public void setEncounterPortraitModel(PortraitModel encounterPortraitModel) {
        this.encounterPortraitModel = encounterPortraitModel;
    }
}
