package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.configuration.CollectionInfo;
import com.mickeytheq.hades.core.project.configuration.EncounterSetInfo;
import com.mickeytheq.hades.core.view.CardFaceSide;

public class NumberingModel {
    private boolean encounterUseOtherFace;
    private EncounterSetInfo encounterSet;
    private String encounterNumber;
    private String encounterTotal;
    private boolean collectionUseOtherFace;
    private CollectionInfo collection;
    private String collectionNumber;

    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        // default the use other options to true for the back face
        boolean isBackFace = cardFaceSide == CardFaceSide.Back;

        encounterUseOtherFace = isBackFace;
        collectionUseOtherFace = isBackFace;
    }

    @Property("EncounterUseOtherFace")
    public boolean isEncounterUseOtherFace() {
        return encounterUseOtherFace;
    }

    public void setEncounterUseOtherFace(boolean encounterUseOtherFace) {
        this.encounterUseOtherFace = encounterUseOtherFace;
    }

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

    @Property("CollectionUseOtherFace")
    public boolean isCollectionUseOtherFace() {
        return collectionUseOtherFace;
    }

    public void setCollectionUseOtherFace(boolean collectionUseOtherFace) {
        this.collectionUseOtherFace = collectionUseOtherFace;
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
