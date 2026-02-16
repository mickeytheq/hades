package com.mickeytheq.hades.core.global.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GlobalConfiguration {
    private CardPreviewConfiguration cardPreviewConfiguration = new CardPreviewConfiguration();

    @JsonProperty("CardPreview")
    public CardPreviewConfiguration getCardPreviewConfiguration() {
        return cardPreviewConfiguration;
    }

    public void setCardPreviewConfiguration(CardPreviewConfiguration cardPreviewConfiguration) {
        this.cardPreviewConfiguration = cardPreviewConfiguration;
    }
}
