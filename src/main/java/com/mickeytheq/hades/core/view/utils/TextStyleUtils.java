package com.mickeytheq.hades.core.view.utils;

import ca.cgjennings.layout.TextStyle;
import resources.Settings;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.Map;

public class TextStyleUtils {
    public static final String AHLCG_SYMBOL_FONT = "AHLCGSymbol";

    private static final TextStyle LARGE_LABEL_TEXT_STYLE;
    private static final TextStyle TITLE_TEXT_STYLE;
    private static final TextStyle SUBTITLE_TEXT_STYLE;
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
    private static final TextStyle INVESTIGATOR_SKILL_VALUE_TEXT_STYLE;
    private static final TextStyle ISS_TEXT_STYLE;
    private static final TextStyle CSS_TEXT_STYLE;
    private static final TextStyle GSS_TEXT_STYLE;
    private static final TextStyle SCENARIO_INDEX_TEXT_STYLE;
    private static final TextStyle SCENARIO_INDEX_BACK_TEXT_STYLE;
    private static final TextStyle SCENARIO_REFERENCE_DIFFICULTY_TEXT_STYLE;
    private static final TextStyle SCENARIO_REFERENCE_TRACKER_BOX_TITLE_TEXT_STYLE;
    private static final TextStyle SCENARIO_REFERENCE_TITLE_TEXT_STYLE;

    static {
        // TODO: change all the style references to a specific Font rather than family as there are multiple 'Arno Pro' fonts out there
        LARGE_LABEL_TEXT_STYLE = new TextStyle();
        LARGE_LABEL_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        LARGE_LABEL_TEXT_STYLE.add(TextAttribute.SIZE, 5.6);
        LARGE_LABEL_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        LARGE_LABEL_TEXT_STYLE.add(TextAttribute.WIDTH, 1.05);
        LARGE_LABEL_TEXT_STYLE.add(TextAttribute.TRACKING, 0);
        LARGE_LABEL_TEXT_STYLE.add(TextAttribute.FOREGROUND, Color.BLACK);

        TITLE_TEXT_STYLE = new TextStyle();
        TITLE_TEXT_STYLE.add(TextAttribute.FAMILY, "Arkhamic");
        TITLE_TEXT_STYLE.add(TextAttribute.SIZE, 11.0);
        TITLE_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
        TITLE_TEXT_STYLE.add(TextAttribute.WIDTH, 1);
        TITLE_TEXT_STYLE.add(TextAttribute.TRACKING, 0.015);
        TITLE_TEXT_STYLE.add(TextAttribute.FOREGROUND, Color.BLACK);
        TITLE_TEXT_STYLE.add(TextAttribute.KERNING, TextAttribute.KERNING_ON);

        SUBTITLE_TEXT_STYLE = new TextStyle();
        SUBTITLE_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        SUBTITLE_TEXT_STYLE.add(TextAttribute.SIZE, 6.4);
        SUBTITLE_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        SUBTITLE_TEXT_STYLE.add(TextAttribute.WIDTH, 0.96);
        SUBTITLE_TEXT_STYLE.add(TextAttribute.FOREGROUND, Color.BLACK);

        SUB_TYPE_TEXT_STYLE = new TextStyle();
        SUB_TYPE_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        SUB_TYPE_TEXT_STYLE.add(TextAttribute.SIZE, 6.0);
        SUB_TYPE_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        SUB_TYPE_TEXT_STYLE.add(TextAttribute.WIDTH, 1);
        SUB_TYPE_TEXT_STYLE.add(TextAttribute.FOREGROUND, Color.BLACK);

        BODY_TEXT_STYLE = new TextStyle();
        BODY_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        BODY_TEXT_STYLE.add(TextAttribute.SIZE, 8.6);
        BODY_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_MEDIUM);
        BODY_TEXT_STYLE.add(TextAttribute.WIDTH, 0.98);
        BODY_TEXT_STYLE.add(TextAttribute.TRACKING, -0.01);
        BODY_TEXT_STYLE.add(TextAttribute.FOREGROUND, Color.BLACK);

        TRAIT_TEXT_STYLE = new TextStyle();
        TRAIT_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        TRAIT_TEXT_STYLE.add(TextAttribute.SIZE, 8.6);
        TRAIT_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        TRAIT_TEXT_STYLE.add(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);

        VICTORY_TEXT_STYLE = new TextStyle();
        VICTORY_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        VICTORY_TEXT_STYLE.add(TextAttribute.SIZE, 8.6);
        VICTORY_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        VICTORY_TEXT_STYLE.add(TextAttribute.FOREGROUND, Color.BLACK);

        STORY_TEXT_STYLE = new TextStyle();
        STORY_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        STORY_TEXT_STYLE.add(TextAttribute.SIZE, 8.6);
        STORY_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
        STORY_TEXT_STYLE.add(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
        STORY_TEXT_STYLE.add(TextAttribute.FOREGROUND, Color.BLACK);

        // TODO: flavor is the same as story, just copy?
        FLAVOR_TEXT_STYLE = new TextStyle();
        FLAVOR_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        FLAVOR_TEXT_STYLE.add(TextAttribute.SIZE, 8.6);
        FLAVOR_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
        FLAVOR_TEXT_STYLE.add(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
        FLAVOR_TEXT_STYLE.add(TextAttribute.FOREGROUND, Color.BLACK);

        ARKHAM_HORROR_FONT_TEXT_STYLE = new TextStyle();
        ARKHAM_HORROR_FONT_TEXT_STYLE.add(TextAttribute.FAMILY, AHLCG_SYMBOL_FONT);
        ARKHAM_HORROR_FONT_TEXT_STYLE.add(TextAttribute.SIZE, 6.8);

        COLLECTION_NUMBER_TEXT_STYLE = new TextStyle();
        COLLECTION_NUMBER_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        COLLECTION_NUMBER_TEXT_STYLE.add(TextAttribute.SIZE, 4.5);
        COLLECTION_NUMBER_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        COLLECTION_NUMBER_TEXT_STYLE.add(TextAttribute.TRACKING, 0.01);
        COLLECTION_NUMBER_TEXT_STYLE.add(TextAttribute.FOREGROUND, Color.WHITE);

        INVESTIGATOR_SKILL_VALUE_TEXT_STYLE = new TextStyle();
        INVESTIGATOR_SKILL_VALUE_TEXT_STYLE.add(TextAttribute.FAMILY, "Bolton");
        INVESTIGATOR_SKILL_VALUE_TEXT_STYLE.add(TextAttribute.SIZE, 11.0);
        INVESTIGATOR_SKILL_VALUE_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_MEDIUM);
        INVESTIGATOR_SKILL_VALUE_TEXT_STYLE.add(TextAttribute.FOREGROUND, Color.BLACK);

        SCENARIO_REFERENCE_DIFFICULTY_TEXT_STYLE = new TextStyle();
        SCENARIO_REFERENCE_DIFFICULTY_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        SCENARIO_REFERENCE_DIFFICULTY_TEXT_STYLE.add(TextAttribute.SIZE, 6.0);
        SCENARIO_REFERENCE_DIFFICULTY_TEXT_STYLE.add(TextAttribute.WIDTH, 0.97);
        SCENARIO_REFERENCE_DIFFICULTY_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        SCENARIO_REFERENCE_DIFFICULTY_TEXT_STYLE.add(TextAttribute.FOREGROUND, Color.BLACK);

        // modified title style
        SCENARIO_REFERENCE_TRACKER_BOX_TITLE_TEXT_STYLE = new TextStyle();
        SCENARIO_REFERENCE_TRACKER_BOX_TITLE_TEXT_STYLE.add(TextAttribute.FAMILY, "Arkhamic");
        SCENARIO_REFERENCE_TRACKER_BOX_TITLE_TEXT_STYLE.add(TextAttribute.SIZE, 11.0 * 0.836);
        SCENARIO_REFERENCE_TRACKER_BOX_TITLE_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
        SCENARIO_REFERENCE_TRACKER_BOX_TITLE_TEXT_STYLE.add(TextAttribute.WIDTH, 1 * 1.02);
        SCENARIO_REFERENCE_TRACKER_BOX_TITLE_TEXT_STYLE.add(TextAttribute.FOREGROUND, Color.BLACK);

        SCENARIO_REFERENCE_TITLE_TEXT_STYLE = new TextStyle();
        SCENARIO_REFERENCE_TITLE_TEXT_STYLE.add(TextAttribute.FAMILY, "Arkhamic");
        SCENARIO_REFERENCE_TITLE_TEXT_STYLE.add(TextAttribute.SIZE, 11.0 * 1.318);
        SCENARIO_REFERENCE_TITLE_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
        SCENARIO_REFERENCE_TITLE_TEXT_STYLE.add(TextAttribute.WIDTH, 1 * 1.05);
        SCENARIO_REFERENCE_TITLE_TEXT_STYLE.add(TextAttribute.FOREGROUND, Color.BLACK);

        // story text but smaller
        ISS_TEXT_STYLE = new TextStyle();
        ISS_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        ISS_TEXT_STYLE.add(TextAttribute.SIZE, 8.6 * 0.925);
        ISS_TEXT_STYLE.add(TextAttribute.TRACKING, -0.01);
        ISS_TEXT_STYLE.add(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);

        // story text but black
        CSS_TEXT_STYLE = new TextStyle();
        CSS_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        CSS_TEXT_STYLE.add(TextAttribute.SIZE, 8.6);
        CSS_TEXT_STYLE.add(TextAttribute.TRACKING, -0.01);
        CSS_TEXT_STYLE.add(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
        CSS_TEXT_STYLE.add(TextAttribute.FOREGROUND, Color.BLACK);

        // story text - regular
        GSS_TEXT_STYLE = new TextStyle();
        GSS_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        GSS_TEXT_STYLE.add(TextAttribute.SIZE, 8.6);
        GSS_TEXT_STYLE.add(TextAttribute.TRACKING, -0.01);
        GSS_TEXT_STYLE.add(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);

        // for 'Agenda 1a' headers
        SCENARIO_INDEX_TEXT_STYLE = new TextStyle();
        SCENARIO_INDEX_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        SCENARIO_INDEX_TEXT_STYLE.add(TextAttribute.SIZE, 7.0);
        SCENARIO_INDEX_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        SCENARIO_INDEX_TEXT_STYLE.add(TextAttribute.FOREGROUND, Color.BLACK);

        // for 'Agenda 1a' headers on the back of cards (smaller)
        SCENARIO_INDEX_BACK_TEXT_STYLE = new TextStyle();
        SCENARIO_INDEX_BACK_TEXT_STYLE.add(TextAttribute.FAMILY, "Arno Pro");
        SCENARIO_INDEX_BACK_TEXT_STYLE.add(TextAttribute.SIZE, 4.9);
        SCENARIO_INDEX_BACK_TEXT_STYLE.add(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        SCENARIO_INDEX_BACK_TEXT_STYLE.add(TextAttribute.FOREGROUND, Color.BLACK);

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

    public static TextStyle getSubtitleTextStyle() {
        return SUBTITLE_TEXT_STYLE;
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

    public static TextStyle getInvestigatorSkillValueTextStyle() {
        return INVESTIGATOR_SKILL_VALUE_TEXT_STYLE;
    }

    public static TextStyle getIssStoryTextStyle() {
        return ISS_TEXT_STYLE;
    }

    public static TextStyle getCssStoryTextStyle() {
        return CSS_TEXT_STYLE;
    }

    public static TextStyle getGssStoryTextStyle() {
        return GSS_TEXT_STYLE;
    }

    public static TextStyle getScenarioIndexTextStyle() {
        return SCENARIO_INDEX_TEXT_STYLE;
    }

    public static TextStyle getScenarioIndexBackTextStyle() {
        return SCENARIO_INDEX_BACK_TEXT_STYLE;
    }

    public static TextStyle getScenarioReferenceDifficultyTextStyle() {
        return SCENARIO_REFERENCE_DIFFICULTY_TEXT_STYLE;
    }

    public static TextStyle getScenarioReferenceTitleTextStyle() {
        return SCENARIO_REFERENCE_TITLE_TEXT_STYLE;
    }

    public static TextStyle getScenarioReferenceTrackerBoxTitleTextStyle() {
        return SCENARIO_REFERENCE_TRACKER_BOX_TITLE_TEXT_STYLE;
    }

    public static Map<TextAttribute, Object> getAttributes(TextStyle textStyle) {
        // janky way of getting the attributes out of a style - create a fake string and apply the style to it
        // and capture all the attributes
        Map<TextAttribute, Object> attributeValues = new HashMap<>();

        AttributedString attributedString = new AttributedString("") {

            @Override
            public void addAttribute(AttributedCharacterIterator.Attribute attribute, Object value) {
                attributeValues.put((TextAttribute) attribute, value);
            }
        };

        textStyle.applyStyle(attributedString);

        return attributeValues;
    }
}
