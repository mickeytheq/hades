package com.mickeytheq.ahlcg4j.core.view.utils;

import ca.cgjennings.layout.TextStyle;
import resources.Settings;

import java.awt.*;
import java.awt.font.TextAttribute;

public class TextStyleUtils {
    public static final String AHLCG_SYMBOL_FONT = "AHLCGSymbol";

    private static final TextStyle LARGE_LABEL_TEXT_STYLE;
    private static final TextStyle TITLE_TEXT_STYLE;
    private static final TextStyle SUB_TYPE_TEXT_STYLE;
    private static final TextStyle BODY_TEXT_STYLE;
    private static final TextStyle TRAIT_TEXT_STYLE;
    private static final TextStyle VICTORY_TEXT_STYLE;
    private static final TextStyle STORY_TEXT_STYLE;
    private static final TextStyle FLAVOR_TEXT_STYLE;
    private static final TextStyle ARKHAM_HORROR_FONT_TEXT_STYLE;
    private static final TextStyle COLLECTION_NUMBER_TEXT_STYLE;
    private static final TextStyle ENCOUNTER_NUMBER_TEXT_STYLE;
    private static final TextStyle ARTIST_TEXT_STYLE;
    private static final TextStyle COPYRIGHT_TEXT_STYLE;
    private static final TextStyle HEADER_TEXT_STYLE;

    static {
        // TODO: change all the style references to a specific Font rather than family as there are multiple 'Arno Pro' fonts out there
        LARGE_LABEL_TEXT_STYLE = new TextStyle();
        LARGE_LABEL_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        LARGE_LABEL_TEXT_STYLE.add(TextAttribute.SIZE, 11.2);
        LARGE_LABEL_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        LARGE_LABEL_TEXT_STYLE.add(TextAttribute.WIDTH, 1.05);
        LARGE_LABEL_TEXT_STYLE.add(TextAttribute.TRACKING, 0);
        LARGE_LABEL_TEXT_STYLE.add(TextAttribute.FOREGROUND, Settings.Colour.BLACK);

        TITLE_TEXT_STYLE = new TextStyle();
        TITLE_TEXT_STYLE.add(TextAttribute.FAMILY, "Arkhamic");
        TITLE_TEXT_STYLE.add(TextAttribute.SIZE, 22.0);
        TITLE_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
        TITLE_TEXT_STYLE.add(TextAttribute.WIDTH, 1);
        TITLE_TEXT_STYLE.add(TextAttribute.TRACKING, 0.015);
        TITLE_TEXT_STYLE.add(TextAttribute.FOREGROUND, Color.BLACK);
        TITLE_TEXT_STYLE.add(TextAttribute.KERNING, TextAttribute.KERNING_ON);

        SUB_TYPE_TEXT_STYLE = new TextStyle();
        SUB_TYPE_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        SUB_TYPE_TEXT_STYLE.add(TextAttribute.SIZE, 12.0);
        SUB_TYPE_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        SUB_TYPE_TEXT_STYLE.add(TextAttribute.WIDTH, 1);
        SUB_TYPE_TEXT_STYLE.add(TextAttribute.FOREGROUND, Color.BLACK);

        BODY_TEXT_STYLE = new TextStyle();
        BODY_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        BODY_TEXT_STYLE.add(TextAttribute.SIZE, 17.2);
        BODY_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_MEDIUM);
        BODY_TEXT_STYLE.add(TextAttribute.WIDTH, 0.98);
        BODY_TEXT_STYLE.add(TextAttribute.TRACKING, -0.01);
        BODY_TEXT_STYLE.add(TextAttribute.FOREGROUND, Color.BLACK);

        TRAIT_TEXT_STYLE = new TextStyle();
        TRAIT_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        TRAIT_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_MEDIUM);
        TRAIT_TEXT_STYLE.add(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);

        VICTORY_TEXT_STYLE = new TextStyle();
        VICTORY_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        VICTORY_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);

        STORY_TEXT_STYLE = new TextStyle();
        STORY_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        STORY_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
        STORY_TEXT_STYLE.add(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);

        // TODO: flavor is the same as story, just copy?
        FLAVOR_TEXT_STYLE = new TextStyle();
        FLAVOR_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        FLAVOR_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
        FLAVOR_TEXT_STYLE.add(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);

        ARKHAM_HORROR_FONT_TEXT_STYLE = new TextStyle();
        ARKHAM_HORROR_FONT_TEXT_STYLE.add(TextAttribute.FAMILY, AHLCG_SYMBOL_FONT);
        ARKHAM_HORROR_FONT_TEXT_STYLE.add(TextAttribute.SIZE, 13.6);

        COLLECTION_NUMBER_TEXT_STYLE = new TextStyle();
        COLLECTION_NUMBER_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        COLLECTION_NUMBER_TEXT_STYLE.add(TextAttribute.SIZE, 9.0);
        COLLECTION_NUMBER_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        COLLECTION_NUMBER_TEXT_STYLE.add(TextAttribute.TRACKING, 0.01);
        COLLECTION_NUMBER_TEXT_STYLE.add(TextAttribute.FOREGROUND, Color.WHITE);

        // same as collection
        ENCOUNTER_NUMBER_TEXT_STYLE = COLLECTION_NUMBER_TEXT_STYLE;
        ARTIST_TEXT_STYLE = COLLECTION_NUMBER_TEXT_STYLE;
        COPYRIGHT_TEXT_STYLE = COLLECTION_NUMBER_TEXT_STYLE;

        // same as victory
        HEADER_TEXT_STYLE = VICTORY_TEXT_STYLE;
    }

    public static TextStyle getLargeLabelTextStyle() {
        return LARGE_LABEL_TEXT_STYLE;
    }

    public static TextStyle getTitleTextStyle() {
        return TITLE_TEXT_STYLE;
    }

    public static TextStyle getSubTypeTextStyle() {
        return SUB_TYPE_TEXT_STYLE;
    }

    public static TextStyle getBodyTextStyle() {
        return BODY_TEXT_STYLE;
    }

    public static TextStyle getStoryTextStyle() {
        return STORY_TEXT_STYLE;
    }

    public static TextStyle getVictoryTextStyle() {
        return VICTORY_TEXT_STYLE;
    }

    public static TextStyle getFlavorTextStyle() {
        return FLAVOR_TEXT_STYLE;
    }

    public static TextStyle getTraitTextStyle() {
        return TRAIT_TEXT_STYLE;
    }

    public static TextStyle getArkhamHorrorFontTextStyle() {
        return ARKHAM_HORROR_FONT_TEXT_STYLE;
    }

    public static TextStyle getCollectionNumberTextStyle() {
        return COLLECTION_NUMBER_TEXT_STYLE;
    }

    public static TextStyle getEncounterNumberTextStyle() {
        return ENCOUNTER_NUMBER_TEXT_STYLE;
    }

    public static TextStyle getArtistTextStyle() {
        return ARTIST_TEXT_STYLE;
    }

    public static TextStyle getCopyrightTextStyle() {
        return COPYRIGHT_TEXT_STYLE;
    }

    public static TextStyle getHeaderTextStyle() {
        return HEADER_TEXT_STYLE;
    }
}
