package com.mickeytheq.strangeeons.ahlcg4j.scratchpad;

import com.mickeytheq.strangeeons.ahlcg4j.Card;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.backs.EncounterCardBack;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.treachery.Treachery;
import com.mickeytheq.strangeeons.ahlcg4j.serialise.StrangeEonsSettingsSerialiser;
import resources.Settings;

public class SettingsSerialiseScratch {
    public static void main(String[] args) {
        StrangeEonsSettingsSerialiser serialiser = new StrangeEonsSettingsSerialiser();

        Treachery treachery = new Treachery();
        treachery.getCommonCardFieldsModel().setTitle("Hello");

        EncounterCardBack encounterCardBack = new EncounterCardBack();

        Settings settings = new Settings();

        serialiser.serialiseCardFace(settings, treachery, "Front");
        serialiser.serialiseCardFace(settings, encounterCardBack, "Back");
    }
}
