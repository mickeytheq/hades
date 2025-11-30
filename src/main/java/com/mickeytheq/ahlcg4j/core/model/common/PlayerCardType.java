package com.mickeytheq.ahlcg4j.core.model.common;

import com.mickeytheq.ahlcg4j.codegenerated.InterfaceConstants;
import resources.Language;

// TODO: enums like this need a parameter for a language key to be used in toString() for UI display when used in comboboxes
public enum PlayerCardType {
    Standard(InterfaceConstants.STANDARD, false, false),
    Neutral(InterfaceConstants.CLASS_NEUTRAL, false, false),
    Specialist(InterfaceConstants.CLASS_SPECIALIST, false, false),
    Story(InterfaceConstants.STORY, true, false),
    StoryWeakness(InterfaceConstants.WKNTYPE_STORYWEAKNESS, true, true),
    Weakness(InterfaceConstants.WKNTYPE_WEAKNESS, false, true),
    BasicWeakness(InterfaceConstants.WKNTYPE_BASICWEAKNESS, false, true);

    private final String languageKey;
    private final boolean hasEncounterDetails;
    private final boolean isWeakness;

    PlayerCardType(String languageKey, boolean hasEncounterDetails, boolean isWeakness) {
        this.languageKey = languageKey;
        this.hasEncounterDetails = hasEncounterDetails;
        this.isWeakness = isWeakness;
    }

    public boolean isHasEncounterDetails() {
        return hasEncounterDetails;
    }

    public boolean isWeakness() {
        return isWeakness;
    }

    @Override
    public String toString() {
        return Language.string(languageKey);
    }
}
