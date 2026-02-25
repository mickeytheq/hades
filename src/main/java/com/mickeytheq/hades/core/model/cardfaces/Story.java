package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.BaseCardFaceModel;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

@Model(typeCode = "Story", version = 1)
public class Story extends BaseCardFaceModel implements HasCommonCardFieldsModel, HasEncounterSetModel, HasCollectionModel {
    private final CommonCardFieldsModel commonCardFieldsModel = new CommonCardFieldsModel();
    private final StorySectionModel section1 = new StorySectionModel();
    private final StorySectionModel section2 = new StorySectionModel();
    private final StorySectionModel section3 = new StorySectionModel();
    private final EncounterSetModel encounterSetModel = new EncounterSetModel();
    private final CollectionModel collectionModel = new CollectionModel();
    private final StoryFieldsModel storyFieldsModel = new StoryFieldsModel();

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
    }

    @Property(CardModelUtils.GENERAL)
    public CommonCardFieldsModel getCommonCardFieldsModel() {
        return commonCardFieldsModel;
    }

    @Property("Story")
    public StoryFieldsModel getStoryFieldsModel() {
        return storyFieldsModel;
    }

    @Property(CardModelUtils.STORY_SECTION_1)
    public StorySectionModel getSection1() {
        return section1;
    }

    @Property(CardModelUtils.STORY_SECTION_2)
    public StorySectionModel getSection2() {
        return section2;
    }

    @Property(CardModelUtils.STORY_SECTION_3)
    public StorySectionModel getSection3() {
        return section3;
    }

    @Property(CardModelUtils.COLLECTION)
    public CollectionModel getCollectionModel() {
        return collectionModel;
    }

    @Property(CardModelUtils.ENCOUNTER_SET)
    public EncounterSetModel getEncounterSetModel() {
        return encounterSetModel;
    }

    public static class StoryFieldsModel {
        private PlayerCardClass playerCardClass = PlayerCardClass.Neutral;

        @Property("Class")
        public PlayerCardClass getPlayerCardClass() {
            return playerCardClass;
        }

        public void setPlayerCardClass(PlayerCardClass playerCardClass) {
            this.playerCardClass = playerCardClass;
        }
    }
}
