package com.mickeytheq.hades.core.global.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mickeytheq.hades.util.shape.Unit;
import com.mickeytheq.hades.util.shape.UnitConversionUtils;

public class CardPreviewConfiguration {
    private int desiredPreviewResolutionPpi = 300;
    private boolean showBleedMargin = false;

    // the bleed margin in the 600ppi templates is 72 pixels so use this is our default value, i.e. use all the available bleed
    private double desiredBleedMarginInPoints = UnitConversionUtils.convertUnit(Unit.Pixel, Unit.Point, 72, 600);

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
