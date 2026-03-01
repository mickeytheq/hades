package com.mickeytheq.hades.core.global.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mickeytheq.hades.core.CardDimensions;
import com.mickeytheq.hades.util.shape.Unit;
import com.mickeytheq.hades.util.shape.UnitConversionUtils;

public class CardPreviewConfiguration {
    private int desiredPreviewResolutionPpi = 600;
    private boolean showBleedMargin = true;
    private double desiredBleedMarginInPoints = CardDimensions.BLEED_POINTS;

    @JsonProperty("DesiredPreviewResolutionPpi")
    public int getDesiredPreviewResolutionPpi() {
        return desiredPreviewResolutionPpi;
    }

    public void setDesiredPreviewResolutionPpi(int desiredPreviewResolutionPpi) {
        this.desiredPreviewResolutionPpi = desiredPreviewResolutionPpi;
    }

    @JsonProperty("ShowBleedMargin")
    public boolean isShowBleedMargin() {
        return showBleedMargin;
    }

    public void setShowBleedMargin(boolean showBleedMargin) {
        this.showBleedMargin = showBleedMargin;
    }

    @JsonProperty("DesiredBleedMarginInPoints")
    public double getDesiredBleedMarginInPoints() {
        return desiredBleedMarginInPoints;
    }

    public void setDesiredBleedMarginInPoints(double desiredBleedMarginInPoints) {
        this.desiredBleedMarginInPoints = desiredBleedMarginInPoints;
    }
}
