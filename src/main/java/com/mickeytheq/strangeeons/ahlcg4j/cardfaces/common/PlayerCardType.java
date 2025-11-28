package com.mickeytheq.strangeeons.ahlcg4j.cardfaces.common;

import com.mickeytheq.strangeeons.ahlcg4j.codegenerated.InterfaceConstants;
import resources.Language;

// TODO: enums like this need a parameter for a language key to be used in toString() for UI display when used in comboboxes
public enum PlayerCardType {
    Standard(InterfaceConstants.STANDARD),
    Neutral(InterfaceConstants.CLASS_NEUTRAL),
    Specialist(InterfaceConstants.CLASS_SPECIALIST),
    Story(InterfaceConstants.STORY),
    StoryWeakness(InterfaceConstants.WKNTYPE_STORYWEAKNESS),
    Weakness(InterfaceConstants.WKNTYPE_WEAKNESS),
    BasicWeakness(InterfaceConstants.WKNTYPE_BASICWEAKNESS);

    private final String languageKey;

    PlayerCardType(String languageKey) {
        this.languageKey = languageKey;
    }

    @Override
    public String toString() {
        return Language.string(languageKey);
    }
}
