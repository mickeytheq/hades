package com.mickeytheq.hades.core.model;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.serialise.discriminator.EmptyEntityDiscriminator;
import org.apache.commons.lang3.StringUtils;

public class CardMetadataModel implements EmptyEntityDiscriminator {
    private String comments;
    private Integer quantity;

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

    @Override
    public boolean isEmpty() {
        if (!StringUtils.isEmpty(comments))
            return false;

        if (quantity != null)
            return false;

        return true;
    }
}
