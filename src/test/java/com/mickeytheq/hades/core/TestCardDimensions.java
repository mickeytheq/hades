package com.mickeytheq.hades.core;

import org.testng.Assert;
import org.testng.annotations.Test;

// sanity checks to make sure no rounding errors creep in and off-by-one the pixel counts
public class TestCardDimensions {
    @Test
    public void testBleed() {
        Assert.assertEquals(CardDimensions.BLEED_PIXELS_AT_600_PPI, 72);
        Assert.assertEquals(CardDimensions.BLEED_PIXELS_AT_300_PPI, 36);
    }

    @Test
    public void testStandard600PPI() {
        Assert.assertEquals(CardDimensions.STANDARD_PORTRAIT_600_PPI_WITH_BLEED_PIXELS.getWidth(), 1500 + CardDimensions.BLEED_PIXELS_AT_600_PPI * 2);
        Assert.assertEquals(CardDimensions.STANDARD_PORTRAIT_600_PPI_WITH_BLEED_PIXELS.getHeight(), 2100 + CardDimensions.BLEED_PIXELS_AT_600_PPI * 2);

        Assert.assertEquals(CardDimensions.STANDARD_LANDSCAPE_600_PPI_WITH_BLEED_PIXELS.getHeight(), 1500 + CardDimensions.BLEED_PIXELS_AT_600_PPI * 2);
        Assert.assertEquals(CardDimensions.STANDARD_LANDSCAPE_600_PPI_WITH_BLEED_PIXELS.getWidth(), 2100 + CardDimensions.BLEED_PIXELS_AT_600_PPI * 2);
    }

    @Test
    public void testStandard300PPI() {
        Assert.assertEquals(CardDimensions.STANDARD_PORTRAIT_300_PPI.getWidth(), 750);
        Assert.assertEquals(CardDimensions.STANDARD_PORTRAIT_300_PPI.getHeight(), 1050);

        Assert.assertEquals(CardDimensions.STANDARD_LANDSCAPE_300_PPI.getHeight(), 750);
        Assert.assertEquals(CardDimensions.STANDARD_LANDSCAPE_300_PPI.getWidth(), 1050);
    }

    @Test
    public void testMiniCard600PPI() {
        Assert.assertEquals(CardDimensions.MINI_CARD_PORTRAIT_600_PPI_WITH_BLEED_PIXELS.getWidth(), 1119 + CardDimensions.BLEED_PIXELS_AT_600_PPI * 2);
        Assert.assertEquals(CardDimensions.MINI_CARD_PORTRAIT_600_PPI_WITH_BLEED_PIXELS.getHeight(), 1440 + CardDimensions.BLEED_PIXELS_AT_600_PPI * 2);
    }
}
