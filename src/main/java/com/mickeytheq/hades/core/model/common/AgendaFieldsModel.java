package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.serialise.discriminator.BooleanEmptyWhenFalseDiscriminator;

public class AgendaFieldsModel {
    private boolean copyOtherFace = false;
    private String number;
    private String deckId;
    private Statistic doom = Statistic.empty();

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

    @Property("DeckId")
    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }

    @Property("Doom")
    public Statistic getDoom() {
        return doom;
    }

    public void setDoom(Statistic doom) {
        this.doom = doom;
    }
}
