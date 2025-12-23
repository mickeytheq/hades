package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.configuration.CollectionInfo;
import com.mickeytheq.hades.core.project.configuration.EncounterSetInfo;

public class NumberingModel {
    private EncounterSetInfo encounterSet;
    private String encounterNumber;
    private String encounterTotal;
    private CollectionInfo collection;
    private String collectionNumber;

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

    @Property("EncounterSet")
    public EncounterSetInfo getEncounterSet() {
        return encounterSet;
    }

    public void setEncounterSet(EncounterSetInfo encounterSet) {
        this.encounterSet = encounterSet;
    }

    @Property("Collection")
    public CollectionInfo getCollection() {
        return collection;
    }

    public void setCollection(CollectionInfo collection) {
        this.collection = collection;
    }
}
