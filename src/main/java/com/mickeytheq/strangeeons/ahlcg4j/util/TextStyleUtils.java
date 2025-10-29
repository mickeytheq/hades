package com.mickeytheq.strangeeons.ahlcg4j.util;

import ca.cgjennings.layout.TextStyle;
import resources.Settings;

import java.awt.*;
import java.awt.font.TextAttribute;

public class TextStyleUtils {
    private static final TextStyle LARGE_LABEL_TEXT_STYLE;
    private static final TextStyle TITLE_TEXT_STYLE;
    private static final TextStyle BODY_TEXT_STYLE;
    private static final TextStyle TRAIT_TEXT_STYLE;
    private static final TextStyle VICTORY_TEXT_STYLE;
    private static final TextStyle STORY_TEXT_STYLE;
    private static final TextStyle FLAVOR_TEXT_STYLE;
    private static final TextStyle ARKHAM_HORROR_FONT_TEXT_STYLE;
    private static final TextStyle COLLECTION_NUMBER_TEXT_STYLE;
    private static final TextStyle ENCOUNTER_NUMBER_TEXT_STYLE;

    static {
        // TODO: change all the style references to a specific Font rather than family as there are multiple 'Arno Pro' fonts out there
        LARGE_LABEL_TEXT_STYLE = new TextStyle();
        LARGE_LABEL_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        LARGE_LABEL_TEXT_STYLE.add(TextAttribute.SIZE, 5.6);
        LARGE_LABEL_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        LARGE_LABEL_TEXT_STYLE.add(TextAttribute.WIDTH, 1.05);
        LARGE_LABEL_TEXT_STYLE.add(TextAttribute.TRACKING, 0);
        LARGE_LABEL_TEXT_STYLE.add(TextAttribute.FOREGROUND, Settings.Colour.BLACK);

        TITLE_TEXT_STYLE = new TextStyle();
        TITLE_TEXT_STYLE.add(TextAttribute.FAMILY, "Arkhamic");
        TITLE_TEXT_STYLE.add(TextAttribute.SIZE, 11.0);
        TITLE_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
        TITLE_TEXT_STYLE.add(TextAttribute.WIDTH, 1);
        TITLE_TEXT_STYLE.add(TextAttribute.TRACKING, 0.015);
        TITLE_TEXT_STYLE.add(TextAttribute.FOREGROUND, Color.BLACK);
        TITLE_TEXT_STYLE.add(TextAttribute.KERNING, TextAttribute.KERNING_ON);

        BODY_TEXT_STYLE = new TextStyle();
        BODY_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        BODY_TEXT_STYLE.add(TextAttribute.SIZE, 8.6);
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
        ARKHAM_HORROR_FONT_TEXT_STYLE.add(TextAttribute.FAMILY, "AHLCGSymbol");
        ARKHAM_HORROR_FONT_TEXT_STYLE.add(TextAttribute.SIZE, 6.8);

        COLLECTION_NUMBER_TEXT_STYLE = new TextStyle();
        COLLECTION_NUMBER_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        COLLECTION_NUMBER_TEXT_STYLE.add(TextAttribute.SIZE, 4.5);
        COLLECTION_NUMBER_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        COLLECTION_NUMBER_TEXT_STYLE.add(TextAttribute.TRACKING, 0.01);
        COLLECTION_NUMBER_TEXT_STYLE.add(TextAttribute.FOREGROUND, Color.WHITE);

        // same as collection
        ENCOUNTER_NUMBER_TEXT_STYLE = COLLECTION_NUMBER_TEXT_STYLE;
    }

    public static TextStyle getLargeLabelTextStyle() {
        return LARGE_LABEL_TEXT_STYLE;
    }

    public static TextStyle getTitleTextStyle() {
        return TITLE_TEXT_STYLE;
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
}
