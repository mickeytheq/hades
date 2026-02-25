package com.mickeytheq.hades.core.model.common;

import org.apache.commons.lang3.StringUtils;

public class CardModelUtils {
    public static final String COPY_OTHER_FACE = "CopyOtherFace";
    public static final String COLLECTION = "Collection";
    public static final String ENCOUNTER_SET = "EncounterSet";
    public static final String ART_PORTRAIT = "ArtPortrait";
    public static final String GENERAL = "General";
    public static final String PLAYER = "Player";
    public static final String STORY = "Story";
    public static final String AGENDA = "Agenda";
    public static final String ACT = "Act";

    public static final String STORY_SECTION_1 = "StorySection1";
    public static final String STORY_SECTION_2 = "StorySection2";
    public static final String STORY_SECTION_3 = "StorySection3";

    // deck id take the first character and increment it by 1, e.g. 'a' -> 'b'
    // deck id should only be one character in most cases but preserve the rest of the string if there is anything
    public static String getNextDeckId(String deckId) {
        if (StringUtils.isEmpty(deckId))
            return deckId;

        return (char)(deckId.charAt(0) + 1) + StringUtils.substring(deckId, 1);
    }
}
