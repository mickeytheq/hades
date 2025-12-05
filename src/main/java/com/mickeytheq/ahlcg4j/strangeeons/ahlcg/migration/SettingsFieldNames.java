package com.mickeytheq.ahlcg4j.strangeeons.ahlcg.migration;

// settings keys and other values used by the AHLCG plugin
public class SettingsFieldNames {
    // the suffix used to differentiate settings on the back of a card
    public static final String BACK_SUFFIX = "Back";

    // general fields
    public static final String ENCOUNTER_TOTAL = "EncounterTotal";
    public static final String ENCOUNTER_NUMBER = "EncounterNumber";
    public static final String ENCOUNTER_SET_TYPE = "EncounterType";
    public static final String COLLECTION_NAME = "Collection";
    public static final String COLLECTION_TYPE = "CollectionType";
    public static final String COLLECTION_NUMBER = "CollectionNumber";
    public static final String ENCOUNTER_SET_NAME = "Encounter";
    public static final String BACK_TYPE_BACK = "BackTypeBack";
    public static final String TEMPLATE_REPLACEMENT_BACK = "TemplateReplacementBack";

    // back card type codes
    public static final String BACK_TYPE_STANDARD = "Standard";
    public static final String BACK_TYPE_PLAYER = "Player";
    public static final String BACK_TYPE_ENCOUNTER = "Encounter";
    public static final String BACK_TYPE_PLAYER_PURPLE = "PlayerPurple";

    // general fields
    public static final String TITLE_BACK = "TitleBack";
    public static final String SUBTITLE = "Subtitle";
    public static final String TRAITS = "Traits";
    public static final String KEYWORDS = "Keywords";
    public static final String GAME_TEXT = "Rules";
    public static final String FLAVOR = "Flavor";
    public static final String VICTORY = "Victory";
    public static final String ARTIST = "Artist";
    public static final String STORY = "Story";
    public static final String SUBTYPE = "Subtype";

    // campaign guide
    public static final String PAGE_NUMBER = "Page";
    public static final String RULES_LEFT = "RulesLeft";
    public static final String RULES_RIGHT = "RulesRight";

    // customizable upgrade
    public static final String CUSTOMIZABLE_UPGRADE_SECTION_NAME = "CustName";
    public static final String CUSTOMIZABLE_UPGRADE_SECTION_COST = "CustCost";
    public static final String CUSTOMIZABLE_UPGRADE_SECTION_GAME_TEXT = "CustText";

    public static final String CUSTOM_COLLECTION_NAME = "CustomCollection";
    public static final String CUSTOM_ENCOUNTER_SET_NAME = "CustomEncounterSet";


    // story sections - shared by a few cards including Act/Agenda backs and Story faces
    public static final String STORY_SECTION_HEADER_PREFIX = "Header";
    public static final String STORY_SECTION_STORY_PREFIX = "AccentedStory";
    public static final String STORY_GAME_TEXT_PREFIX = "Rules";

    // act/agenda
    public static final String ACT_STORY = "ActStory";
    public static final String AGENDA_STORY = "AgendaStory";


    // special code for duplicating settings
    public static final String COPY_FRONT = "Copy front";

    // investigator
    public static final String INVESTIGATOR_STORY = "InvStory";


    // stats
    public static final String HEALTH = "Stamina";
    public static final String SANITY = "Sanity";
    public static final String WILLPOWER = "Willpower";
    public static final String INTELLECT = "Intellect";
    public static final String COMBAT = "Combat";
    public static final String AGILITY = "Agility";


    // chaos tokens
    public static final String CHAOS_TOKEN_SKULL = "Skull";
    public static final String CHAOS_TOKEN_CULTIST = "Cultist";
    public static final String CHAOS_TOKEN_TABLET = "Tablet";
    public static final String CHAOS_TOKEN_ELDER_THING = "ElderThing";

    // statistics
    public static final String ENEMY_HEALTH = "Health";
    public static final String ENEMY_DAMAGE = "Damage";
    public static final String ENEMY_HORROR = "Horror";
    public static final String ENEMY_COMBAT = "Attack";
    public static final String ENEMY_EVADE = "Evade";

    public static final String CARD_CLASS = "CardClass";

    public static final String PER_INVESTIGATOR = "PerInvestigator";

    public static final String SPECIALIST_CLASS_NAME = "Specialist";
    public static final String STORY_CLASS_NAME = "Story";
    public static final String STORY_WEAKNESS_CLASS_NAME = "StoryWeakness";
}
