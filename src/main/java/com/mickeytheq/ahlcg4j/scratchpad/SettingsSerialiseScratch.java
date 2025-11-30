package com.mickeytheq.ahlcg4j.scratchpad;

import com.mickeytheq.ahlcg4j.serialise.StrangeEonsSettingsSerialiser;
import com.mickeytheq.ahlcg4j.core.model.common.WeaknessType;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.EncounterCardBack;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.Treachery;
import resources.Settings;

public class SettingsSerialiseScratch {
    public static void main(String[] args) {
        Treachery treachery = new Treachery();
        treachery.getCommonCardFieldsModel().setTitle("Hello");
        treachery.getCommonCardFieldsModel().setRules("Rules here");
        treachery.setWeaknessType(WeaknessType.Basic);

        EncounterCardBack encounterCardBack = new EncounterCardBack();

        Settings settings = new Settings();

        StrangeEonsSettingsSerialiser serialiser = new StrangeEonsSettingsSerialiser();

        serialiser.serialiseCardFace(settings, treachery, "Front");
        serialiser.serialiseCardFace(settings, encounterCardBack, "Back");

        Treachery deserialiseTreachery = new Treachery();
        EncounterCardBack deserialiseEncounterCardBack = new EncounterCardBack();
        serialiser.deserialiseCardFace(settings, deserialiseTreachery, "Front");
        serialiser.deserialiseCardFace(settings, deserialiseEncounterCardBack, "Back");
    }
}
