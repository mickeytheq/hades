package com.mickeytheq.hades.core.global.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GlobalConfiguration {
    private CardPreviewConfiguration cardPreviewConfiguration = new CardPreviewConfiguration();

    @JsonProperty("CardPreview")
    public CardPreviewConfiguration getCardPreview() {
        return cardPreviewConfiguration;
    }

    public void setCardPreview(CardPreviewConfiguration cardPreviewConfiguration) {
        this.cardPreviewConfiguration = cardPreviewConfiguration;
    }
}
