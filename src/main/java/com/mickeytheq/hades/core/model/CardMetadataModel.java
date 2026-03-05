package com.mickeytheq.hades.core.model;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.serialise.discriminator.BooleanEmptyWhenFalseDiscriminator;
import com.mickeytheq.hades.serialise.discriminator.EmptyEntityDiscriminator;
import org.apache.commons.lang3.StringUtils;

public class CardMetadataModel implements EmptyEntityDiscriminator {
    private String comments;
    private Integer quantity;
    private boolean setAside;
    private boolean reward;
    private boolean startingLocation;

    @Property("Comments")
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Property("Quantity")
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Property(value = "SetAside", discriminator = BooleanEmptyWhenFalseDiscriminator.class)
    public boolean isSetAside() {
        return setAside;
    }

    public void setSetAside(boolean setAside) {
        this.setAside = setAside;
    }

    @Property(value = "Reward", discriminator = BooleanEmptyWhenFalseDiscriminator.class)
    public boolean isReward() {
        return reward;
    }

    public void setReward(boolean reward) {
        this.reward = reward;
    }

    @Property(value = "StartingLocation", discriminator = BooleanEmptyWhenFalseDiscriminator.class)
    public boolean isStartingLocation() {
        return startingLocation;
    }

    public void setStartingLocation(boolean startingLocation) {
        this.startingLocation = startingLocation;
    }

    @Override
    public boolean isEmpty() {
        if (!StringUtils.isEmpty(comments))
            return false;

        if (quantity != null)
            return false;

        if (setAside)
            return false;

        if (reward)
            return false;

        if (startingLocation)
            return false;

        return true;
    }
}
