package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.configuration.CollectionInfo;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.serialise.EmptyEntityDiscriminator;
import org.apache.commons.lang3.StringUtils;

public class CollectionModel implements EmptyEntityDiscriminator {
    private boolean copyOtherFace;
    private CollectionInfo collection;
    private String number;

    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        // default the use other options to true for the back face
        boolean isBackFace = cardFaceSide == CardFaceSide.Back;

        copyOtherFace = isBackFace;
    }

    @Override
    public boolean isEmpty() {
        if (collection != null)
            return false;

        if (!StringUtils.isEmpty(number))
            return false;

        if (copyOtherFace)
            return false;

        return true;
    }

    @Property("CopyOtherFace")
    public boolean isCopyOtherFace() {
        return copyOtherFace;
    }

    public void setCopyOtherFace(boolean copyOtherFace) {
        this.copyOtherFace = copyOtherFace;
    }

    @Property("Number")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Property("Collection")
    public CollectionInfo getCollection() {
        return collection;
    }

    public void setCollection(CollectionInfo collection) {
        this.collection = collection;
    }
}
