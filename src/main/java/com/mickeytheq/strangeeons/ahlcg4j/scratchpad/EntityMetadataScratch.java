package com.mickeytheq.strangeeons.ahlcg4j.scratchpad;

import com.mickeytheq.strangeeons.ahlcg4j.WeaknessType;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.PortraitModel;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.treachery.Treachery;
import com.mickeytheq.strangeeons.ahlcg4j.entity.AnnotatedEntityMetadataBuilder;
import com.mickeytheq.strangeeons.ahlcg4j.entity.EntityMetadata;
import com.mickeytheq.strangeeons.ahlcg4j.entity.EntityPropertyMetadata;
import com.mickeytheq.strangeeons.ahlcg4j.entity.PropertyMetadata;

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
