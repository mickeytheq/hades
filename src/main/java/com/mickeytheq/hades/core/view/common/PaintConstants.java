package com.mickeytheq.hades.core.view.common;

import com.mickeytheq.hades.util.shape.DimensionEx;

public class PaintConstants {
    // the height of regions used for footer text in millimetres
    // footer text are elements like artist, copyright, encounter numbers, collection numbers
    public static double FOOTER_TEXT_HEIGHT_MMS = 1.69;

    // container for encounter set icons and basic weakness icons used on story/story weakness/basic weakness cards
    public static DimensionEx ENCOUNTER_SET_CIRCLE_OVERLAY_SIZE = DimensionEx.millimetres(10.245, 10.245);

    // the vanilla basic weakness icon size
    public static DimensionEx BASIC_WEAKNESS_ICON_SIZE = DimensionEx.millimetres(6.00, 6.00);

    // encounter set icon size to be used on player cards
    public static DimensionEx ENCOUNTER_SET_ICON_SIZE = DimensionEx.millimetres(5.08, 5.08);

}
