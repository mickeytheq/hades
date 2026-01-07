package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.BaseCardFaceModel;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.common.CollectionModel;
import com.mickeytheq.hades.core.model.common.EncounterSetModel;
import com.mickeytheq.hades.core.model.common.StorySectionModel;
import com.mickeytheq.hades.core.model.common.CommonCardFieldsModel;
import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

@Model(typeCode = "Story")
public class Story extends BaseCardFaceModel {
    private final CommonCardFieldsModel commonCardFieldsModel = new CommonCardFieldsModel();
    private final StorySectionModel section1 = new StorySectionModel();
    private final StorySectionModel section2 = new StorySectionModel();
    private final StorySectionModel section3 = new StorySectionModel();
    private final EncounterSetModel encounterSetModel = new EncounterSetModel();
    private final CollectionModel collectionModel = new CollectionModel();

    @Override
    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
    }

    @Property(flatten = true)
    public CommonCardFieldsModel getCommonCardFieldsModel() {
        return commonCardFieldsModel;
    }

    @Property("Section1")
    public StorySectionModel getSection1() {
        return section1;
    }

    @Property("Section2")
    public StorySectionModel getSection2() {
        return section2;
    }

    @Property("Section3")
    public StorySectionModel getSection3() {
        return section3;
    }

    @Property("Collection")
    public CollectionModel getCollectionModel() {
        return collectionModel;
    }

    @Property("EncounterSet")
    public EncounterSetModel getEncounterSetModel() {
        return encounterSetModel;
    }
}
