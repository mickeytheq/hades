package com.mickeytheq.ahlcg4j.scratchpad;

import com.mickeytheq.ahlcg4j.core.model.entity.AnnotatedEntityMetadataBuilder;
import com.mickeytheq.ahlcg4j.core.model.entity.EntityMetadata;
import com.mickeytheq.ahlcg4j.core.model.entity.EntityPropertyMetadata;
import com.mickeytheq.ahlcg4j.core.model.entity.PropertyMetadata;
import com.mickeytheq.ahlcg4j.core.model.common.WeaknessType;
import com.mickeytheq.ahlcg4j.core.model.common.PortraitModel;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.Treachery;

public class EntityMetadataScratch {
    public static void main(String[] args) {
        EntityMetadata entityMetadata = AnnotatedEntityMetadataBuilder.build(Treachery.class);

        Treachery treachery = new Treachery();

        PropertyMetadata propertyMetadata;

        propertyMetadata = entityMetadata.getProperty("WeaknessType");
        propertyMetadata.setPropertyValue(treachery, WeaknessType.Basic);

        propertyMetadata = entityMetadata.getProperty("Rules");
        propertyMetadata.setPropertyValue(treachery, "<rev>");

        propertyMetadata = entityMetadata.getProperty("ArtPortrait");
        PortraitModel portraitModel = (PortraitModel)propertyMetadata.getPropertyValue(treachery);

        Object panX = ((EntityPropertyMetadata)propertyMetadata).getEntityMetadata().getProperty("PanX").getPropertyValue(portraitModel);
    }
}
