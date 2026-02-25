package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.serialise.discriminator.BooleanEmptyWhenFalseDiscriminator;
import org.apache.commons.lang3.StringUtils;

public class ActFieldsModel {
    private boolean copyOtherFace = false;
    private String number;
    private String deckId;
    private Statistic clues = Statistic.empty();

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

    @Property("DeckId")
    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }

    @Property("Clues")
    public Statistic getClues() {
        return clues;
    }

    public void setClues(Statistic clues) {
        this.clues = clues;
    }
}
