package com.mickeytheq.hades.core.model;

import com.mickeytheq.hades.core.model.entity.Property;

public class CardMetadataModel {
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
}
