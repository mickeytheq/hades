package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.serialise.BooleanFalseDiscriminator;
import com.mickeytheq.hades.serialise.EmptyEntityDiscriminator;

public class LocationFieldsModel implements EmptyEntityDiscriminator {
    public static final String EMPTY_VALUE = null;
    public static final String NONE_VALUE = "(None)";
    public static final String COPY_OTHER_VALUE = "(CopyOther)";

    private Statistic shroud = Statistic.empty();
    private Statistic clues = Statistic.empty();

    private boolean copyOtherFace = false;

    private String locationIcon = EMPTY_VALUE;

    private String connectionIcon1 = EMPTY_VALUE;
    private String connectionIcon2 = EMPTY_VALUE;
    private String connectionIcon3 = EMPTY_VALUE;
    private String connectionIcon4 = EMPTY_VALUE;
    private String connectionIcon5 = EMPTY_VALUE;
    private String connectionIcon6 = EMPTY_VALUE;

    @Property("Shroud")
    public Statistic getShroud() {
        return shroud;
    }

    public void setShroud(Statistic shroud) {
        this.shroud = shroud;
    }

    @Property("Clues")
    public Statistic getClues() {
        return clues;
    }

    public void setClues(Statistic clues) {
        this.clues = clues;
    }

    @Property(value = CardModelPropertyNames.COPY_OTHER_FACE, discriminator = BooleanFalseDiscriminator.class)
    public boolean isCopyOtherFace() {
        return copyOtherFace;
    }

    public void setCopyOtherFace(boolean copyOtherFace) {
        this.copyOtherFace = copyOtherFace;
    }

    @Property("LocationIcon")
    public String getLocationIcon() {
        return locationIcon;
    }

    public void setLocationIcon(String locationIcon) {
        this.locationIcon = locationIcon;
    }

    @Property("ConnectionIcon1")
    public String getConnectionIcon1() {
        return connectionIcon1;
    }

    public void setConnectionIcon1(String connectionIcon1) {
        this.connectionIcon1 = connectionIcon1;
    }

    @Property("ConnectionIcon2")
    public String getConnectionIcon2() {
        return connectionIcon2;
    }

    public void setConnectionIcon2(String connectionIcon2) {
        this.connectionIcon2 = connectionIcon2;
    }

    @Property("ConnectionIcon3")
    public String getConnectionIcon3() {
        return connectionIcon3;
    }

    public void setConnectionIcon3(String connectionIcon3) {
        this.connectionIcon3 = connectionIcon3;
    }

    @Property("ConnectionIcon4")
    public String getConnectionIcon4() {
        return connectionIcon4;
    }

    public void setConnectionIcon4(String connectionIcon4) {
        this.connectionIcon4 = connectionIcon4;
    }

    @Property("ConnectionIcon5")
    public String getConnectionIcon5() {
        return connectionIcon5;
    }

    public void setConnectionIcon5(String connectionIcon5) {
        this.connectionIcon5 = connectionIcon5;
    }

    @Property("ConnectionIcon6")
    public String getConnectionIcon6() {
        return connectionIcon6;
    }

    public void setConnectionIcon6(String connectionIcon6) {
        this.connectionIcon6 = connectionIcon6;
    }

    @Override
    public boolean isEmpty() {
        if (isCopyOtherFace())
            return false;

        if (!getShroud().isEmpty())
            return false;

        if (!getClues().isEmpty())
            return false;

        if (getLocationIcon() != null)
            return false;

        if (getConnectionIcon1() != null)
            return false;

        if (getConnectionIcon2() != null)
            return false;

        if (getConnectionIcon3() != null)
            return false;

        if (getConnectionIcon4() != null)
            return false;

        if (getConnectionIcon5() != null)
            return false;

        if (getConnectionIcon6() != null)
            return false;

        return true;
    }
}
