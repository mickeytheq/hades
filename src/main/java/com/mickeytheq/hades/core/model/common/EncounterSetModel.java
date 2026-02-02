package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.configuration.EncounterSetInfo;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.serialise.discriminator.BooleanEmptyWhenFalseDiscriminator;
import com.mickeytheq.hades.serialise.discriminator.EmptyEntityDiscriminator;
import org.apache.commons.lang3.StringUtils;

public class EncounterSetModel implements EmptyEntityDiscriminator {
    private boolean copyOtherFace = false;
    private EncounterSetInfo encounterSet;
    private String number;
    private String total;

    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        // default the use other options to true for the back face
        boolean isBackFace = cardFaceSide == CardFaceSide.Back;

        copyOtherFace = isBackFace;
    }

    @Override
    public boolean isEmpty() {
        if (encounterSet != null)
            return false;

        if (!StringUtils.isEmpty(number))
            return false;

        if (!StringUtils.isEmpty(total))
            return false;

        if (copyOtherFace)
            return false;

        return true;
    }

    @Property(value = CardModelPropertyNames.COPY_OTHER_FACE, discriminator = BooleanEmptyWhenFalseDiscriminator.class)
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

    @Property("Total")
    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Property(CardModelPropertyNames.ENCOUNTER_SET)
    public EncounterSetInfo getEncounterSet() {
        return encounterSet;
    }

    public void setEncounterSet(EncounterSetInfo encounterSet) {
        this.encounterSet = encounterSet;
    }
}
