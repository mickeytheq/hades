package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.codegenerated.GameConstants;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.BaseCardFaceModel;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;
import resources.Language;

@Model(typeCode = "ScenarioReference")
public class ScenarioReference extends BaseCardFaceModel implements HasCommonCardFieldsModel {
    public static class SymbolChaosTokenInfo {
        private String rules;
        private SymbolChaosToken combineWith;

        @Property("Rules")
        public String getRules() {
            return rules;
        }

        public void setRules(String rules) {
            this.rules = rules;
        }

        @Property("CombineWith")
        public SymbolChaosToken getCombineWith() {
            return combineWith;
        }

        public void setCombineWith(SymbolChaosToken combineWith) {
            this.combineWith = combineWith;
        }
    }

    public enum Difficulty {
        EasyStandard(GameConstants.DIFFICULTY_FRONT), HardExpert(GameConstants.DIFFICULTY_BACK);

        private final String languageKey;

        Difficulty(String languageKey) {
            this.languageKey = languageKey;
        }

        public String getLanguageKey() {
            return languageKey;
        }

        public String toString() {
            return Language.gstring(languageKey);
        }
    }

    public enum SymbolChaosToken {
        Skull(InterfaceConstants.CHAOS_SKULL),
        Cultist(InterfaceConstants.CHAOS_CULTIST),
        Tablet(InterfaceConstants.CHAOS_TABLET),
        ElderThing(InterfaceConstants.CHAOS_ELDERTHING);

        private final String interfaceLanguageKey;

        SymbolChaosToken(String interfaceLanguageKey) {
            this.interfaceLanguageKey = interfaceLanguageKey;
        }

        @Override
        public String toString() {
            return Language.string(interfaceLanguageKey);
        }
    }

    private final ScenarioReferenceFieldsModel scenarioReferenceFieldsModel = new ScenarioReferenceFieldsModel();
    private final CommonCardFieldsModel commonCardFieldsModel = new CommonCardFieldsModel();
    private final EncounterSetModel encounterSetModel = new EncounterSetModel();
    private final CollectionModel collectionModel = new CollectionModel();

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        if (cardFaceSide == CardFaceSide.Front) {
            getScenarioReferenceFieldsModel().setDifficulty(Difficulty.EasyStandard);
            getCommonCardFieldsModel().setCopyOtherFaceTitles(false);
        }
        else {
            getScenarioReferenceFieldsModel().setDifficulty(Difficulty.HardExpert);

            // by default copy the titles of the front face of the card
            getCommonCardFieldsModel().setCopyOtherFaceTitles(true);
        }
    }

    @Property("ScenarioReference")
    public ScenarioReferenceFieldsModel getScenarioReferenceFieldsModel() {
        return scenarioReferenceFieldsModel;
    }

    @Property(CardModelPropertyNames.GENERAL)
    public CommonCardFieldsModel getCommonCardFieldsModel() {
        return commonCardFieldsModel;
    }

    @Property(CardModelPropertyNames.ENCOUNTER_SET)
    public EncounterSetModel getEncounterSetModel() {
        return encounterSetModel;
    }

    @Property(CardModelPropertyNames.COLLECTION)
    public CollectionModel getCollectionModel() {
        return collectionModel;
    }

    public static class ScenarioReferenceFieldsModel {
        private Difficulty difficulty = Difficulty.EasyStandard;
        private final SymbolChaosTokenInfo skull = new SymbolChaosTokenInfo();
        private final SymbolChaosTokenInfo cultist = new SymbolChaosTokenInfo();
        private final SymbolChaosTokenInfo tablet = new SymbolChaosTokenInfo();
        private final SymbolChaosTokenInfo elderThing = new SymbolChaosTokenInfo();
        private String trackingBox;

        @Property("Difficulty")
        public Difficulty getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(Difficulty difficulty) {
            this.difficulty = difficulty;
        }

        @Property("Skull")
        public SymbolChaosTokenInfo getSkull() {
            return skull;
        }

        @Property("Cultist")
        public SymbolChaosTokenInfo getCultist() {
            return cultist;
        }

        @Property("Tablet")
        public SymbolChaosTokenInfo getTablet() {
            return tablet;
        }

        @Property("ElderThing")
        public SymbolChaosTokenInfo getElderThing() {
            return elderThing;
        }

        @Property("TrackingBox")
        public String getTrackingBox() {
            return trackingBox;
        }

        public void setTrackingBox(String trackingBox) {
            this.trackingBox = trackingBox;
        }
    }
}
