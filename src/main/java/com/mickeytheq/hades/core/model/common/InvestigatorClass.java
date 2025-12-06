package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import resources.Language;

public enum InvestigatorClass {
    Guardian(InterfaceConstants.CLASS_GUARDIAN),
    Seeker(InterfaceConstants.CLASS_SEEKER),
    Mystic(InterfaceConstants.CLASS_MYSTIC),
    Rogue(InterfaceConstants.CLASS_ROGUE),
    Survivor(InterfaceConstants.CLASS_SURVIVOR),
    Neutral(InterfaceConstants.CLASS_NEUTRAL),
    Story(InterfaceConstants.STORY);

    private final String languageKey;

    InvestigatorClass(String languageKey) {
        this.languageKey = languageKey;
    }

    public String toString() {
        return Language.string(languageKey);
    }
}
