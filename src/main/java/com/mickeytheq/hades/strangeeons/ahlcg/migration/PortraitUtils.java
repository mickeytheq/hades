package com.mickeytheq.hades.strangeeons.ahlcg.migration;

import ca.cgjennings.apps.arkham.component.DefaultPortrait;
import ca.cgjennings.apps.arkham.diy.DIY;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class PortraitUtils {

    // collection
    public static final String COLLECTION_PORTRAIT_SUFFIX = "-Collection";
    public static final String USER_COLLECTION_ICON_PREFIX = "AHLCG-UserCollectionIcon";
    public static final String ENCOUNTER_SET_PORTRAIT_SUFFIX = "-Encounter";
    public static final String USER_ENCOUNTER_SET_ICON_PREFIX = "AHLCG-UserEncounterIcon";
    public static final String PORTRAIT_SUFFIX = "-Portrait";
    public static final String BACK_PORTRAIT_SUFFIX = "-BackPortrait";

    public static DefaultPortrait getCollectionPortrait(DIY diy) {
        return getDefaultPortrait(diy, COLLECTION_PORTRAIT_SUFFIX);
    }

    public static Optional<DefaultPortrait> findCollectionPortrait(DIY diy) {
        return findDefaultPortrait(diy, COLLECTION_PORTRAIT_SUFFIX);
    }

    public static DefaultPortrait getEncounterSetPortrait(DIY diy) {
        return getDefaultPortrait(diy, ENCOUNTER_SET_PORTRAIT_SUFFIX);
    }

    public static Optional<DefaultPortrait> findEncounterSetPortrait(DIY diy) {
        return findDefaultPortrait(diy, ENCOUNTER_SET_PORTRAIT_SUFFIX);
    }

    public static List<DefaultPortrait> getDefaultPortraits(DIY diy) {
        return IntStream.range(0, diy.getPortraitCount())
                .mapToObj(diy::getPortrait)
                .filter(o -> o instanceof DefaultPortrait)
                .map(o -> (DefaultPortrait)o)
                .collect(Collectors.toList());
    }

    public static DefaultPortrait getDefaultPortrait(DIY diy, String portraitKeySuffix) {
        Optional<DefaultPortrait> portraitMatch = getDefaultPortraits(diy).stream().filter(o -> o.getBaseKey().endsWith(portraitKeySuffix)).findFirst();

        if (!portraitMatch.isPresent())
            throw new RuntimeException("Could not find portrait with key suffix " + portraitKeySuffix + " in DIY object " + diy);

        return portraitMatch.get();
    }

    public static Optional<DefaultPortrait> findDefaultPortrait(DIY diy, String portraitKeySuffix) {
        Optional<DefaultPortrait> portraitMatch = getDefaultPortraits(diy).stream().filter(o -> o.getBaseKey().endsWith(portraitKeySuffix)).findFirst();

        return portraitMatch;
    }
}
