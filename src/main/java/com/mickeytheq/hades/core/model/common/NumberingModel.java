package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;

public class NumberingModel {
    private String encounterNumber;
    private String encounterTotal;
    private String collectionNumber;
    private PortraitModel collectionPortraitModel = new PortraitModel();
    private PortraitModel encounterPortraitModel = new PortraitModel();

    @Property("EncounterNumber")
    public String getEncounterNumber() {
        return encounterNumber;
    }

    public void setEncounterNumber(String encounterNumber) {
        this.encounterNumber = encounterNumber;
    }

    @Property("EncounterTotal")
    public String getEncounterTotal() {
        return encounterTotal;
    }

    public void setEncounterTotal(String encounterTotal) {
        this.encounterTotal = encounterTotal;
    }

    @Property("CollectionNumber")
    public String getCollectionNumber() {
        return collectionNumber;
    }

    public void setCollectionNumber(String collectionNumber) {
        this.collectionNumber = collectionNumber;
    }

    @Property("CollectionPortrait")
    public PortraitModel getCollectionPortraitModel() {
        return collectionPortraitModel;
    }

    public void setCollectionPortraitModel(PortraitModel collectionPortraitModel) {
        this.collectionPortraitModel = collectionPortraitModel;
    }

    @Property("EncounterPortrait")
    public PortraitModel getEncounterPortraitModel() {
        return encounterPortraitModel;
    }

    public void setEncounterPortraitModel(PortraitModel encounterPortraitModel) {
        this.encounterPortraitModel = encounterPortraitModel;
    }
}
