package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.configuration.CollectionConfiguration;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.serialise.discriminator.BooleanEmptyWhenFalseDiscriminator;
import com.mickeytheq.hades.serialise.discriminator.EmptyEntityDiscriminator;
import org.apache.commons.lang3.StringUtils;

public class CollectionModel implements EmptyEntityDiscriminator {
    private boolean copyOtherFace = false;
    private CollectionConfiguration collectionConfiguration;
    private String number;

    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        // default the use other options to true for the back face
        boolean isBackFace = cardFaceSide == CardFaceSide.Back;

        copyOtherFace = isBackFace;
    }

    @Override
    public boolean isEmpty() {
        if (collectionConfiguration != null)
            return false;

        if (!StringUtils.isEmpty(number))
            return false;

        if (copyOtherFace)
            return false;

        return true;
    }

    @Property(value = CardModelUtils.COPY_OTHER_FACE, discriminator = BooleanEmptyWhenFalseDiscriminator.class)
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

    @Property(CardModelUtils.COLLECTION)
    public CollectionConfiguration getCollectionConfiguration() {
        return collectionConfiguration;
    }

    public void setCollectionConfiguration(CollectionConfiguration collectionConfiguration) {
        this.collectionConfiguration = collectionConfiguration;
    }
}
